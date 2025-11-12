package com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.repositories

import android.util.Log
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models.PurchaseOrderDto
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models.PurchaseOrderItemRequestDto
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models.PurchaseOrderRequestDto
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.services.PurchaseOrderService
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.domain.repositories.PurchaseOrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PurchaseOrderRepositoryImpl @Inject constructor(
    private val service: PurchaseOrderService
) : PurchaseOrderRepository {

    companion object {
        private const val TAG = "PURCHASE_ORDER_REPO"
    }

    override suspend fun createPurchaseOrder(
        accountId: String,
        request: PurchaseOrderRequestDto
    ): Result<PurchaseOrderDto> = runCatching {
        withContext(Dispatchers.IO) {
            Log.d(TAG, "→ Creating order for accountId=$accountId with request=$request")

            val response = service.createPurchaseOrder(accountId, request)

            Log.d(TAG, "← Response code=${response.code()} message=${response.message()}")
            Log.d(TAG, "← Response body=${response.body()}")
            Log.d(TAG, "← Response error=${response.errorBody()?.string()}")

            if (!response.isSuccessful) {
                throw Exception("Error creating order: ${response.code()} ${response.message()}")
            }

            response.body() ?: throw Exception("Empty response from server")
        }
    }

    override suspend fun getPurchaseOrders(accountId: String): Result<List<PurchaseOrderDto>> =
        runCatching {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "→ Fetching orders for accountId=$accountId")
                val response = service.getPurchaseOrders(accountId)

                Log.d(TAG, "← Response code=${response.code()} message=${response.message()}")
                Log.d(TAG, "← Response body=${response.body()}")
                Log.d(TAG, "← Response error=${response.errorBody()?.string()}")

                if (!response.isSuccessful)
                    throw Exception("Error fetching orders: ${response.code()}")
                response.body() ?: emptyList()
            }
        }

    override suspend fun getPurchaseOrderById(purchaseOrderId: String): Result<PurchaseOrderDto> =
        runCatching {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "→ Fetching order by id=$purchaseOrderId")
                val response = service.getPurchaseOrderById(purchaseOrderId)

                Log.d(TAG, "← Response code=${response.code()} message=${response.message()}")
                Log.d(TAG, "← Response body=${response.body()}")
                Log.d(TAG, "← Response error=${response.errorBody()?.string()}")

                if (!response.isSuccessful)
                    throw Exception("Order not found: ${response.code()}")
                response.body() ?: throw Exception("Empty response")
            }
        }

    override suspend fun addItem(purchaseOrderId: String, item: PurchaseOrderItemRequestDto): Result<Unit> =
        runCatching {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "→ Adding item to orderId=$purchaseOrderId item=$item")
                val response = service.addItemToPurchaseOrder(purchaseOrderId, item)

                Log.d(TAG, "← Response code=${response.code()} message=${response.message()}")
                Log.d(TAG, "← Response error=${response.errorBody()?.string()}")

                if (!response.isSuccessful)
                    throw Exception("Error adding item: ${response.code()}")
            }
        }

    override suspend fun removeItem(purchaseOrderId: String, productId: String): Result<Unit> =
        runCatching {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "→ Removing item productId=$productId from orderId=$purchaseOrderId")
                val response = service.removeItemFromPurchaseOrder(purchaseOrderId, productId)

                Log.d(TAG, "← Response code=${response.code()} message=${response.message()}")
                Log.d(TAG, "← Response error=${response.errorBody()?.string()}")

                if (!response.isSuccessful)
                    throw Exception("Error removing item: ${response.code()}")
            }
        }

    override suspend fun confirmOrder(purchaseOrderId: String): Result<Unit> =
        runCatching {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "→ Confirming orderId=$purchaseOrderId")
                val response = service.confirmPurchaseOrder(purchaseOrderId)

                Log.d(TAG, "← Response code=${response.code()} message=${response.message()}")
                Log.d(TAG, "← Response error=${response.errorBody()?.string()}")

                if (!response.isSuccessful)
                    throw Exception("Error confirming order: ${response.code()}")
            }
        }
}
