package com.liquotrack.stocksip.features.ordermanagement.domain

interface SalesOrderRepository {
    suspend fun createSalesOrderFromPurchaseOrder(purchaseOrderId: String): SalesOrderResponse

    suspend fun getOrderById(orderId: String): SalesOrderResponse
}