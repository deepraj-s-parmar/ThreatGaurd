# 🛡️ ThreatGuard AI
### AI-Powered Scam Detection and Protection

ThreatGuard AI is a web-based security application designed to help users identify potential digital scams and suspicious activities. It provides a platform for analyzing potentially fraudulent content and understanding possible security risks.

🌐 **Live Demo:** https://threatgaurd.onrender.com/demo/

📚 **API Documentation:** https://threatgaurd.onrender.com/docs

💻 **GitHub Repository:** https://github.com/deepraj-s-parmar/ThreatGaurd

---

## 🚀 Project Overview

Digital scams, phishing attempts, and fraudulent messages are increasingly common. Many users struggle to identify suspicious links, messages, and online activities.

ThreatGuard AI aims to make digital threat awareness easier through a user-friendly interface and backend analysis services.

The project combines a web frontend with a Python-based backend to provide a centralized platform for security analysis.

## ✨ Key Features

- **Threat Analysis:** Analyze submitted content for potential security risks.
- **Scam Detection:** Help identify suspicious and potentially fraudulent content.
- **Risk Assessment:** Present analysis results to help users understand potential threats.
- **Interactive Dashboard:** Simple interface for interacting with the detection system.
- **API Integration:** Backend endpoints for communication between the frontend and analysis services.
- **Web-Based Access:** Access the application through a browser without installing desktop software.

*Note: Available detection capabilities depend on the configured backend providers.*

## 🛠️ Technology Stack

| Component | Technology |
|---|---|
| Frontend | HTML, CSS, JavaScript |
| Backend | Python, FastAPI |
| API Server | Uvicorn |
| Data Validation | Pydantic |
| AI Integration | Google Generative AI, OpenAI (provider support) |
| Database / Cloud Support | Firebase Admin SDK |
| Deployment | Render |
| Version Control | Git and GitHub |

## 🏗️ Project Structure

```text
ThreatGaurd/
│
├── android-app/
│
├── backend/
│   ├── app/
│   │   ├── main.py
│   │   ├── api/
│   │   ├── models/
│   │   ├── services/
│   │   ├── ai/
│   │   └── scoring/
│   │
│   ├── requirements.txt
│   ├── .env.example
│   └── .env
│
├── demo-web/
│   ├── index.html
│   ├── style.css
│   └── app.js
│
├── docs/
├── screenshots/
│
├── .gitignore
├── LICENSE
└── README.md
```

## 💻 Run Locally

### 1. Clone the repository

```bash
git clone https://github.com/deepraj-s-parmar/ThreatGaurd.git
```

### 2. Navigate to the backend

```bash
cd ThreatGaurd/backend
```

### 3. Create a virtual environment

```bash
python -m venv venv
```

Activate it on Windows:

```powershell
venv\Scripts\activate
```

### 4. Install dependencies

```bash
pip install -r requirements.txt
```

### 5. Configure environment variables

Create a `.env` file inside the `backend` folder.

Example configuration:

```env
AI_PROVIDER=fallback
STT_PROVIDER=demo
FIREBASE_ENABLED=false
```

For integrations requiring API credentials, configure the appropriate credentials and provider settings separately.

### 6. Start the backend server

Run this command from the `backend` directory:

```bash
uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
```

### 7. Open the application

| Service | Local URL |
|---|---|
| Web Application | http://localhost:8000/demo/ |
| API Documentation | http://localhost:8000/docs |
| Health Check | http://localhost:8000/health |

## ☁️ Deployment

ThreatGuard AI is deployed using **Render**.

The deployment runs the FastAPI backend and serves the frontend from the same application.

- Hosting Platform: Render
- Runtime: Python 3.12
- Root Directory: `backend`
- Build Command:

```bash
pip install -r requirements.txt
```

- Start Command:

```bash
uvicorn app.main:app --host 0.0.0.0 --port $PORT
```

**Live Application:** https://threatgaurd.onrender.com/demo/

## 🔌 API

ThreatGuard AI uses FastAPI to provide backend API functionality.

Explore the available endpoints and interactive API interface here:

https://threatgaurd.onrender.com/docs

## ⚙️ Current Configuration

The currently deployed version uses:

- AI Provider: Fallback
- Speech-to-Text Provider: Demo
- Firebase: Disabled

Some integrations are configurable but are not necessarily active in the deployed version. Configure the required credentials and providers to enable supported integrations.

## 🔒 Security Notes

- Never upload `.env` files or private API credentials to GitHub.
- Keep authentication credentials and service-account keys private.
- Use environment variables for deployment configuration.
- Treat automated threat assessments as assistance, not a guarantee that content is safe or malicious.

## 🎯 Project Goals

- Improve awareness of digital scams and suspicious activities.
- Make security analysis easier to access.
- Provide an understandable interface for threat assessment.
- Build a modular platform that can support additional detection integrations.

## 📌 Future Improvements

- Expand detection capabilities.
- Improve analysis accuracy.
- Integrate additional AI providers.
- Enhance reporting and visualization.
- Extend mobile application functionality.
- Add more security analysis features.

## 👨‍💻 Author

**Deepraj S. Parmar**

GitHub: https://github.com/deepraj-s-parmar

---

⭐ If you find this project interesting, consider giving the repository a star!

**ThreatGuard AI — Stay Alert. Stay Protected.**