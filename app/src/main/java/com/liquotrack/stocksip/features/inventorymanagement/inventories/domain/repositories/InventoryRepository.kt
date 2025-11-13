package com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.repositories

import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryAdditionRequest
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryResponse
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventorySubtrackRequest
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryTransferRequest

/**
 * Repository interface for managing Inventory entities.
 * Defines methods for retrieving, adding, subtracting, and deleting inventories.
 */
interface InventoryRepository {

    /**
     * Retrieves all inventories associated with a specific warehouse ID.
     * @param warehouseId The unique identifier of the warehouse.
     *
     * @return A list of InventoryResponse objects associated with the specified warehouse.
     */
    suspend fun getAllInventoriesByWarehouseId(
        warehouseId: String
    ): List<InventoryResponse>

    /**
     * Retrieves an inventory by its product ID and warehouse ID.
     * @param productId The unique identifier of the product.
     * @param warehouseId The unique identifier of the warehouse.
     *
     * @return The InventoryResponse object if found, null otherwise.
     */
    suspend fun getInventoryByProductIdAndWarehouseId(
        productId: String,
        warehouseId: String
    ): InventoryResponse?

    /**
     * Adds products to a warehouse's inventory. It can create a new inventory entry or update an existing one.
     *
     * @param warehouseId The unique identifier of the warehouse.
     * @param productId The unique identifier of the product.
     * @param inventory The InventoryAdditionRequest object containing details of the inventory addition.
     *
     * @return The updated InventoryResponse object after adding the products, or null if the operation fails.
     */
    suspend fun addProductsToWarehouseInventory(
        warehouseId: String,
        productId: String,
        inventory: InventoryAdditionRequest
    ): InventoryResponse?

    /**
     * Subtracts products from a warehouse's inventory.
     *
     * @param warehouseId The unique identifier of the warehouse.
     * @param productId The unique identifier of the product.
     * @param inventory The InventorySubtrackRequest object containing details of the inventory subtraction.
     *
     * @return The updated InventoryResponse object after subtracting the products, or null if the operation fails.
     */
    suspend fun subtrackProductsFromWarehouseInventory(
        warehouseId: String,
        productId: String,
        inventory: InventorySubtrackRequest
    ): InventoryResponse?

    /**
     * Transfers products from one warehouse to another.
     *
     * @param originWarehouseId The unique identifier of the origin warehouse.
     * @param productToTransferId The unique identifier of the product to be transferred.
     * @param inventoryRequest The InventoryTransferRequest object containing details of the transfer.
     *
     * @return The updated InventoryResponse object after the transfer, or null if the operation fails.
     */
    suspend fun transferProductsToAnotherWarehouse(
        originWarehouseId: String,
        productToTransferId: String,
        inventoryRequest: InventoryTransferRequest
    ): InventoryResponse?

    /**
     * Deletes an inventory by its unique identifier.
     *
     * @param inventoryId The unique identifier of the inventory to be deleted.
     */
    suspend fun deleteInventory(inventoryId: String)
}