# FraudShield AI — Demo Scenarios

Preset scenarios for reliable demonstration during judging.
Each scenario tests a specific scam type and expected result.

---

## 1. Digital Arrest Scam (CRITICAL)

**Input:**
```
This is CBI. Your bank account is linked to a money laundering case under investigation. 
You are under digital arrest. Stay on this video call and do not tell anyone. 
Transfer ₹2,00,000 immediately to the secure government account to avoid arrest. 
Your Aadhaar is linked to illegal activity. If you disconnect, a warrant will be issued.
```

**Expected Result:**
- Scam Type: Digital Arrest Scam
- Risk Score: 85-100 (CRITICAL)
- Red Flags: Government impersonation, arrest threat, urgent payment, coercion

---

## 2. Fake Bank Support (CRITICAL)

**Input:**
```
Dear Customer, this is SBI Customer Care. Your account will be blocked within 24 hours 
due to incomplete KYC verification. Please share your Aadhaar number and OTP sent to 
your registered mobile number immediately to avoid account suspension.
```

**Expected Result:**
- Scam Type: Fake Bank Support
- Risk Score: 70-90 (HIGH/CRITICAL)
- Red Flags: Bank impersonation, KYC urgency, OTP request

---

## 3. Investment Scam (HIGH)

**Input:**
```
🔥 EXCLUSIVE OFFER! Join our premium stock trading group on Telegram. 
We guarantee 200% returns in just 30 days! Invest ₹50,000 today and earn ₹1,50,000. 
Zero risk, 100% profit guaranteed. Limited slots available.
```

**Expected Result:**
- Scam Type: Investment Scam
- Risk Score: 55-80 (MEDIUM/HIGH)
- Red Flags: Guaranteed returns, zero risk claims

---

## 4. Prepaid Task / Job Scam (HIGH)

**Input:**
```
Hi! Simple work-from-home opportunity. Complete online rating tasks and 
earn ₹500 per task. Daily income up to ₹5000! Pay registration fee of ₹999 
to join our Telegram group.
```

**Expected Result:**
- Scam Type: Prepaid Task / Job Scam
- Risk Score: 40-70 (MEDIUM/HIGH)
- Red Flags: Registration fee, task-based earning, Telegram group

---

## 5. Phishing SMS (HIGH)

**Input:**
```
URGENT: Your SBI account will be blocked today due to expired KYC. 
Verify your KYC immediately: http://sbi-kyc-update.fake-link.com/verify
```

**Expected Result:**
- Scam Type: Phishing
- Risk Score: 50-80 (MEDIUM/HIGH)
- Red Flags: Suspicious link, bank impersonation, urgency

---

## 6. Safe / Legitimate Message (LOW)

**Input:**
```
Hey! Are we still on for dinner at 7 PM tonight? I was thinking we could try 
that new restaurant near Connaught Place. Let me know if the timing works!
```

**Expected Result:**
- Scam Type: Legitimate
- Risk Score: 0-10 (LOW)
- Red Flags: None

This scenario is important to demonstrate that FraudShield AI does NOT flag every message — 
it correctly identifies legitimate communications.
