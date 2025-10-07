package com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.repositories

import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.Warehouse

/**
 * Repository interface for managing Warehouse entities.
 * Defines methods for retrieving, registering, updating, and deleting warehouses.
 */
interface WarehouseRepository {

    /**
     * Retrieves all warehouses associated with a specific account ID.
     * @param accountId The unique identifier of the account.
     *
     * @return A list of Warehouse entities associated with the given account ID.
     */
    suspend fun getAllByAccountIdWarehouses(accountId: String): List<Warehouse>

    /**
     * Retrieves a warehouse by its unique identifier.
     * @param warehouseId The unique identifier of the warehouse.
     *
     * @return The Warehouse entity if found, null otherwise.
     */
    suspend fun getWarehouseById(warehouseId: String): Warehouse

    /**
     * Registers a new warehouse.
     *
     * @param warehouse The Warehouse entity to be registered.
     * @return The registered Warehouse entity with its unique identifier.
     */
    suspend fun registerWarehouse(warehouse: Warehouse): Warehouse

    /**
     * Updates an existing warehouse.
     *
     * @param warehouse The Warehouse entity with updated information.
     * @return The updated Warehouse entity.
     */
    suspend fun updateWarehouse(warehouse: Warehouse): Warehouse

    /**
     * Deletes a warehouse by its unique identifier.
     *
     * @param warehouseId The unique identifier of the warehouse to be deleted.
     */
    suspend fun deleteWarehouse(warehouseId: String)
}