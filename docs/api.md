# FraudShield AI — API Documentation

## Base URL
```
http://localhost:8000
```

## Interactive Documentation
- Swagger UI: `http://localhost:8000/docs`
- ReDoc: `http://localhost:8000/redoc`

---

## Endpoints

### Health Check

#### `GET /health`
Check if the backend is running.

**Response:**
```json
{
    "status": "healthy",
    "service": "FraudShield AI Backend",
    "version": "1.0.0",
    "ai_provider": "fallback",
    "stt_provider": "demo",
    "firebase_enabled": false
}
```

---

### Analysis

#### `POST /api/analyze/message`
Analyze a suspicious text message.

**Request:**
```json
{
    "message": "This is CBI. Transfer money to avoid arrest.",
    "input_type": "sms",
    "source_info": "Unknown number"
}
```

**Response:** `AnalysisResult` (see schema below)

---

#### `POST /api/analyze/transcript`
Analyze a call transcript.

**Request:**
```json
{
    "transcript": "Caller: This is the customs department...",
    "source_info": "Unknown caller"
}
```

**Response:** `AnalysisResult`

---

#### `POST /api/analyze/audio`
Upload audio for Speech-to-Text transcription + analysis.

**Request:** `multipart/form-data` with `file` field

**Response (Demo Mode):**
```json
{
    "status": "demo_mode",
    "message": "STT is in demo mode. Please paste the transcript manually.",
    "provider": "demo",
    "transcript": "",
    "analysis": null
}
```

---

### Scan History

#### `GET /api/scans`
List all previous scan results.

#### `GET /api/scans/{scan_id}`
Get full details of a specific scan.

---

### Reports

#### `POST /api/reports`
Submit an incident report.

**Request:**
```json
{
    "scan_id": "uuid-here",
    "scam_type": "Digital Arrest Scam",
    "risk_score": 92,
    "description": "Received threatening call"
}
```

---

### Dashboard

#### `GET /api/stats`
Get dashboard statistics.

**Response:**
```json
{
    "total_scans": 5,
    "threats_detected": 4,
    "high_risk_alerts": 3,
    "critical_alerts": 2,
    "protection_status": "active"
}
```

---

### Demo

#### `GET /api/demo/scenarios`
List all demo scenarios.

#### `POST /api/demo/run/{scenario_id}`
Run a preset demo scenario.

---

## AnalysisResult Schema

```json
{
    "id": "uuid",
    "scam_type": "Digital Arrest Scam",
    "risk_score": 92,
    "risk_level": "CRITICAL",
    "confidence": 0.92,
    "summary": "...",
    "explanation": "...",
    "red_flags": [
        {
            "indicator": "Government Impersonation",
            "description": "...",
            "severity": "critical"
        }
    ],
    "recommended_actions": ["Do not transfer money", "..."],
    "detected_entities": ["CBI", "arrest"],
    "score_breakdown": [
        {"signal": "Government Impersonation", "points": 25, "description": "..."}
    ],
    "input_type": "sms",
    "input_text": "...",
    "ai_provider": "fallback",
    "created_at": "2024-01-01T00:00:00",
    "disclaimer": "AI-generated risk assessment. Verify through official channels."
}
```
