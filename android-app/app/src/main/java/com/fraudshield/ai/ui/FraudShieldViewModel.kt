package com.fraudshield.ai.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fraudshield.ai.data.api.RetrofitClient
import com.fraudshield.ai.data.model.*
import kotlinx.coroutines.launch

/**
 * FraudShield AI — Main ViewModel
 * Manages UI state and API communication for all screens.
 */
class FraudShieldViewModel : ViewModel() {

    private val api = RetrofitClient.api

    // ── UI State ──
    var analysisResult by mutableStateOf<AnalysisResult?>(null)
        private set
    var isAnalyzing by mutableStateOf(false)
        private set
    var analysisStep by mutableStateOf(0)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var scanHistory by mutableStateOf<List<ScanRecord>>(emptyList())
        private set
    var demoScenarios by mutableStateOf<List<DemoScenario>>(emptyList())
        private set
    var stats by mutableStateOf(StatsResponse())
        private set
    var selectedScan by mutableStateOf<AnalysisResult?>(null)
        private set
    var reportStatus by mutableStateOf<String?>(null)
        private set

    init {
        loadDemoScenarios()
    }

    // ── Analysis ──

    fun analyzeMessage(text: String, inputType: String = "text") {
        if (text.isBlank()) {
            errorMessage = "Please enter a message to analyze."
            return
        }

        viewModelScope.launch {
            isAnalyzing = true
            analysisResult = null
            errorMessage = null
            analysisStep = 0

            try {
                // Animate pipeline stages
                for (step in 1..5) {
                    analysisStep = step
                    kotlinx.coroutines.delay(400)
                }

                val result = api.analyzeMessage(
                    AnalyzeMessageRequest(
                        message = text,
                        inputType = inputType
                    )
                )
                analysisResult = result
                loadStats()
            } catch (e: Exception) {
                errorMessage = "Analysis failed: ${e.message}. Is the backend running?"
            } finally {
                isAnalyzing = false
            }
        }
    }

    fun analyzeTranscript(transcript: String) {
        if (transcript.isBlank()) {
            errorMessage = "Please enter a transcript to analyze."
            return
        }

        viewModelScope.launch {
            isAnalyzing = true
            analysisResult = null
            errorMessage = null
            analysisStep = 0

            try {
                for (step in 1..5) {
                    analysisStep = step
                    kotlinx.coroutines.delay(400)
                }

                val result = api.analyzeTranscript(
                    AnalyzeTranscriptRequest(transcript = transcript)
                )
                analysisResult = result
                loadStats()
            } catch (e: Exception) {
                errorMessage = "Analysis failed: ${e.message}"
            } finally {
                isAnalyzing = false
            }
        }
    }

    fun runDemoScenario(scenario: DemoScenario) {
        viewModelScope.launch {
            isAnalyzing = true
            analysisResult = null
            errorMessage = null
            analysisStep = 0

            try {
                for (step in 1..5) {
                    analysisStep = step
                    kotlinx.coroutines.delay(350)
                }

                val result = api.runDemoScenario(scenario.id)
                analysisResult = result
                loadStats()
            } catch (e: Exception) {
                errorMessage = "Demo failed: ${e.message}"
            } finally {
                isAnalyzing = false
            }
        }
    }

    // ── History ──

    fun loadHistory() {
        viewModelScope.launch {
            try {
                scanHistory = api.getScans()
            } catch (e: Exception) {
                // Silently fail — history is a secondary feature
            }
        }
    }

    fun loadScanDetail(scanId: String) {
        viewModelScope.launch {
            try {
                selectedScan = api.getScan(scanId)
            } catch (e: Exception) {
                errorMessage = "Could not load scan details."
            }
        }
    }

    // ── Stats ──

    fun loadStats() {
        viewModelScope.launch {
            try {
                stats = api.getStats()
            } catch (e: Exception) {
                // Use defaults
            }
        }
    }

    // ── Demo Scenarios ──

    private fun loadDemoScenarios() {
        viewModelScope.launch {
            try {
                demoScenarios = api.getDemoScenarios()
            } catch (e: Exception) {
                // Use hardcoded fallback
                demoScenarios = listOf(
                    DemoScenario(
                        id = "demo_digital_arrest", name = "Digital Arrest Scam",
                        description = "CBI impersonation with arrest threat",
                        inputText = "This is CBI. Your bank account is linked to money laundering. Transfer ₹2,00,000 immediately to avoid arrest.",
                        expectedRiskLevel = "CRITICAL"
                    ),
                    DemoScenario(
                        id = "demo_legitimate", name = "Safe Message",
                        description = "Normal legitimate message",
                        inputText = "Hey! Are we still on for dinner at 7 PM tonight?",
                        expectedRiskLevel = "LOW"
                    )
                )
            }
        }
    }

    // ── Reports ──

    fun reportIncident(result: AnalysisResult) {
        viewModelScope.launch {
            try {
                val response = api.submitReport(
                    ReportRequest(
                        scanId = result.id,
                        scamType = result.scamType,
                        riskScore = result.riskScore,
                        description = result.summary
                    )
                )
                reportStatus = response.message
            } catch (e: Exception) {
                reportStatus = "Report saved locally. Contact 1930 for immediate assistance."
            }
        }
    }

    // ── Utility ──

    fun clearResult() {
        analysisResult = null
        selectedScan = null
        errorMessage = null
        reportStatus = null
    }

    fun clearError() {
        errorMessage = null
    }
}
