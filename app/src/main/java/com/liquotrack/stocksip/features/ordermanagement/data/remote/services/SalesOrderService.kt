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

    @POST("orders/from-procurement/{purchaseOrderId}")
    suspend fun createSalesOrderFromPurchaseOrder(
        @Path("purchaseOrderId") purchaseOrderId: String
    ): Response<SalesOrderDto>

    @GET("orders")
    suspend fun getAllOrders(): Response<List<SalesOrderDto>>

}