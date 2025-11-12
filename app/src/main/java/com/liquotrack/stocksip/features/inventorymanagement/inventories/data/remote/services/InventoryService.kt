package com.liquotrack.stocksip.features.inventorymanagement.inventories.data.remote.services

import com.liquotrack.stocksip.features.inventorymanagement.inventories.data.remote.models.InventoryDto
import com.liquotrack.stocksip.features.inventorymanagement.inventories.data.remote.models.InventoryDtoItem
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryAdditionRequest
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventorySubtrackRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Service interface for managing Inventory entities via remote API calls.
 * Defines methods for retrieving, adding, subtracting, and deleting inventories.
 */
interface InventoryService {

    /**
     * Retrieves all inventories for a specific warehouse by its ID.
     *
     * @param warehouseId The ID of the warehouse. It is passed as a path parameter.
     *
     * @return A [Response] object containing an [InventoryDto] with the list of inventories.
     */
    @GET("warehouses/{warehouseId}/products")
    suspend fun getAllInventoriesByWarehouseId(
        @Path("warehouseId") warehouseId: String
    ): Response<InventoryDto>

    /**
     * Retrieves the inventory for a specific product in a specific warehouse.
     *
     * @param warehouseId The ID of the warehouse. It is passed as a path parameter.
     * @param productId The ID of the product. It is passed as a path parameter.
     *
     * @return A [Response] object containing an [InventoryDtoItem] for the specified product and warehouse.
     */
    @GET("warehouses/{warehouseId}/products/{productId}")
    suspend fun getInventoryByProductIdAndWarehouseId(
        @Path("warehouseId") warehouseId: String,
        @Path("productId") productId: String
    ): Response<InventoryDtoItem>

    /**
     * Adds products to the inventory of a specific product in a specific warehouse.
     *
     * @param warehouseId The ID of the warehouse. It is passed as a path parameter.
     * @param productId The ID of the product. It is passed as a path parameter.
     * @param inventoryAdditionRequest The request body containing the details of the addition. It is passed as a [InventoryAdditionRequest] in the request body.
     *
     * @return A [Response] object containing the updated [InventoryDtoItem].
     */
    @POST("warehouses/{warehouseId}/products/{productId}/additions")
    suspend fun addProductsToWarehouseInventory(
        @Path("warehouseId") warehouseId: String,
        @Path("productId") productId: String,
        @Body inventoryAdditionRequest: InventoryAdditionRequest
    ): Response<InventoryDtoItem>

    /**
     * Subtracts products from the inventory of a specific product in a specific warehouse.
     *
     * @param warehouseId The ID of the warehouse. It is passed as a path parameter.
     * @param productId The ID of the product. It is passed as a path parameter
     * @param inventorySubtrackRequest The request body containing the details of the subtraction. It is passed as a [InventorySubtrackRequest] in the request body.
     *
     * @return A [Response] object containing the updated [InventoryDtoItem].
     */
    @POST("warehouses/{warehouseId}/products/{productId}/subtractions")
    suspend fun subtrackProductsFromWarehouseInventory(
        @Path("warehouseId") warehouseId: String,
        @Path("productId") productId: String,
        @Body inventorySubtrackRequest: InventorySubtrackRequest
    ): Response<InventoryDtoItem>

    /**
     * Deletes an inventory by its ID.
     *
     * @param inventoryId The ID of the inventory to be deleted. It is passed as a path parameter.
     *
     * @return A [Response] object indicating the result of the deletion operation.
     */
    @DELETE("inventories/{inventoryId}")
    suspend fun deleteInventory(
        @Path("inventoryId") inventoryId: String
    ): Response<Unit>
}