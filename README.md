# ThreatGuard AI

### AI-Powered Scam Detection and Protection

> **DETECT • UNDERSTAND • PROTECT**
>
> *Designed for the Maxim Code AI Software Innovation Competition.*

---

## 🚀 Project Access

- 🌐 **Local Web Demo**: http://localhost:8000/demo/
- 📄 **API Documentation**: http://localhost:8000/docs

Run the FastAPI backend locally to access these links.

---

## 1. Problem
India is experiencing a massive increase in cyber fraud, particularly **"Digital Arrest" scams**. In these scams, fraudsters impersonate government agencies, police, CBI, tax authorities, or banks, psychologically pressuring victims under the threat of arrest or account freezing. 

The core challenge isn't simply a lack of awareness; it is **timing**. Most victims realize they have been scammed only **AFTER** their money has been transferred. There is a critical lack of real-time intervention at the exact moment the suspicious communication is taking place.

## 2. Solution
**FraudShield AI** is an AI-powered real-time scam detection and user protection platform. It provides an immediate warning at the moment suspicious communication is analyzed. 

Instead of acting as a generic educational chatbot or a post-facto security dashboard, FraudShield AI focuses entirely on **"Intervention before financial loss."**

## 3. Why FraudShield AI Stands Out
- **Innovation**: Real-time scam intelligence pipeline instead of a generic chat interface.
- **Problem Solving**: Focuses on the exact moment of pressure to intercept financial transactions.
- **Explainable AI (XAI)**: Displays why a message was flagged, listing specific red flags, a risk score, and plain-language reasoning.
- **Technical Skills**: Combines Android (Kotlin + Compose), Python (FastAPI), Speech-to-Text abstractions, rule-based heuristics, and configurable LLMs.

---

## 4. Features
- **SMS & WhatsApp Scan**: Copy-paste or upload messages for analysis.
- **Call Transcript Scan**: Paste or simulate a call conversation for scanning.
- **Audio File Upload**: Speech-to-Text transcription followed by scam pattern recognition.
- **Scam Classification**: Classifies text into 8 distinct scam categories.
- **Explainable Warning Alerts**: Prominent danger screen with detailed red flags.
- **Reporting Interface**: One-click action to guide reporting the scam to the **National Cyber Crime Helpline (1930)**.
- **Scan History**: Stores previous scans locally and optionally to Firebase Firestore.
- **Interactive Web & Mobile App**: Demo web portal and full Jetpack Compose Android client.

---

## 5. How It Works
The platform implements a distinct four-stage workflow:

1. **INPUT (Detect)**: The user submits suspicious communication (text, transcript, or audio).
2. **ANALYSIS (Understand)**: The system normalizes the input and applies Speech-to-Text (STT) + Natural Language Processing (NLP).
3. **SCAM INTELLIGENCE (Classify)**: Real-time checks extract scam patterns, classify the category, and calculate an explainable risk score.
4. **PROTECTION (Protect)**: The user receives a detailed warning dashboard, highlighted red flags, and immediate safety recommendations (e.g. *"No Indian agency conducts arrests via video call. Do not transfer funds."*).

---

## 6. AI Architecture & Risk Scoring
FraudShield AI operates a hybrid intelligence layer:

```
Context Analysis ──> Pattern Detection ──> Scam Classification ──> Risk Scoring ──> Explainable Alert
```

### Deterministic Risk Scoring
Rather than relying blindly on an LLM-generated risk score, the system calculates score factors deterministically based on key security indicators:
- Government / Law Enforcement Impersonation: **+25 points**
- Arrest / Legal Threat: **+25 points**
- Urgent Payment / Money Demand: **+20 points**
- OTP / PIN / Password Request: **+20 points**
- Fear / Psychological Coercion: **+15 points**
- Suspicious URL / Link: **+15 points**
- Investment / Returns Guarantee: **+20 points**
- Prepaid Task / Job Fraud: **+20 points**
- Bank / KYC Impersonation: **+20 points**
- Lottery / Prize / Reward Scam: **+18 points**
- Aadhaar / Identity Threat: **+18 points**
- UPI / Payment Fraud: **+18 points**

#### Score Normalization
- **0–29**: LOW Risk
- **30–59**: MEDIUM Risk
- **60–79**: HIGH Risk
- **80–100**: CRITICAL Risk

---

