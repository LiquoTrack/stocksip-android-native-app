package com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.repositories

import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseRequest
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseResponse
import java.io.File

/**
 * Repository interface for managing Warehouse entities.
 * Defines methods for retrieving, registering, updating, and deleting warehouses.
 */
interface WarehouseRepository {

    /**
     * Retrieves all warehouses associatead with a specific account ID.
     * @param accountId The unique identifier of the account.
     *
     * @return A list of Warehouse entities associated with the given account ID.
     */
    suspend fun getAllWarehousesByAccountId(accountId: String): List<WarehouseResponse>

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
    suspend fun updateWarehouse(warehouse: WarehouseResponse): WarehouseResponse

    /**
     * Deletes a warehouse by its unique identifier.
     *
     * @param warehouseId The unique identifier of the warehouse to be deleted.
     */
    suspend fun deleteWarehouse(warehouseId: String)
}