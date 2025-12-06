package com.liquotrack.stocksip.features.ordermanagement.domain

interface SalesOrderRepository {
    suspend fun createSalesOrderFromPurchaseOrder(purchaseOrderId: String, catalogIdBuyFrom: String?): SalesOrderResponse

    suspend fun getAllOrders(): List<SalesOrderResponse>
}