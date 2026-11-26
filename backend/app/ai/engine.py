"""
FraudShield AI — AI Engine Orchestrator
Routes analysis requests to LLM or fallback classifier.
Implements the four-stage pipeline: DETECT → UNDERSTAND → CLASSIFY → PROTECT
"""

import logging
from app.models.schemas import AnalysisResult
from app.ai.llm_provider import get_llm_provider
from app.ai.fallback_classifier import analyze_with_fallback

logger = logging.getLogger("fraudshield.engine")


async def analyze_communication(text: str, input_type: str = "text") -> AnalysisResult:
    """
    Main analysis pipeline — the core of FraudShield AI.
    
    Pipeline stages:
    1. DETECT — Receive and normalize input
    2. UNDERSTAND — Extract context, intent, and suspicious language  
    3. CLASSIFY — Identify scam type and calculate risk score
    4. PROTECT — Generate warning, explanation, and safety guidance
    
    Routes to configured LLM provider first, falls back to rule-based engine.
    """
    # Stage 1: DETECT — Normalize input
    normalized_text = normalize_input(text)
    
    if not normalized_text or len(normalized_text.strip()) < 3:
        return AnalysisResult(
            scam_type="Legitimate",
            risk_score=0,
            risk_level="LOW",
            confidence=0.95,
            summary="Input too short for meaningful analysis.",
            explanation="The provided text is too short to analyze for scam patterns.",
            red_flags=[],
            recommended_actions=["Provide a longer message for better analysis."],
            detected_entities=[],
            score_breakdown=[],
            input_text=text,
            ai_provider="none",
        )
    
    # Stages 2-4: UNDERSTAND → CLASSIFY → PROTECT
    # Try LLM provider first
    llm = get_llm_provider()
    
    if llm.is_available():
        logger.info(f"Using LLM provider: {llm.provider}")
        result = await llm.analyze(normalized_text, input_type)
        if result is not None:
            result.input_text = text  # Keep original text
            return result
        logger.warning("LLM analysis failed, falling back to rule-based engine")
    
    # Fallback to rule-based classifier
    logger.info("Using fallback rule-based classifier")
    result = analyze_with_fallback(normalized_text, input_type)
    result.input_text = text  # Keep original text
    return result


def normalize_input(text: str) -> str:
    """
    Normalize input text for consistent analysis.
    Preserves meaning while cleaning formatting.
    """
    if not text:
        return ""
    
    # Strip leading/trailing whitespace
    text = text.strip()
    
    # Normalize unicode quotes and dashes
    replacements = {
        '\u201c': '"', '\u201d': '"',  # Smart quotes
        '\u2018': "'", '\u2019': "'",
        '\u2013': '-', '\u2014': '-',  # Dashes
        '\u00a0': ' ',  # Non-breaking space
    }
    for old, new in replacements.items():
        text = text.replace(old, new)
    
    # Collapse multiple whitespace
    import re
    text = re.sub(r'\s+', ' ', text)
    
    return text
