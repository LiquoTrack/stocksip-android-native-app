package com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.services

import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models.PurchaseOrderDto
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models.PurchaseOrderItemRequestDto
import com.liquotrack.stocksip.features.ordermanagement.purchaseorders.data.remote.models.PurchaseOrderRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PurchaseOrderService {

    @POST("accounts/{accountId}/purchase-orders")
    suspend fun createPurchaseOrder(
        @Path("accountId") accountId: String,
        @Body request: PurchaseOrderRequestDto
    ): Response<PurchaseOrderDto>

    @GET("accounts/{accountId}/purchase-orders")
    suspend fun getPurchaseOrders(
        @Path("accountId") accountId: String
    ): Response<List<PurchaseOrderDto>>

    @GET("purchase-orders/{purchaseOrderId}")
    suspend fun getPurchaseOrderById(
        @Path("purchaseOrderId") purchaseOrderId: String
    ): Response<PurchaseOrderDto>

    @POST("purchase-orders/{purchaseOrderId}/items")
    suspend fun addItemToPurchaseOrder(
        @Path("purchaseOrderId") purchaseOrderId: String,
        @Body item: PurchaseOrderItemRequestDto
    ): Response<Unit>

    @DELETE("purchase-orders/{purchaseOrderId}/items/{productId}")
    suspend fun removeItemFromPurchaseOrder(
        @Path("purchaseOrderId") purchaseOrderId: String,
        @Path("productId") productId: String
    ): Response<Unit>

    @PUT("purchase-orders/{purchaseOrderId}/confirmations")
    suspend fun confirmPurchaseOrder(
        @Path("purchaseOrderId") purchaseOrderId: String
    ): Response<Unit>
}