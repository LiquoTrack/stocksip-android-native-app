package com.liquotrack.stocksip.features.ordermanagement.data.remote.services

import com.liquotrack.stocksip.features.ordermanagement.data.remote.models.SalesOrderDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

import retrofit2.http.Query

interface SalesOrderService {

    @POST("orders/from-procurement/{purchaseOrderId}")
    suspend fun createSalesOrderFromPurchaseOrder(
        @Path("purchaseOrderId") purchaseOrderId: String,
        @Query("catalogId") catalogIdBuyFrom: String?
    ): Response<SalesOrderDto>

    @GET("orders")
    suspend fun getAllOrders(): Response<List<SalesOrderDto>>

}