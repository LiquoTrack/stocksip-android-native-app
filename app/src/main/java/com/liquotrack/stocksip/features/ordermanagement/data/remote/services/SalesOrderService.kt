package com.liquotrack.stocksip.features.ordermanagement.data.remote.services

import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.CreateSalesOrderRequestDto
import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.OrderStatusResponseDto
import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.ProposeDeliveryScheduleRequestDto
import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.RespondDeliveryProposalRequestDto
import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.SalesOrderDto
import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.SupplierOrdersResponseDto
import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.UpdateOrderStatusRequestDto
import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.LiquorStoreOwnerOrdersResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SalesOrderService {
    @POST("orders/generate-purchase-order")
    suspend fun generatePurchaseOrder(): Response<SalesOrderDto>

    @POST("orders/from-procurement/completed")
    suspend fun createSalesOrderFromCompletedProcurementOrder(): Response<SalesOrderDto>

    @GET("orders/{id}")
    suspend fun getOrderById(@Path("id") id: String): Response<SalesOrderDto>

    @GET("orders")
    suspend fun getAllOrders(): Response<List<SalesOrderDto>>

    @POST("orders")
    suspend fun createOrder(@Body body: CreateSalesOrderRequestDto): Response<SalesOrderDto>

    @POST("orders/{orderId}/delivery/propose")
    suspend fun proposeDeliveryScheduleForOrder(
        @Path("orderId") orderId: String,
        @Body body: ProposeDeliveryScheduleRequestDto
    ): Response<SalesOrderDto>

    @GET("orders/{orderId}/delivery/propose")
    suspend fun getDeliveryProposalForOrder(@Path("orderId") orderId: String): Response<SalesOrderDto>

    @POST("orders/{orderId}/delivery/respond")
    suspend fun respondToDeliveryProposal(
        @Path("orderId") orderId: String,
        @Body body: RespondDeliveryProposalRequestDto
    ): Response<SalesOrderDto>

    @GET("orders/{orderId}/delivery/respond")
    suspend fun getDeliveryProposalAndResponse(@Path("orderId") orderId: String): Response<SalesOrderDto>

    @PUT("orders/{orderId}/status")
    suspend fun updateOrderStatus(
        @Path("orderId") orderId: String,
        @Body body: UpdateOrderStatusRequestDto
    ): Response<SalesOrderDto>

    @GET("orders/{orderId}/status")
    suspend fun getOrderStatus(@Path("orderId") orderId: String): Response<OrderStatusResponseDto>

    @GET("orders/supplier/{supplierId}")
    suspend fun getOrdersBySupplierId(@Path("supplierId") supplierId: String): Response<SupplierOrdersResponseDto>

    @GET("orders/liquor-store-owner/{liquorStoreOwnerId}")
    suspend fun getOrdersByLiquorStoreOwnerId(@Path("liquorStoreOwnerId") liquorStoreOwnerId: String): Response<LiquorStoreOwnerOrdersResponseDto>
}