"""
FraudShield AI — LLM Provider
Configurable LLM integration for scam analysis.
Supports Google Gemini, OpenAI, and extensible to other providers.
"""

import os
import json
import logging
from typing import Optional
from app.models.schemas import AnalysisResult, RedFlag, RiskScoreBreakdown, RiskLevel, InputType
from app.scoring.risk_engine import calculate_risk_score, get_risk_level_from_score

logger = logging.getLogger("fraudshield.llm")

# ── Structured prompt for scam analysis ──

SYSTEM_PROMPT = """You are FraudShield AI, a specialized scam detection engine for Indian cyber fraud.
Your ONLY purpose is to analyze communications (SMS, WhatsApp, call transcripts) and detect scam patterns.

You must respond with ONLY valid JSON matching this exact schema:
{
    "scam_type": "one of: Digital Arrest Scam, Fake Bank Support, Investment Scam, Prepaid Task / Job Scam, Phishing, UPI / Payment Scam, Government / Authority Impersonation, Lottery / Prize Scam, Legitimate",
    "confidence": 0.0 to 1.0,
    "summary": "brief summary of the analysis",
    "explanation": "detailed plain-language explanation of why this is dangerous (or safe)",
    "red_flags": [
        {"indicator": "flag name", "description": "why this is a red flag", "severity": "low/medium/high/critical"}
    ],
    "recommended_actions": ["action 1", "action 2"],
    "detected_entities": ["entity 1", "entity 2"]
}

Focus on Indian scam patterns including:
- Digital arrest scams (CBI/police impersonation, arrest threats)
- Fake bank support (KYC scams, account blocking threats)  
- Investment scams (guaranteed returns, crypto fraud)
- Task/job scams (prepaid tasks, registration fees)
- Phishing (fake links, credential harvesting)
- UPI/payment scams (fake collect requests, QR code scams)
- Government impersonation (Aadhaar fraud, tax scams)

Be thorough but do not claim 100% certainty. Provide actionable safety guidance."""


ANALYSIS_PROMPT_TEMPLATE = """Analyze this {input_type} for scam indicators:

---
{text}
---

Classify the scam type, identify red flags, explain why it's dangerous (or safe if legitimate), and provide safety recommendations.
Respond with ONLY valid JSON matching the required schema."""


