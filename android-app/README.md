# FraudShield AI — Android App

## Overview
FraudShield AI Android application built with Kotlin, Jetpack Compose, and Material 3.

## Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** MVVM
- **Networking:** Retrofit + OkHttp
- **Storage:** Room Database (local) + Firebase Firestore (cloud)
- **Navigation:** Jetpack Navigation Compose

## Setup

1. Open the `android-app` folder in Android Studio
2. Sync Gradle dependencies
3. Update `local.properties` with your SDK path
4. Update the API base URL in `Constants.kt`
5. Build and run on an emulator or device

## Screens

| Screen | Description |
|--------|-------------|
| Splash | Animated FraudShield AI logo |
| Home | Dashboard with protection status and stats |
| Message Analysis | Text input for SMS/WhatsApp messages |
| Call Analysis | Transcript/audio input |
| Analyzing | 4-stage pipeline animation |
| Result | Risk score gauge, scam type, red flags, explanation |
| Scan History | List of previous scans |
| Report | Incident reporting with 1930 guidance |
| Safety Tips | Categorized safety information |
| Settings | API URL, demo mode toggle |
| About | Product info and disclaimer |
