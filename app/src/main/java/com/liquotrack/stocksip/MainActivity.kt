package com.liquotrack.stocksip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.liquotrack.stocksip.features.adminpanel.presentation.AdminPanel
import com.liquotrack.stocksip.features.profilemanagement.profile.presentation.ProfilePreview
import com.liquotrack.stocksip.shared.ui.theme.StockSipTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StockSipTheme {
                AdminPanel()
            }
        }
    }
}