class LLMProvider:
    """Configurable LLM provider for AI-powered scam analysis."""
    
    def __init__(self):
        self.provider = os.getenv("AI_PROVIDER", "fallback")
        self.model = os.getenv("AI_MODEL", "")
        self.api_key = os.getenv("AI_API_KEY", "")
        self._client = None
        
        # Provider-specific setup
        if self.provider == "gemini":
            self.api_key = self.api_key or os.getenv("GEMINI_API_KEY", "")
            self.model = self.model or os.getenv("GEMINI_MODEL", "gemini-2.0-flash")
        elif self.provider == "openai":
            self.api_key = self.api_key or os.getenv("OPENAI_API_KEY", "")
            self.model = self.model or os.getenv("OPENAI_MODEL", "gpt-4o-mini")
        
        logger.info(f"LLM Provider initialized: {self.provider} (model: {self.model or 'N/A'})")
    
    def is_available(self) -> bool:
        """Check if an external LLM provider is configured and available."""
        return self.provider in ("gemini", "openai") and bool(self.api_key)
    
    async def analyze(self, text: str, input_type: str = "text") -> Optional[AnalysisResult]:
        """
        Analyze text using the configured LLM provider.
        Returns None if the provider is unavailable or fails.
        """
        if not self.is_available():
            return None
        
        try:
            if self.provider == "gemini":
                return await self._analyze_gemini(text, input_type)
            elif self.provider == "openai":
                return await self._analyze_openai(text, input_type)
            else:
                return None
        except Exception as e:
            logger.error(f"LLM analysis failed: {e}")
            return None
    
    async def _analyze_gemini(self, text: str, input_type: str) -> Optional[AnalysisResult]:
        """Analyze using Google Gemini API."""
        try:
            import google.generativeai as genai
            
            genai.configure(api_key=self.api_key)
            model = genai.GenerativeModel(self.model)
            
            prompt = ANALYSIS_PROMPT_TEMPLATE.format(input_type=input_type, text=text)
            
            response = model.generate_content(
                [{"role": "user", "parts": [SYSTEM_PROMPT + "\n\n" + prompt]}],
                generation_config=genai.types.GenerationConfig(
                    response_mime_type="application/json",
                    temperature=0.2,
                )
            )
            
            return self._parse_llm_response(response.text, text, input_type)
        except Exception as e:
            logger.error(f"Gemini analysis failed: {e}")
            return None
    
    async def _analyze_openai(self, text: str, input_type: str) -> Optional[AnalysisResult]:
        """Analyze using OpenAI API."""
        try:
            from openai import AsyncOpenAI
            
            client = AsyncOpenAI(api_key=self.api_key)
            prompt = ANALYSIS_PROMPT_TEMPLATE.format(input_type=input_type, text=text)
            
            response = await client.chat.completions.create(
                model=self.model,
                messages=[
                    {"role": "system", "content": SYSTEM_PROMPT},
                    {"role": "user", "content": prompt}
                ],
                response_format={"type": "json_object"},
                temperature=0.2,
                max_tokens=1500,
            )
            
            return self._parse_llm_response(response.choices[0].message.content, text, input_type)
        except Exception as e:
            logger.error(f"OpenAI analysis failed: {e}")
            return None
    
    def _parse_llm_response(self, response_text: str, original_text: str, input_type: str) -> Optional[AnalysisResult]:
        """Parse LLM JSON response and merge with deterministic risk scoring."""
        try:
            # Clean response text
            clean = response_text.strip()
            if clean.startswith("```json"):
                clean = clean[7:]
            if clean.startswith("```"):
                clean = clean[3:]
            if clean.endswith("```"):
                clean = clean[:-3]
            clean = clean.strip()
            
            data = json.loads(clean)
            
            # Use deterministic risk scoring (not LLM-generated score)
            risk_score, risk_level, score_breakdown = calculate_risk_score(original_text)
            
            # If LLM classified as legitimate but our engine found signals, use higher score
            llm_scam_type = data.get("scam_type", "Unknown Scam Type")
            if llm_scam_type == "Legitimate" and risk_score >= 30:
                llm_scam_type = "Unknown Scam Type"
            elif llm_scam_type != "Legitimate" and risk_score < 15:
                # If LLM detects a scam but rule engine doesn't, boost the score slightly
                risk_score = max(risk_score, 35)
                risk_level = get_risk_level_from_score(risk_score)
            
            # Parse red flags from LLM
            red_flags = []
            for rf in data.get("red_flags", []):
                if isinstance(rf, dict):
                    red_flags.append(RedFlag(
                        indicator=rf.get("indicator", "Unknown"),
                        description=rf.get("description", ""),
                        severity=rf.get("severity", "high")
                    ))
            
            # Build confidence from LLM + deterministic signals
            llm_confidence = float(data.get("confidence", 0.7))
            det_confidence = 0.92 if risk_score >= 80 else (0.85 if risk_score >= 60 else 0.72)
            confidence = round((llm_confidence * 0.6 + det_confidence * 0.4), 2)
            
            return AnalysisResult(
                scam_type=llm_scam_type,
                risk_score=risk_score,
                risk_level=risk_level,
                confidence=confidence,
                summary=data.get("summary", ""),
                explanation=data.get("explanation", ""),
                red_flags=red_flags,
                recommended_actions=data.get("recommended_actions", []),
                detected_entities=data.get("detected_entities", []),
                score_breakdown=score_breakdown,
                input_type=InputType(input_type) if input_type in [e.value for e in InputType] else InputType.TEXT,
                input_text=original_text,
                ai_provider=self.provider,
            )
        except (json.JSONDecodeError, KeyError, ValueError) as e:
            logger.error(f"Failed to parse LLM response: {e}")
            return None


# Singleton instance
_llm_provider: Optional[LLMProvider] = None

def get_llm_provider() -> LLMProvider:
    """Get or create the LLM provider singleton."""
    global _llm_provider
    if _llm_provider is None:
        _llm_provider = LLMProvider()
    return _llm_provider
