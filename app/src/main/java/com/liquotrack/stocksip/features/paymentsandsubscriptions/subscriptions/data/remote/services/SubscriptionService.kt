package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.services

import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models.InitialSubscriptionDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models.SubscriptionResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Service interface for managing subscriptions via remote API calls.
 */
interface SubscriptionService {

    /**
     * Creates an initial subscription for a given account.
     * @param accountId The ID of the account for which the subscription is to be created.
     * @param body The details of the initial subscription.
     * @return A [Response] containing [SubscriptionResponseDto] with subscription details.
     */
    @POST("accounts/{accountId}/subscriptions")
    suspend fun createInitialSubscription(
        @Path("accountId") accountId: String, @Body body: InitialSubscriptionDto): Response<SubscriptionResponseDto>
}