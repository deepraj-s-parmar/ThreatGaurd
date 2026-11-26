package com.fraudshield.ai.data.api

import com.fraudshield.ai.data.model.*
import retrofit2.http.*

/**
 * FraudShield AI — Retrofit API Service
 * Communicates with the FastAPI backend.
 */
interface FraudShieldApi {

    @POST("api/analyze/message")
    suspend fun analyzeMessage(@Body request: AnalyzeMessageRequest): AnalysisResult

    @POST("api/analyze/transcript")
    suspend fun analyzeTranscript(@Body request: AnalyzeTranscriptRequest): AnalysisResult

    @GET("api/scans")
    suspend fun getScans(): List<ScanRecord>

    @GET("api/scans/{id}")
    suspend fun getScan(@Path("id") scanId: String): AnalysisResult

    @POST("api/reports")
    suspend fun submitReport(@Body report: ReportRequest): ReportResponse

    @GET("api/stats")
    suspend fun getStats(): StatsResponse

    @GET("api/demo/scenarios")
    suspend fun getDemoScenarios(): List<DemoScenario>

    @POST("api/demo/run/{id}")
    suspend fun runDemoScenario(@Path("id") scenarioId: String): AnalysisResult

    @GET("health")
    suspend fun healthCheck(): Map<String, Any>
}
