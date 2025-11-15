package com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.repositories

import android.util.Log
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models.ConfirmSubscriptionDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models.InitialSubscriptionDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.models.UpgradeSubscriptionDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.data.remote.services.SubscriptionService
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.models.AccountSubscription
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.models.Subscription
import com.liquotrack.stocksip.features.paymentsandsubscriptions.subscriptions.domain.repositories.SubscriptionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

/**
 * Implementation of [SubscriptionRepository] that interacts with the [SubscriptionService]
 * to manage subscriptions via remote API calls.
 */
class SubscriptionRepositoryImpl @Inject constructor(private val service: SubscriptionService) : SubscriptionRepository {

    /**
     * Creates an initial subscription for a given account.
     * @param accountId The ID of the account for which the subscription is to be created.
     * @param selectedPlanId The ID of the selected subscription plan.
     * @return A [Subscription] containing the details of the created subscription.
     * @throws Exception if the API call fails or the response is invalid.
     */
    override suspend fun createInitialSubscription(
        accountId: String,
        selectedPlanId: String
    ): Subscription = withContext(Dispatchers.IO) {

        val request = InitialSubscriptionDto(selectedPlanId)
        val response = service.createInitialSubscription(accountId, request)

        if (!response.isSuccessful) {
            val err = try { response.errorBody()?.string()?.ifBlank { null } } catch (_: Exception) { null }
            val composed = buildString {
                append("Failed to create initial subscription: ")
                append(response.code())
                append(" ")
                append(response.message())
                err?.let {
                    append("\n")
                    append(it.take(500))
                }
            }
            throw Exception(composed)
        }

        val body = response.body() ?: throw Exception("Response body is null")

        return@withContext Subscription(
            accountId = accountId,
            planId = selectedPlanId,
            preferenceId = body.preferenceId,
            initPoint = body.initPoint,
            message = body.message ?: "Subscription created successfully"
        )
    }

    /**
     * Confirms a subscription based on the provided preference ID and status.
     * @param preferenceId The ID of the payment preference.
     * @param status The status of the subscription process.
     * @return A [Boolean] indicating whether the confirmation was successful.
     */
    override suspend fun confirmSubscription(
        preferenceId: String,
        status: String
    ): Boolean = withContext(Dispatchers.IO) {
        val body = ConfirmSubscriptionDto(preferenceId, status)
        val response = service.confirmSubscription(body)
        response.isSuccessful
    }

    /**
     * Fetches the current subscription details for a specific account ID.
     * @param accountId The ID of the account whose subscription details are to be fetched.
     * @return An [AccountSubscription] containing the subscription information.
     * @throws Exception if the API call fails or the response is invalid.
     */
    override suspend fun fetchSubscriptionByAccountId(accountId: String): AccountSubscription =
        withContext(Dispatchers.IO) {
            val response = service.fetchSubscriptionByAccountId(accountId)

            try {
                if (response.isSuccessful) {
                    val currentSubscription = response.body()
                        ?: throw Exception("Response body is null")

                    return@withContext AccountSubscription(
                        subscriptionId = currentSubscription.subscriptionId,
                        planId = currentSubscription.planId,
                        status = currentSubscription.status,
                        expirationDate = currentSubscription.expirationDate,
                        planType = currentSubscription.planType,
                        paymentFrequency = currentSubscription.paymentFrequency,
                        maxUsers = currentSubscription.maxUsers,
                        maxProducts = currentSubscription.maxProducts,
                        maxWarehouses = currentSubscription.maxWarehouses,
                    )
                } else {
                    throw Exception("Failed to fetch subscription by account ID: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }

    /**
     * Upgrades the subscription for a given account to a new plan.
     *
     * @param accountId The ID of the account whose subscription is to be upgraded.
     * @param subscriptionId The ID of the subscription to be upgraded.
     * @param newPlanId The ID of the new subscription plan.
     * @return A [Subscription] containing the details of the upgraded subscription.
     */
    override suspend fun upgradeSubscription(
        accountId: String,
        subscriptionId: String,
        newPlanId: String
    ): Subscription = withContext(Dispatchers.IO) {

        try {
            val requestBody = UpgradeSubscriptionDto(newPlanId)
            val response = service.upgradeSubscription(accountId, subscriptionId, requestBody)

            if (!response.isSuccessful) {
                val err = try { response.errorBody()?.string()?.ifBlank { null } } catch (_: Exception) { null }
                val composed = buildString {
                    append("Failed to upgrade subscription: ")
                    append(response.code())
                    append(" ")
                    append(response.message())
                    err?.let {
                        append("\n")
                        append(it.take(500))
                    }
                }
                throw Exception(composed)
            }

            val body = response.body() ?: throw Exception("Response body is null")

            Subscription(
                accountId = accountId,
                planId = newPlanId,
                preferenceId = body.preferenceId,
                initPoint = body.initPoint,
                message = body.message ?: "Subscription upgraded successfully"
            )
        } catch (e: Exception) {
            throw Exception("Error upgrading subscription: ${e.message}", e)
        }

    }

}