package com.liquotrack.stocksip.features.ordermanagement.domain

interface SalesOrderRepository {
    /**
     * Generate a new purchase order.
     *
     * @param orderCode The order code to generate the purchase order for.
     * @return A string representing the generated purchase order.
     */
    suspend fun generatePurchaseOrder(orderCode: String): String
    
    /**
     * Create a Sales Order from a completed Procurement order payload.
     *
     * @return A string representing the created Sales Order.
     */
    suspend fun createSalesOrderFromCompletedProcurementOrder(): String
    
    /**
     * Get order by ID.
     *
     * @param orderCode The order code to retrieve the order for.
     * @return A string representing the order.
     */
    suspend fun getOrderById(orderCode: String): String
    
    /**
     * Get all orders.
     *
     * @return A string representing the list of orders.
     */
    suspend fun getAllOrders(): String
    
    /**
     * Create order.
     *
     * @return A string representing the created order.
     */
    suspend fun createOrder(): String
    
    /**
     * Propose a delivery schedule for an order (Supplier).
     *
     * @return A string representing the proposed delivery schedule.
     */
    suspend fun proposeDeliveryScheduleForOrder(): String
    
    /**
     * Get current delivery proposal for an order (Supplier).
     *
     * @return A string representing the current delivery proposal.
     */
    suspend fun getDeliveryProposalForOrder(): String
    
    /**
     * Respond to a delivery proposal (LiquorStoreOwner).
     *
     * @return A string representing the response to the delivery proposal.
     */
    suspend fun respondToDeliveryProposal(): String
    
    /**
     * Get current delivery proposal and response (LiquorStoreOwner).
     *
     * @return A string representing the current delivery proposal and response.
     */
    suspend fun getDeliveryProposalAndResponse(): String
    
    /**
     * Update order status.
     *
     * @return A string representing the updated order status.
     */
    suspend fun updateOrderStatus(): String
    
    /**
     * Get order status.
     *
     * @return A string representing the order status.
     */
    suspend fun getOrderStatus(): String
    
    /**
     * Get orders by supplier ID.
     *
     * @return A string representing the list of orders.
     */
    suspend fun getOrdersBySupplierId(): String
    
    /**
     * Get orders by Liquor Store Owner ID.
     *
     * @return A string representing the list of orders.
     */
    suspend fun getOrdersByLiquorStoreOwnerId(): String
}