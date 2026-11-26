# FraudShield AI — Architecture

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      CLIENT LAYER                            │
├──────────────────────┬──────────────────────────────────────┤
│   Android App        │   Web Demo Interface                  │
│   (Kotlin/Compose)   │   (HTML/CSS/JS)                       │
│   - Material 3 UI    │   - Premium Cybersecurity Theme       │
│   - MVVM Pattern     │   - Analysis + Results                │
│   - Retrofit HTTP    │   - Demo Scenarios                    │
└──────────┬───────────┴────────────┬─────────────────────────┘
           │                        │
           │      HTTPS / REST      │
           └───────────┬────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                   BACKEND (FastAPI)                           │
├─────────────────────────────────────────────────────────────┤
│  API Router                                                  │
│  ├── POST /analyze/message    ← Text Analysis                │
│  ├── POST /analyze/transcript ← Call Transcript Analysis     │
│  ├── POST /analyze/audio      ← Audio → STT → Analysis      │
│  ├── GET  /scans              ← Scan History                 │
│  ├── GET  /scans/{id}         ← Scan Details                 │
│  ├── POST /reports            ← Incident Reporting           │
│  ├── GET  /stats              ← Dashboard Stats              │
│  └── GET  /demo/scenarios     ← Demo Presets                 │
├─────────────────────────────────────────────────────────────┤
│  AI Engine Orchestrator                                      │
│  ├── LLM Provider (Gemini / OpenAI / configurable)           │
│  └── Fallback Rule-Based Classifier                          │
├─────────────────────────────────────────────────────────────┤
│  Risk Scoring Engine (Deterministic)                         │
│  ├── 12 Signal Categories                                    │
│  ├── Weighted Scoring (0-100)                                │
│  └── Explainable Breakdown                                   │
├─────────────────────────────────────────────────────────────┤
│  Services                                                    │
│  ├── Speech-to-Text (Google / Whisper / Demo)                │
│  ├── Scan Store (In-Memory + Firebase)                       │
│  └── Demo Scenarios                                          │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                   STORAGE LAYER                              │
│  ├── In-Memory Store (default, always available)             │
│  └── Firebase Firestore (optional, configurable)             │
└─────────────────────────────────────────────────────────────┘
```

## AI Pipeline: DETECT → UNDERSTAND → CLASSIFY → PROTECT

```
INPUT (SMS / WhatsApp / Call Transcript / Audio)
    │
    ▼
┌───────────────────────┐
│  STAGE 1: DETECT      │
│  - Receive input      │
│  - Normalize text     │
│  - Identify type      │
└───────────┬───────────┘
            ▼
┌───────────────────────┐
│  STAGE 2: UNDERSTAND  │
│  - STT (if audio)     │
│  - Context analysis   │
│  - Intent extraction  │
│  - Entity detection   │
└───────────┬───────────┘
            ▼
┌───────────────────────┐
│  STAGE 3: CLASSIFY    │
│  - Pattern matching   │
│  - Scam type ID       │
│  - Risk scoring       │
│  - Confidence calc    │
└───────────┬───────────┘
            ▼
┌───────────────────────┐
│  STAGE 4: PROTECT     │
│  - Red flag list      │
│  - Explanation gen    │
│  - Safety guidance    │
│  - Score breakdown    │
└───────────┬───────────┘
            ▼
    STRUCTURED RESULT
```

## Risk Scoring Signals

| Signal | Points | Description |
|--------|--------|-------------|
| Government Impersonation | +25 | CBI, police, customs, etc. |
| Arrest / Legal Threat | +25 | Arrest warrants, investigations |
| Urgent Payment Request | +20 | Immediate money transfer demands |
| OTP / PIN / Password | +20 | Credential harvesting |
| Fear / Coercion | +15 | Psychological manipulation |
| Suspicious Links | +15 | Malicious URLs |
| Investment Guarantee | +20 | Guaranteed returns claims |
| Prepaid Task / Job | +20 | Registration fee scams |
| Bank / KYC Impersonation | +20 | Fake bank support |
| Lottery / Prize | +18 | Fake lottery wins |
| Aadhaar / Identity | +18 | Identity fraud |
| UPI / Payment | +18 | UPI scam patterns |

## Risk Levels

| Score Range | Level | Color |
|-------------|-------|-------|
| 0-29 | LOW | Green |
| 30-59 | MEDIUM | Yellow |
| 60-79 | HIGH | Orange |
| 80-100 | CRITICAL | Red |
