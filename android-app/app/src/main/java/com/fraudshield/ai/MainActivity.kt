package com.fraudshield.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fraudshield.ai.ui.theme.FraudShieldTheme
import com.fraudshield.ai.ui.FraudShieldApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FraudShieldTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FraudShieldApp()
                }
            }
        }
    }
}
