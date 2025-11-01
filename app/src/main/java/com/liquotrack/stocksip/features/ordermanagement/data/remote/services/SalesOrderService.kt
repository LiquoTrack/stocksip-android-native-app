package com.liquotrack.stocksip.features.ordermanagement.data.remote.services

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SalesOrderService {
    @POST("/api/v1/orders/generate-purchase-order")
    suspend fun generatePurchaseOrder(): String

    @POST("/api/v1/orders/from-procurement/completed")
    suspend fun createSalesOrderFromCompletedProcurementOrder(): String

    @GET("/api/v1/orders/{id}")
    suspend fun getOrderById(@Path("id") id: String): String

    @GET("/api/v1/orders")
    suspend fun getAllOrders(): String

    @POST("/api/v1/orders")
    suspend fun createOrder(): String

    @POST("/api/v1/orders/{orderId}/delivery/propose")
    suspend fun proposeDeliveryScheduleForOrder(@Path("orderId") orderId: String): String

    @GET("/api/v1/orders/{orderId}/delivery/propose")
    suspend fun getDeliveryProposalForOrder(@Path("orderId") orderId: String): String

    @POST("/api/v1/orders/{orderId}/delivery/respond")
    suspend fun respondToDeliveryProposal(@Path("orderId") orderId: String): String

    @GET("/api/v1/orders/{orderId}/delivery/respond")
    suspend fun getDeliveryProposalAndResponse(@Path("orderId") orderId: String): String

    @PUT("/api/v1/orders/{orderId}/status")
    suspend fun updateOrderStatus(@Path("orderId") orderId: String): String

    @GET("/api/v1/orders/{orderId}/status")
    suspend fun getOrderStatus(@Path("orderId") orderId: String): String

    @GET("/api/v1/orders/supplier/{supplierId}")
    suspend fun getOrdersBySupplierId(@Path("supplierId") supplierId: String): String

    @GET("/api/v1/orders/liquor-store-owner/{liquorStoreOwnerId}")
    suspend fun getOrdersByLiquorStoreOwnerId(@Path("liquorStoreOwnerId") liquorStoreOwnerId: String): String
}