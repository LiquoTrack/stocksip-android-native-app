package com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.services

import com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.models.WarehouseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
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

    @GET("warehouses/{warehouseId}")
    suspend fun getWarehouseById(@Path("warehouseId") warehouseId: String): Response<WarehouseDto>

    /**
     * Creates a new warehouse for a specific account.
     *
     * @param accountId The ID of the account for which to create the warehouse.
     * @param fields A map of form fields required to create the warehouse.
     * @param image An optional image file to be uploaded with the warehouse data.
     * @return A [Response] containing the created [WarehouseDto] object.
     */
    @Multipart
    @POST("accounts/{accountId}/warehouses/")
    suspend fun createWarehouse(
        @Path("accountId") accountId: String,
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part?
    ): Response<WarehouseDto>
}