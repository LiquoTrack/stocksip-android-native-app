package com.liquotrack.stocksip.features.inventorymanagement.inventories.data.repositories

import com.liquotrack.stocksip.features.inventorymanagement.inventories.data.remote.services.InventoryService
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryAdditionRequest
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryResponse
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventorySubtrackRequest
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.models.InventoryTransferRequest
import com.liquotrack.stocksip.features.inventorymanagement.inventories.domain.repositories.InventoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the [InventoryRepository] interface that interacts with the [InventoryService] to perform
 * inventory-related operations.
 *
 * @property service The remote service used to perform inventory operations. It is injected via constructor injection using Dagger.
 */
class InventoryRepositoryImpl @Inject constructor(private val service: InventoryService) : InventoryRepository {

    /**
     * Retrieves all inventories associated with a specific warehouse ID.
     * @param warehouseId The unique identifier of the warehouse.
     *
     * @return A list of InventoryResponse objects associated with the specified warehouse.
     */
    override suspend fun getAllInventoriesByWarehouseId(warehouseId: String): List<InventoryResponse>
        = withContext(Dispatchers.IO) {
            try {
                val response = service.getAllInventoriesByWarehouseId(warehouseId)
                if (response.isSuccessful) {
                    val body = response.body() ?: return@withContext emptyList()
                    return@withContext body.map { dto ->
                        InventoryResponse(
                            id = dto.inventoryId,
                            productId = dto.productId,
                            name = dto.name,
                            type = dto.type,
                            brand = dto.brand,
                            unitPrice = dto.unitPrice,
                            moneyCode = dto.moneyCode,
                            minimumStock = dto.minimumStock,
                            imageUrl = dto.imageUrl,
                            currentState = dto.currentState,
                            quantity = dto.quantity,
                            warehouseId = dto.warehouseId,
                            expirationDate = dto.expirationDate
                        )
                    }
                }
                return@withContext emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext emptyList()
            }
    }

