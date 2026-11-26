package com.fraudshield.ai.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.fraudshield.ai.data.model.*
import com.fraudshield.ai.ui.theme.*

/**
 * FraudShield AI — Main Application
 * Jetpack Compose app with bottom navigation and all screens.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FraudShieldApp(vm: FraudShieldViewModel = viewModel()) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = Navy950,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("FraudShield AI", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = White)
                        Text("Real-Time Scam Protection", fontSize = 10.sp, color = Gray400,
                            letterSpacing = 1.sp)
                    }
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 12.dp)) {
                        Box(modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(GreenSafe))
                        Spacer(Modifier.width(6.dp))
                        Text("Active", fontSize = 11.sp, color = GreenSafe, fontWeight = FontWeight.Medium)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Navy900.copy(alpha = 0.95f)
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Navy900, contentColor = Gray400) {
                val items = listOf(
                    Triple("home", "Home", Icons.Filled.Home),
                    Triple("analyze", "Analyze", Icons.Filled.Shield),
                    Triple("history", "History", Icons.Filled.History),
                    Triple("safety", "Safety", Icons.Outlined.HealthAndSafety),
                    Triple("settings", "Settings", Icons.Filled.Settings),
                )
                items.forEach { (route, label, icon) ->
                    NavigationBarItem(
                        selected = currentRoute == route,
                        onClick = {
                            if (currentRoute != route) {
                                navController.navigate(route) {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Blue400,
                            selectedTextColor = Blue400,
                            unselectedIconColor = Gray500,
                            unselectedTextColor = Gray500,
                            indicatorColor = Blue500.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        NavHost(navController, startDestination = "home", modifier = Modifier.padding(padding)) {
            composable("home") { HomeScreen(vm, navController) }
            composable("analyze") { AnalyzeScreen(vm, navController) }
            composable("history") { HistoryScreen(vm, navController) }
            composable("safety") { SafetyTipsScreen() }
            composable("settings") { SettingsScreen() }
            composable("result") { ResultScreen(vm, navController) }
            composable("analyzing") { AnalyzingScreen(vm, navController) }
            composable("demo") { DemoScreen(vm, navController) }
            composable("report") { ReportScreen(vm) }
            composable("about") { AboutScreen() }
        }
    }

    // Show error snackbar
    vm.errorMessage?.let { error ->
        LaunchedEffect(error) {
            kotlinx.coroutines.delay(4000)
            vm.clearError()
        }
    }
}

// ═══════════════════════════════════════════════
// HOME SCREEN
// ═══════════════════════════════════════════════

@Composable
fun HomeScreen(vm: FraudShieldViewModel, navController: NavHostController) {
    LaunchedEffect(Unit) { vm.loadStats() }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {
        // Protection Status
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Filled.Shield, "Shield", tint = Blue400,
                        modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(12.dp))
                    Text("Protection Active", color = GreenSafe, fontWeight = FontWeight.Bold,
                        fontSize = 18.sp)
                    Text("FraudShield AI is ready to analyze", color = Gray400, fontSize = 13.sp)
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = { navController.navigate("analyze") },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Blue500)
                    ) {
                        Icon(Icons.Filled.Shield, "Analyze", modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Analyze Now", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }
                }
            }
        }

        // Stats Grid
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Scans", "${vm.stats.totalScans}", Blue400, Modifier.weight(1f))
                StatCard("Threats", "${vm.stats.threatsDetected}", OrangeHigh, Modifier.weight(1f))
                StatCard("Critical", "${vm.stats.criticalAlerts}", RedCritical, Modifier.weight(1f))
            }
        }

        // Quick Actions
        item {
            Text("Quick Actions", color = White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ActionCard("Scan Message", "Analyze SMS or WhatsApp", Icons.Filled.Message, Blue400) {
                    navController.navigate("analyze")
                }
                ActionCard("Analyze Call Content", "Transcript or audio analysis", Icons.Filled.Phone, Indigo400) {
                    navController.navigate("analyze")
                }
                ActionCard("Demo Scenarios", "Test with preset scam examples", Icons.Filled.PlayArrow, GreenSafe) {
                    navController.navigate("demo")
                }
                ActionCard("Report Scam", "Report to cybercrime helpline", Icons.Filled.Report, RedCritical) {
                    navController.navigate("report")
                }
            }
        }

        // Pipeline
        item {
            Text("How It Works", color = White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp))
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PipelineStep("DETECT", "📡")
                    Text("→", color = Gray600, modifier = Modifier.padding(top = 12.dp))
                    PipelineStep("UNDERSTAND", "🧠")
                    Text("→", color = Gray600, modifier = Modifier.padding(top = 12.dp))
                    PipelineStep("CLASSIFY", "📊")
                    Text("→", color = Gray600, modifier = Modifier.padding(top = 12.dp))
                    PipelineStep("PROTECT", "🛡️")
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Navy800),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 28.sp)
            Text(label, color = Gray400, fontSize = 12.sp)
        }
    }
}

@Composable
fun ActionCard(title: String, subtitle: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Navy800),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) { Icon(icon, title, tint = color, modifier = Modifier.size(22.dp)) }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(subtitle, color = Gray500, fontSize = 12.sp)
            }
            Icon(Icons.Filled.ChevronRight, "Go", tint = Gray600)
        }
    }
}

@Composable
fun PipelineStep(label: String, emoji: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(emoji, fontSize = 24.sp)
        Spacer(Modifier.height(4.dp))
        Text(label, color = Gray300, fontSize = 10.sp, fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp)
    }
}

// ═══════════════════════════════════════════════
// ANALYZE SCREEN
// ═══════════════════════════════════════════════

@Composable
fun AnalyzeScreen(vm: FraudShieldViewModel, navController: NavHostController) {
    var messageText by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("SMS", "WhatsApp", "Call Transcript")
    val inputTypes = listOf("sms", "whatsapp", "call_transcript")

    // Navigate when analysis completes
    LaunchedEffect(vm.isAnalyzing) {
        if (vm.isAnalyzing) navController.navigate("analyzing")
    }
    LaunchedEffect(vm.analysisResult) {
        if (vm.analysisResult != null && !vm.isAnalyzing) navController.navigate("result")
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {
        item {
            Text("Analyze Suspicious Communication", color = White,
                fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text("Paste any suspicious message for AI-powered scam analysis.",
                color = Gray400, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
        }

        // Input Type Tabs
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(Modifier.fillMaxWidth().padding(4.dp)) {
                    tabs.forEachIndexed { index, tab ->
                        val isSelected = selectedTab == index
                        Surface(
                            onClick = { selectedTab = index },
                            modifier = Modifier.weight(1f),
                            color = if (isSelected) Navy700 else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(tab, textAlign = TextAlign.Center,
                                color = if (isSelected) Blue400 else Gray500,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(vertical = 10.dp))
                        }
                    }
                }
            }
        }

        // Text Input
        item {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.fillMaxWidth().height(200.dp),
                placeholder = { Text("Paste the suspicious message here...", color = Gray600) },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue500,
                    unfocusedBorderColor = Navy600,
                    focusedContainerColor = Navy900,
                    unfocusedContainerColor = Navy900,
                    cursorColor = Blue400,
                    focusedTextColor = White,
                    unfocusedTextColor = Gray200,
                )
            )
        }

        // Analyze Button
        item {
            Button(
                onClick = {
                    vm.clearResult()
                    if (selectedTab == 2) vm.analyzeTranscript(messageText)
                    else vm.analyzeMessage(messageText, inputTypes[selectedTab])
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue500),
                enabled = messageText.isNotBlank()
            ) {
                Icon(Icons.Filled.Shield, "Analyze", modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Analyze with FraudShield AI", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
        }

        // Error
        vm.errorMessage?.let { error ->
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF450A0A)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(error, color = Red400, fontSize = 13.sp,
                        modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════
// ANALYZING SCREEN
// ═══════════════════════════════════════════════

@Composable
fun AnalyzingScreen(vm: FraudShieldViewModel, navController: NavHostController) {
    val steps = listOf(
        "Detecting input patterns...",
        "Understanding context & intent...",
        "Classifying scam type...",
        "Calculating risk score...",
        "Generating protection guidance..."
    )

    LaunchedEffect(vm.analysisResult) {
        if (vm.analysisResult != null) {
            navController.navigate("result") {
                popUpTo("analyze") { inclusive = false }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Filled.Shield, "Analyzing", tint = Blue400, modifier = Modifier.size(64.dp))
        Spacer(Modifier.height(24.dp))
        Text("Analyzing Communication", color = White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(Modifier.height(24.dp))

        steps.forEachIndexed { index, step ->
            val stepNum = index + 1
            val isComplete = vm.analysisStep > stepNum
            val isActive = vm.analysisStep == stepNum

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                    .background(
                        when {
                            isComplete -> GreenSafe.copy(alpha = 0.08f)
                            isActive -> Blue500.copy(alpha = 0.08f)
                            else -> Color.Transparent
                        },
                        RoundedCornerShape(8.dp)
                    ).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    when {
                        isComplete -> "✅"
                        isActive -> "🔄"
                        else -> "⏳"
                    },
                    fontSize = 16.sp
                )
                Spacer(Modifier.width(12.dp))
                Text(step,
                    color = when {
                        isComplete -> GreenSafe
                        isActive -> Blue400
                        else -> Gray600
                    },
                    fontSize = 14.sp
                )
            }
        }

        if (vm.isAnalyzing) {
            Spacer(Modifier.height(24.dp))
            CircularProgressIndicator(color = Blue400, strokeWidth = 3.dp)
        }
    }
}

// ═══════════════════════════════════════════════
// RESULT SCREEN
// ═══════════════════════════════════════════════

@Composable
fun ResultScreen(vm: FraudShieldViewModel, navController: NavHostController) {
    val result = vm.analysisResult ?: vm.selectedScan ?: return

    val riskColor = when (result.riskLevel) {
        "CRITICAL" -> RedCritical
        "HIGH" -> OrangeHigh
        "MEDIUM" -> YellowMedium
        else -> GreenSafe
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Risk Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = riskColor.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("⚠ ${result.riskLevel} RISK", color = riskColor,
                        fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 2.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("${result.riskScore}", color = riskColor,
                        fontWeight = FontWeight.ExtraBold, fontSize = 56.sp)
                    Text("/100", color = Gray500, fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(result.scamType, color = White,
                        fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(4.dp))
                    Text("Confidence: ${(result.confidence * 100).toInt()}%",
                        color = Gray500, fontSize = 12.sp)
                }
            }
        }

        // Red Flags
        if (result.redFlags.isNotEmpty()) {
            item {
                SectionCard("🚩 Red Flags Detected") {
                    result.redFlags.forEach { flag ->
                        Row(
                            modifier = Modifier.padding(vertical = 6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("⚠", fontSize = 14.sp, modifier = Modifier.padding(end = 10.dp, top = 2.dp))
                            Column {
                                Text(flag.indicator, color = Red400, fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp)
                                Text(flag.description, color = Gray400, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Explanation
        if (result.explanation.isNotBlank()) {
            item {
                SectionCard("💡 Why This Was Flagged") {
                    Text(result.explanation, color = Gray200, fontSize = 14.sp, lineHeight = 22.sp)
                }
            }
        }

        // Recommendations
        if (result.recommendedActions.isNotEmpty()) {
            item {
                SectionCard("🛡️ Safety Recommendations") {
                    result.recommendedActions.forEach { action ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("✓", color = GreenSafe, fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 10.dp))
                            Text(action, color = Gray200, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Score Breakdown
        if (result.scoreBreakdown.isNotEmpty()) {
            item {
                SectionCard("📊 Risk Score Breakdown") {
                    result.scoreBreakdown.forEach { b ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                .background(Blue500.copy(alpha = 0.05f), RoundedCornerShape(6.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(b.signal, color = Gray200, fontSize = 13.sp,
                                modifier = Modifier.weight(1f))
                            Text("+${b.points}", color = Blue400, fontWeight = FontWeight.Bold,
                                fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Actions
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (result.riskScore >= 30) {
                    Button(
                        onClick = { vm.reportIncident(result); navController.navigate("report") },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RedCritical)
                    ) {
                        Text("🚨 Report Incident", fontWeight = FontWeight.SemiBold)
                    }
                }
                OutlinedButton(
                    onClick = { vm.clearResult(); navController.navigate("analyze") },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Navy600)
                ) {
                    Text("🔍 Analyze Another", color = Gray300)
                }
            }
        }

        // Disclaimer
        item {
            Text(result.disclaimer, color = Gray600, fontSize = 11.sp,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
        }
    }
}

@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Navy800),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, color = Gray400, fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp, letterSpacing = 0.5.sp)
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

// ═══════════════════════════════════════════════
// HISTORY SCREEN
// ═══════════════════════════════════════════════

@Composable
fun HistoryScreen(vm: FraudShieldViewModel, navController: NavHostController) {
    LaunchedEffect(Unit) { vm.loadHistory() }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Scan History", color = White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(Modifier.height(8.dp))
        }

        if (vm.scanHistory.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Outlined.Shield, "No scans", tint = Gray600,
                        modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(12.dp))
                    Text("No scans yet", color = Gray500, fontSize = 15.sp)
                    Text("Analyze a message to get started.", color = Gray600, fontSize = 13.sp)
                }
            }
        }

        items(vm.scanHistory) { scan ->
            val riskColor = when (scan.riskLevel) {
                "CRITICAL" -> RedCritical; "HIGH" -> OrangeHigh
                "MEDIUM" -> YellowMedium; else -> GreenSafe
            }

            Card(
                onClick = { vm.loadScanDetail(scan.id); navController.navigate("result") },
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(48.dp)
                            .clip(CircleShape)
                            .background(riskColor.copy(alpha = 0.15f))
                            .border(2.dp, riskColor.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${scan.riskScore}", color = riskColor,
                            fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(scan.scamType, color = White, fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp)
                        Text(scan.summary, color = Gray500, fontSize = 12.sp,
                            maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Text(scan.riskLevel, color = riskColor, fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp)
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════
// DEMO SCREEN
// ═══════════════════════════════════════════════

@Composable
fun DemoScreen(vm: FraudShieldViewModel, navController: NavHostController) {
    LaunchedEffect(vm.analysisResult) {
        if (vm.analysisResult != null && !vm.isAnalyzing) navController.navigate("result")
    }
    LaunchedEffect(vm.isAnalyzing) {
        if (vm.isAnalyzing) navController.navigate("analyzing")
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Demo Scenarios", color = White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text("Test with preset scam scenarios.", color = Gray400, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
        }

        items(vm.demoScenarios) { scenario ->
            val riskColor = when (scenario.expectedRiskLevel) {
                "CRITICAL" -> RedCritical; "HIGH" -> OrangeHigh
                "MEDIUM" -> YellowMedium; else -> GreenSafe
            }

            Card(
                onClick = { vm.clearResult(); vm.runDemoScenario(scenario) },
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(scenario.name, color = White, fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp, modifier = Modifier.weight(1f))
                        Surface(
                            color = riskColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(100.dp)
                        ) {
                            Text(scenario.expectedRiskLevel, color = riskColor,
                                fontWeight = FontWeight.SemiBold, fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(scenario.description, color = Gray400, fontSize = 13.sp)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        scenario.inputText.take(100) + if (scenario.inputText.length > 100) "..." else "",
                        color = Gray600, fontSize = 11.sp,
                        modifier = Modifier.background(Navy900, RoundedCornerShape(6.dp)).padding(8.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { vm.clearResult(); vm.runDemoScenario(scenario) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Blue500.copy(alpha = 0.3f))
                    ) {
                        Text("▶ Run Scenario", color = Blue400, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════
// REPORT SCREEN
// ═══════════════════════════════════════════════

@Composable
fun ReportScreen(vm: FraudShieldViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Report Incident", color = White, fontWeight = FontWeight.Bold, fontSize = 22.sp)

        Card(
            colors = CardDefaults.cardColors(containerColor = RedCritical.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("📞", fontSize = 36.sp)
                Spacer(Modifier.height(8.dp))
                Text("National Cyber Crime Helpline", color = White,
                    fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("1930", color = RedCritical, fontWeight = FontWeight.ExtraBold,
                    fontSize = 36.sp)
                Spacer(Modifier.height(8.dp))
                Text("Call this number immediately if you have been a victim of cyber fraud.",
                    color = Gray300, fontSize = 13.sp, textAlign = TextAlign.Center)
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Navy800),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Online Reporting", color = White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Text("File a complaint at:", color = Gray400, fontSize = 13.sp)
                Text("cybercrime.gov.in", color = Blue400, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Spacer(Modifier.height(12.dp))
                Text("How to report:", color = Gray400, fontSize = 13.sp)
                listOf(
                    "1. Visit cybercrime.gov.in",
                    "2. Click 'File a Complaint'",
                    "3. Register with your mobile number",
                    "4. Select the fraud category",
                    "5. Provide details and evidence",
                    "6. Submit the complaint"
                ).forEach { step ->
                    Text(step, color = Gray300, fontSize = 13.sp, modifier = Modifier.padding(vertical = 2.dp))
                }
            }
        }

        vm.reportStatus?.let { status ->
            Card(
                colors = CardDefaults.cardColors(containerColor = GreenSafe.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(status, color = GreenSafe, fontSize = 13.sp, modifier = Modifier.padding(16.dp))
            }
        }

        Text(
            "FraudShield AI provides reporting guidance. It does not have direct integration with government systems.",
            color = Gray600, fontSize = 11.sp, textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ═══════════════════════════════════════════════
// SAFETY TIPS SCREEN
// ═══════════════════════════════════════════════

@Composable
fun SafetyTipsScreen() {
    data class SafetyTip(val emoji: String, val title: String, val whatItLooksLike: String,
                         val redFlags: List<String>, val doNot: List<String>, val doThis: List<String>)

    val tips = listOf(
        SafetyTip("🚨", "Digital Arrest Scams",
            "Caller claims to be CBI/police, says you're under investigation.",
            listOf("Threats of arrest", "Video call demands", "Immediate money transfer"),
            listOf("Transfer money", "Stay on the call", "Share documents"),
            listOf("Hang up", "Call 1930", "Report at cybercrime.gov.in")),
        SafetyTip("💳", "UPI / Payment Scams",
            "Fake collect requests or QR codes for 'receiving' money.",
            listOf("Collect requests from strangers", "QR codes to receive money", "UPI PIN requests"),
            listOf("Enter UPI PIN to receive money", "Scan unknown QR codes"),
            listOf("Reject unknown requests", "Verify via bank app")),
        SafetyTip("🏦", "Fake Bank Support",
            "SMS/call claiming account will be blocked unless KYC is verified.",
            listOf("Account blocking threats", "KYC urgency", "OTP/card requests"),
            listOf("Share OTP or CVV", "Click links in messages"),
            listOf("Call bank's official number", "Visit bank branch")),
        SafetyTip("🔗", "Phishing",
            "Links to fake websites that look like real banks/services.",
            listOf("Suspicious URLs", "HTTP instead of HTTPS", "Urgency to click"),
            listOf("Click links in suspicious messages", "Enter credentials"),
            listOf("Verify URL directly", "Enable 2FA")),
        SafetyTip("📈", "Investment Scams",
            "Guaranteed high returns with zero risk.",
            listOf("Guaranteed returns", "Zero risk claims", "Pressure to invest now"),
            listOf("Invest via unsolicited messages", "Join unknown trading groups"),
            listOf("Verify with SEBI", "Consult a financial advisor")),
        SafetyTip("💼", "Job / Task Scams",
            "Work-from-home with simple tasks requiring registration fee.",
            listOf("Registration fee", "Unrealistic pay", "Telegram group recruitment"),
            listOf("Pay for job opportunities", "Share banking details"),
            listOf("Verify on company websites", "Report to cybercrime.gov.in")),
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Safety Tips", color = White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text("Protect yourself from common scam types.", color = Gray400, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
        }

        items(tips.size) { index ->
            val tip = tips[index]
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("${tip.emoji} ${tip.title}", color = White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(tip.whatItLooksLike, color = Gray300, fontSize = 13.sp)
                    Spacer(Modifier.height(10.dp))
                    Text("RED FLAGS", color = Red400, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                    tip.redFlags.forEach { Text("• $it", color = Gray400, fontSize = 12.sp) }
                    Spacer(Modifier.height(6.dp))
                    Text("DO NOT", color = OrangeHigh, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                    tip.doNot.forEach { Text("✗ $it", color = Gray400, fontSize = 12.sp) }
                    Spacer(Modifier.height(6.dp))
                    Text("DO THIS", color = GreenSafe, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                    tip.doThis.forEach { Text("✓ $it", color = Gray400, fontSize = 12.sp) }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════
// SETTINGS SCREEN
// ═══════════════════════════════════════════════

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings", color = White, fontWeight = FontWeight.Bold, fontSize = 22.sp)

        Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(14.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("API Configuration", color = White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Text("Backend URL: http://10.0.2.2:8000", color = Gray400, fontSize = 13.sp)
                Text("For physical devices, use your computer's IP address.", color = Gray500, fontSize = 12.sp)
            }
        }

        Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(14.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("About FraudShield AI", color = White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Text("Version: 1.0.0", color = Gray400, fontSize = 13.sp)
                Text("AI Provider: Configurable (Gemini / OpenAI / Fallback)", color = Gray400, fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                Text("Built for Maxim Code AI Software Innovation Competition", color = Gray500, fontSize = 12.sp)
            }
        }

        Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(14.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("Privacy", color = White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Text("FraudShield AI analyzes communications for scam indicators. " +
                     "We do not store personal communication data beyond scan results. " +
                     "Analysis results are stored locally and optionally in Firebase Firestore.",
                    color = Gray400, fontSize = 13.sp)
            }
        }

        Text("AI-generated risk assessment. Verify critical decisions through official channels.",
            color = Gray600, fontSize = 11.sp, textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth())
    }
}

// ═══════════════════════════════════════════════
// ABOUT SCREEN
// ═══════════════════════════════════════════════

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(20.dp))
        Icon(Icons.Filled.Shield, "Shield", tint = Blue400, modifier = Modifier.size(64.dp))
        Text("FraudShield AI", color = White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Text("Real-Time AI Protection Against Digital Scams", color = Gray400,
            fontSize = 14.sp, textAlign = TextAlign.Center)
        Text("DETECT • UNDERSTAND • PROTECT", color = Blue400,
            fontWeight = FontWeight.SemiBold, fontSize = 12.sp, letterSpacing = 2.sp)
        Spacer(Modifier.height(8.dp))
        Text("Version 1.0.0", color = Gray500, fontSize = 13.sp)
        Text("Maxim Code AI Software Innovation Competition", color = Gray500, fontSize = 13.sp)
        Spacer(Modifier.height(16.dp))
        Text("AI-generated risk assessment. Verify critical decisions through official channels.",
            color = Gray600, fontSize = 11.sp, textAlign = TextAlign.Center)
    }
}
