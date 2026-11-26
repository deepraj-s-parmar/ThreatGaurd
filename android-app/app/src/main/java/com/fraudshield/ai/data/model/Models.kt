package com.fraudshield.ai.data.model

import com.google.gson.annotations.SerializedName

/**
 * FraudShield AI — Data Models
 * Mirrors the backend Pydantic schemas.
 */

data class AnalyzeMessageRequest(
    val message: String,
    @SerializedName("input_type") val inputType: String = "text",
    @SerializedName("source_info") val sourceInfo: String? = null
)

data class AnalyzeTranscriptRequest(
    val transcript: String,
    @SerializedName("source_info") val sourceInfo: String? = null
)

data class RedFlag(
    val indicator: String = "",
    val description: String = "",
    val severity: String = "high"
)

data class ScoreBreakdown(
    val signal: String = "",
    val points: Int = 0,
    val description: String = ""
)

data class AnalysisResult(
    val id: String = "",
    @SerializedName("scam_type") val scamType: String = "",
    @SerializedName("risk_score") val riskScore: Int = 0,
    @SerializedName("risk_level") val riskLevel: String = "LOW",
    val confidence: Float = 0f,
    val summary: String = "",
    val explanation: String = "",
    @SerializedName("red_flags") val redFlags: List<RedFlag> = emptyList(),
    @SerializedName("recommended_actions") val recommendedActions: List<String> = emptyList(),
    @SerializedName("detected_entities") val detectedEntities: List<String> = emptyList(),
    @SerializedName("score_breakdown") val scoreBreakdown: List<ScoreBreakdown> = emptyList(),
    @SerializedName("input_type") val inputType: String = "text",
    @SerializedName("input_text") val inputText: String = "",
    @SerializedName("ai_provider") val aiProvider: String = "fallback",
    @SerializedName("created_at") val createdAt: String = "",
    val disclaimer: String = "AI-generated risk assessment. Verify critical decisions through official channels."
)

data class ScanRecord(
    val id: String = "",
    @SerializedName("scam_type") val scamType: String = "",
    @SerializedName("risk_score") val riskScore: Int = 0,
    @SerializedName("risk_level") val riskLevel: String = "LOW",
    @SerializedName("input_type") val inputType: String = "text",
    val summary: String = "",
    @SerializedName("created_at") val createdAt: String = ""
)

data class DemoScenario(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    @SerializedName("input_text") val inputText: String = "",
    @SerializedName("input_type") val inputType: String = "text",
    @SerializedName("expected_scam_type") val expectedScamType: String = "",
    @SerializedName("expected_risk_level") val expectedRiskLevel: String = ""
)

data class ReportRequest(
    @SerializedName("scan_id") val scanId: String,
    @SerializedName("scam_type") val scamType: String,
    @SerializedName("risk_score") val riskScore: Int,
    val description: String? = null,
    @SerializedName("communication_source") val communicationSource: String? = null
)

data class ReportResponse(
    val id: String = "",
    val status: String = "",
    val message: String = "",
    @SerializedName("scan_id") val scanId: String = "",
    @SerializedName("created_at") val createdAt: String = ""
)

data class StatsResponse(
    @SerializedName("total_scans") val totalScans: Int = 0,
    @SerializedName("threats_detected") val threatsDetected: Int = 0,
    @SerializedName("high_risk_alerts") val highRiskAlerts: Int = 0,
    @SerializedName("critical_alerts") val criticalAlerts: Int = 0,
    @SerializedName("protection_status") val protectionStatus: String = "active"
)
