package com.liquotrack.stocksip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.liquotrack.stocksip.core.navigation.AppNavigation
import com.liquotrack.stocksip.core.navigation.Route
import com.liquotrack.stocksip.shared.ui.theme.StockSipTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startDestination = when (intent?.data?.lastPathSegment) {
            "congrats" -> Route.Congrats.route
            "failure" -> Route.Failure.route
            "pending" -> Route.Pending.route
            else -> Route.Login.route
        }

        setContent {
            StockSipTheme {
                AppNavigation(startDestination = startDestination)
            }
        }
    }
}