## 7. Technology Stack
- **Backend**: Python 3.13, FastAPI, Uvicorn, Pydantic, Pytest
- **Mobile Application**: Android Kotlin, Jetpack Compose, Material 3, Retrofit, OkHttp, Room Database, Jetpack Navigation
- **Web Demo Interface**: Vanilla HTML5, CSS3, ES6 JavaScript
- **Cloud Infrastructure**: Firebase Firestore (optional storage)
- **AI Integrations**: Configurable LLM API (Google Gemini, OpenAI GPT, or Fallback Engine)

---

## 8. Scam Categories
1. **Digital Arrest Scam**
2. **Fake Bank Support**
3. **Investment Scam**
4. **Prepaid Task / Job Scam**
5. **Phishing**
6. **UPI / Payment Scam**
7. **Government / Authority Impersonation**
8. **Lottery / Prize Scam**

---

## 9. Demo Scenarios
The project contains 6 preset demo scenarios in the database:
1. **Digital Arrest Scenario (CRITICAL)**: "CBI" video call money-laundering threat.
2. **Fake Bank Support (CRITICAL)**: "SBI" customer care KYC block threat + OTP request.
3. **Investment Scam (HIGH)**: 200% guaranteed return Telegram stock group invitation.
4. **Prepaid Task Scam (HIGH)**: Earn ₹500/task with upfront registration fee.
5. **Phishing SMS (HIGH)**: Urgent SBI account block notification link.
6. **Safe Message (LOW)**: Normal dinner invitation.

---

## 10. Project Structure
```
FraudShield-AI/
│
├── android-app/             # Kotlin + Compose Android app
│   ├── app/                 # Jetpack Compose UI, Retrofit API client
│   └── README.md            # Mobile app documentation
│
├── backend/                 # FastAPI server codebase
│   ├── app/                 
│   │   ├── main.py          # FastAPI server entry point & static mounts
│   │   ├── api/             # API Router endpoints
│   │   ├── models/          # Pydantic schemas
│   │   ├── services/        # Firebase store, STT service, demo data
│   │   ├── ai/              # Configurable LLM and Rule Fallback Engine
│   │   └── scoring/         # Heuristics & risk scoring engines
│   ├── requirements.txt     # Python backend dependencies
│   └── .env.example         # Environment template configuration
│
├── demo-web/                # HTML + CSS + JS developer/judging interface
│   ├── index.html           
│   ├── style.css            
│   └── app.js               
│
├── docs/                    # Deep-dive architecture and API specifications
│   ├── architecture.md      
│   ├── api.md               
│   └── demo-scenarios.md    
│
├── README.md                # Root product overview & instructions
└── LICENSE                  # MIT License
```

---

## 11. Setup & Installation

### Environment Variables
Configure the backend by creating a `.env` file in the `backend/` folder:
```bash
# Copy example
cp backend/.env.example backend/.env
```
Fill in the parameters (LLM configurations are optional; the fallback engine will run automatically if left empty):
```env
AI_PROVIDER=fallback
STT_PROVIDER=demo
FIREBASE_ENABLED=false
```

### Running the Backend
1. Navigate to the backend directory:
   ```powershell
   cd backend
   ```
2. Set up the virtual environment:
   ```powershell
   python -m venv venv
   .\venv\Scripts\activate
   ```
3. Install dependencies:
   ```powershell
   pip install -r requirements.txt
   ```
4. Start the FastAPI server:
   ```powershell
   uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
   ```
5. View API docs at: `http://localhost:8000/docs`
6. Access the Web Demo interface directly at: `http://localhost:8000/demo/`

### Running the Android App
1. Open the `/android-app` folder inside Android Studio.
2. Ensure Gradle synchronization completes successfully.
3. Choose a device (or emulator) running Android API 26 or higher.
4. Modify `android-app/app/build.gradle.kts` `API_BASE_URL` to point to your machine's local IP address (or `http://10.0.2.2:8000` for default Android emulators).
5. Compile and run the application.

---

## 12. Security, Privacy, & Limitations
- **Technical Honesty**: FraudShield AI acts as an analyzer for *user-submitted* communications (text, files, transcripts). It does **NOT** intercept live phone calls or intercept WhatsApp data directly due to Android sandbox and permission models. The architecture is modular to allow integration with call-screen APIs later.
- **False Positives**: The application displays a prominent warning disclaimer: *"AI-generated risk assessment. Verify critical decisions through official channels."* It does not make absolute legal claims.
- **Privacy First**: Sensitive data is processed transiently and is not stored unless a user explicitly saves a scan to their local history.

---

## 13. Team & Competition
- **Project**: ThreatGaurd
- **Competition**: Maxim Code AI Software Innovation Competition
- **Team**: Students Innovation Team
- **License**: MIT License
