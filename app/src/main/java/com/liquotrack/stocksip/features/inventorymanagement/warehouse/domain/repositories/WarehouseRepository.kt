package com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.repositories

import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseProduct
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseRequest
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseResponse
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehousesWithCount
import java.io.File

/**
 * Repository interface for managing Warehouse entities.
 * Defines methods for retrieving, registering, updating, and deleting warehouses.
 */
interface WarehouseRepository {

    /**
     * Retrieves all warehouses associated with a specific account ID.
     * @param accountId The unique identifier of the account.
     *
     * @return A WarehouseWithCount object containing the total count and list of Warehouse entities.
     */
    suspend fun getAllWarehousesByAccountId(accountId: String): WarehousesWithCount

    /**
     * Retrieves a warehouse by its unique identifier.
     * @param warehouseId The unique identifier of the warehouse.
     *
     * @return The Warehouse entity if found, null otherwise.
     */
    suspend fun getWarehouseById(warehouseId: String): WarehouseResponse

    /**
     * Registers a new warehouse.
     *
     * @param warehouse The Warehouse entity to be registered.
     * @return The registered Warehouse entity with its unique identifier.
     */
    suspend fun registerWarehouse(warehouse: WarehouseRequest, accountId: String, imageFile: File?): WarehouseResponse

    /**
     * Updates an existing warehouse.
     *
     * @param warehouse The Warehouse entity with updated information.
     * @return The updated Warehouse entity.
     */
    suspend fun updateWarehouse(warehouse: WarehouseRequest, warehouseId: String, imageFile: File?): WarehouseResponse

    /**
     * Deletes a warehouse by its unique identifier.
     *
     * @param warehouseId The unique identifier of the warehouse to be deleted.
     */
    suspend fun deleteWarehouse(warehouseId: String)

    /**
     * Retrieves all products stored in a specific warehouse.
     * @param warehouseId The unique identifier of the warehouse.
     *
     * @return A list of WarehouseProduct entities stored in the specified warehouse.
     */
    suspend fun getProductsByWarehouseId(warehouseId: String): List<WarehouseProduct>
}