    /**
     * Retrieves an inventory by its product ID and warehouse ID.
     * @param productId The unique identifier of the product.
     * @param warehouseId The unique identifier of the warehouse.
     *
     * @return The InventoryResponse object if found, null otherwise.
     */
    override suspend fun getInventoryByProductIdAndWarehouseId(
        productId: String,
        warehouseId: String
    ): InventoryResponse? = withContext(Dispatchers.IO) {
        try {
            val response = service.getInventoryByProductIdAndWarehouseId(warehouseId, productId)
            if (response.isSuccessful) {
                val body = response.body() ?: return@withContext null
                return@withContext body.let { dto ->
                    InventoryResponse(
                        id = dto.inventoryId,
                        productId = dto.productId,
                        name = dto.name,
                        type = dto.type,
                        brand = dto.brand,
                        unitPrice = dto.unitPrice,
                        moneyCode = dto.moneyCode,
                        minimumStock = dto.minimumStock,
                        imageUrl = dto.imageUrl,
                        currentState = dto.currentState,
                        quantity = dto.quantity,
                        warehouseId = dto.warehouseId,
                        expirationDate = dto.expirationDate
                    )
                }
            }
            return@withContext null
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    /**
     * Adds products to a warehouse's inventory. It can create a new inventory entry or update an existing one.
     *
     * @param warehouseId The unique identifier of the warehouse.
     * @param productId The unique identifier of the product.
     * @param inventory The InventoryAdditionRequest object containing details of the inventory addition.
     *
     * @return The updated InventoryResponse object after adding the products, or null if the operation fails.
     */
    override suspend fun addProductsToWarehouseInventory(
        warehouseId: String,
        productId: String,
        inventory: InventoryAdditionRequest
    ): InventoryResponse? = withContext(Dispatchers.IO) {
        try {
            val response = service.addProductsToWarehouseInventory(warehouseId, productId, inventory)
            if (response.isSuccessful) {
                val body = response.body() ?: return@withContext null
                return@withContext body.let { dto ->
                    InventoryResponse(
                        id = dto.inventoryId,
                        productId = dto.productId,
                        name = dto.name,
                        type = dto.type,
                        brand = dto.brand,
                        unitPrice = dto.unitPrice,
                        moneyCode = dto.moneyCode,
                        minimumStock = dto.minimumStock,
                        imageUrl = dto.imageUrl,
                        currentState = dto.currentState,
                        quantity = dto.quantity,
                        warehouseId = dto.warehouseId,
                        expirationDate = dto.expirationDate
                    )
                }
            } else {
                return@withContext null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    /**
     * Subtracts products from a warehouse's inventory.
     *
     * @param warehouseId The unique identifier of the warehouse.
     * @param productId The unique identifier of the product.
     * @param inventory The InventorySubtrackRequest object containing details of the inventory subtraction.
     *
     * @return The updated InventoryResponse object after subtracting the products, or null if the operation fails.
     */
    override suspend fun subtrackProductsFromWarehouseInventory(
        warehouseId: String,
        productId: String,
        inventory: InventorySubtrackRequest
    ): InventoryResponse? = withContext(Dispatchers.IO) {
        try {
            val response = service.subtrackProductsFromWarehouseInventory(warehouseId, productId, inventory)
            if (response.isSuccessful) {
                val body = response.body() ?: return@withContext null
                return@withContext body.let { dto ->
                    InventoryResponse(
                        id = dto.inventoryId,
                        productId = dto.productId,
                        name = dto.name,
                        type = dto.type,
                        brand = dto.brand,
                        unitPrice = dto.unitPrice,
                        moneyCode = dto.moneyCode,
                        minimumStock = dto.minimumStock,
                        imageUrl = dto.imageUrl,
                        currentState = dto.currentState,
                        quantity = dto.quantity,
                        warehouseId = dto.warehouseId,
                        expirationDate = dto.expirationDate
                    )
                }
            } else {
                return@withContext null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    /**
     * Transfers products from one warehouse to another.
     *
     * @param originWarehouseId The unique identifier of the origin warehouse.
     * @param productToTransferId The unique identifier of the product to be transferred.
     * @param inventoryRequest The InventoryTransferRequest object containing details of the transfer.
     *
     * @return The updated InventoryResponse object after the transfer, or null if the operation fails.
     */
    override suspend fun transferProductsToAnotherWarehouse(
        originWarehouseId: String,
        productToTransferId: String,
        inventoryRequest: InventoryTransferRequest
    ): InventoryResponse? = withContext(Dispatchers.IO) {
        try {
            val response = service.transferProductsToAnotherWarehouse(
                warehouseId = originWarehouseId,
                productId = productToTransferId,
                inventoryTransferRequest = inventoryRequest
            )

            if (response.isSuccessful) {
                val body = response.body() ?: return@withContext null
                return@withContext body.let { dto ->
                    InventoryResponse(
                        id = dto.inventoryId,
                        productId = dto.productId,
                        name = dto.name,
                        type = dto.type,
                        brand = dto.brand,
                        unitPrice = dto.unitPrice,
                        moneyCode = dto.moneyCode,
                        minimumStock = dto.minimumStock,
                        imageUrl = dto.imageUrl,
                        currentState = dto.currentState,
                        quantity = dto.quantity,
                        warehouseId = dto.warehouseId,
                        expirationDate = dto.expirationDate
                    )
                }
            } else {
                return@withContext null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    /**
     * Retrieves an inventory by its unique identifier.
     *
     * @param inventoryId The unique identifier of the inventory.
     *
     * @return The InventoryResponse object if found, null otherwise.
     */
    override suspend fun getInventoryById(inventoryId: String): InventoryResponse? = withContext(Dispatchers.IO) {
        try {
            val response = service.getInventoryById(inventoryId)
            if (response.isSuccessful) {
                val body = response.body() ?: return@withContext null
                return@withContext body.let { dto ->
                    InventoryResponse(
                        id = dto.inventoryId,
                        productId = dto.productId,
                        name = dto.name,
                        type = dto.type,
                        brand = dto.brand,
                        unitPrice = dto.unitPrice,
                        moneyCode = dto.moneyCode,
                        minimumStock = dto.minimumStock,
                        imageUrl = dto.imageUrl,
                        currentState = dto.currentState,
                        quantity = dto.quantity,
                        warehouseId = dto.warehouseId,
                        expirationDate = dto.expirationDate
                    )
                }
            }
            return@withContext null
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    /**
     * Deletes an inventory by its unique identifier.
     *
     * @param inventoryId The unique identifier of the inventory to be deleted.
     */
    override suspend fun deleteInventory(inventoryId: String) = withContext(Dispatchers.IO) {
        try {
            val response = service.deleteInventory(inventoryId)
            if (!response.isSuccessful) {
                throw Exception("Error deleting inventory: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Failed to delete inventory")
        }
    }
}