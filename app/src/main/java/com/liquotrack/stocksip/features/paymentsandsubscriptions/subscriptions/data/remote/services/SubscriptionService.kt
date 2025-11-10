package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.services

import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models.AccountSubscriptionDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models.ConfirmSubscriptionDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models.InitialSubscriptionDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models.SubscriptionResponseDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models.UpgradeSubscriptionDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models.UpgradeSubscriptionResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    /**
     * Confirms a subscription based on the provided details.
     * @param body The confirmation details for the subscription.
     * @return A [Response] containing a [String] message regarding the confirmation status.
     */
    @POST("subscriptions")
    suspend fun confirmSubscription(@Body body: ConfirmSubscriptionDto) : Response<ConfirmSubscriptionDto>

    /**
     * Fetches the current subscription details for a specific account ID.
     * @param accountId The ID of the account whose subscription details are to be fetched.
     * @return A [Response] containing [AccountSubscriptionDto] with the subscription information.
     */
    @GET("accounts/{accountId}/subscriptions")
    suspend fun fetchSubscriptionByAccountId(@Path("accountId") accountId: String) : Response<AccountSubscriptionDto>

    /**
     * Upgrades the subscription plan for a given account and subscription ID.
     * @param accountId The ID of the account.
     * @param subscriptionId The ID of the subscription to be upgraded.
     * @return A [Response] containing [UpgradeSubscriptionResponseDto] with upgrade details.
     */
    @PUT("accounts/{accountId}/subscriptions/{subscriptionId}")
    suspend fun upgradeSubscription(
        @Path("accountId") accountId: String,
        @Path("subscriptionId") subscriptionId: String,
        @Body newPlan: UpgradeSubscriptionDto
    ): Response<UpgradeSubscriptionResponseDto>

}
