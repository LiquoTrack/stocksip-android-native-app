package com.liquotrack.stocksip.features.ordermanagement.domain

interface SalesOrderRepository {
    /**
     * Get order by ID.
     */
    suspend fun getOrderById(orderId: String): SalesOrderResponse

    /**
     * Get all orders.
     */
    suspend fun getAllOrders(): List<SalesOrderResponse>

    /**
     * Create order.
     */
    suspend fun createOrder(request: CreateOrderRequest): SalesOrderResponse

    /**
     * Propose a delivery schedule for an order (Supplier).
     */
    suspend fun proposeDeliveryScheduleForOrder(orderId: String, proposedDate: String, notes: String?): SalesOrderResponse

    /**
     * Get current delivery proposal for an order (Supplier).
     */
    suspend fun getDeliveryProposalForOrder(orderId: String): SalesOrderResponse

    /**
     * Respond to a delivery proposal (LiquorStoreOwner).
     */
    suspend fun respondToDeliveryProposal(orderId: String, accept: Boolean, notes: String?): SalesOrderResponse

    /**
     * Get current delivery proposal and response (LiquorStoreOwner).
     */
    suspend fun getDeliveryProposalAndResponse(orderId: String): SalesOrderResponse

    /**
     * Update order status (alias values supported: PENDING, CONFIRM, CANCEL).
     */
    suspend fun updateOrderStatus(orderId: String, newStatusAlias: String, reason: String? = null): SalesOrderResponse

    /**
     * Get order status summary and last update.
     */
    suspend fun getOrderStatus(orderId: String): OrderStatusResponse

    /**
     * Get orders by supplier ID.
     */
    suspend fun getOrdersBySupplierId(supplierId: String): SupplierOrdersResponse

    /**
     * Get orders by Liquor Store Owner ID.
     */
    suspend fun getOrdersByLiquorStoreOwnerId(liquorStoreOwnerId: String): LiquorStoreOwnerOrdersResponse
}