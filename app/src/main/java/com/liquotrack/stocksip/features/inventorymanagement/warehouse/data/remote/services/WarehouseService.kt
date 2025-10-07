package com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.services

import com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.models.WarehouseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Service interface for fetching warehouse data from the remote API.
 * This interface defines the endpoints and HTTP methods used to interact with the warehouse-related API.
 */
interface WarehouseService {

    /**
     * Fetches all warehouses associated with a specific account ID.
     *
     * @param accountId The ID of the account for which to retrieve warehouses.
     * @return A [Response] containing a list of [WarehouseDto] objects.
     */
    @GET("accounts/{accountId}/warehouses")
    suspend fun getAllWarehousesByAccountId(@Path("accountId") accountId: String): Response<List<WarehouseDto>>
}