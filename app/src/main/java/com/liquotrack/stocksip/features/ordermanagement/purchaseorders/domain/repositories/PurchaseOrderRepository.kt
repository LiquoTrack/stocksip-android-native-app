package com.liquotrack.stocksip.features.ordermanagement.purchaseorders.domain.repositories

import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models.PurchaseOrderDto
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models.PurchaseOrderItemRequestDto
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models.PurchaseOrderRequestDto

interface PurchaseOrderRepository {
    suspend fun createPurchaseOrder(accountId: String, request: PurchaseOrderRequestDto): Result<PurchaseOrderDto>
    suspend fun getPurchaseOrders(accountId: String): Result<List<PurchaseOrderDto>>
    suspend fun getPurchaseOrderById(purchaseOrderId: String): Result<PurchaseOrderDto>
    suspend fun addItem(purchaseOrderId: String, item: PurchaseOrderItemRequestDto): Result<Unit>
    suspend fun removeItem(purchaseOrderId: String, productId: String): Result<Unit>
    suspend fun confirmOrder(purchaseOrderId: String): Result<Unit>
}