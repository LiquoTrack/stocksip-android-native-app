package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.repositories

import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.services.SubscriptionService
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.models.Subscription
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.repositories.SubscriptionRepository
import javax.inject.Inject

class SubscriptionRepositoryImpl @Inject constructor(private val service: SubscriptionService) : SubscriptionRepository {

    override suspend fun createInitialSubscription(
        accountId: String,
        selectedPlanId: String
    ): Subscription {
        TODO("Not yet implemented")
    }
}