package com.liquotrack.stocksip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.liquotrack.stocksip.core.navigation.AppNavigation
import com.liquotrack.stocksip.core.navigation.Route
import com.liquotrack.stocksip.shared.ui.theme.StockSipTheme

class PaymentActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val status = intent?.data?.lastPathSegment

        setContent {
            StockSipTheme {
                val startDestination = when (status) {
                    "congrats" -> Route.Congrats.route
                    "failure" -> Route.Failure.route
                    "pending" -> Route.Pending.route
                    else -> Route.Main.route
                }

                AppNavigation(startDestination = startDestination)
            }
        }
    }
}