package com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.repositories

import com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.helpers.toMultipart
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.services.WarehouseService
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseRequest
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseResponse
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehousesWithCount
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.repositories.WarehouseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class WarehouseRepositoryImpl @Inject constructor(private val service: WarehouseService) : WarehouseRepository {

    /**
     * Retrieves all warehouses associated with a specific account ID.
     * This implementation fetches data from a remote service and maps the DTOs to domain models.
     *
     * @param accountId The unique identifier of the account.
     * @return A list of Warehouse entities associated with the given account ID.
     */
    override suspend fun getAllWarehousesByAccountId(accountId: String): WarehousesWithCount =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getAllWarehousesByAccountId(accountId)
                if (response.isSuccessful) {
                    response.body()?.let { wrapper ->
                        val warehouses = wrapper.warehouses.map { warehouseDto ->
                            WarehouseResponse(
                                id = warehouseDto.warehouseId,
                                name = warehouseDto.name,
                                street = warehouseDto.addressStreet,
                                city = warehouseDto.addressCity,
                                district = warehouseDto.addressDistrict,
                                postalCode = warehouseDto.addressPostalCode,
                                country = warehouseDto.addressCountry,
                                temperatureMin = warehouseDto.temperatureMin,
                                temperatureMax = warehouseDto.temperatureMax,
                                capacity = warehouseDto.capacity,
                                imageUrl = warehouseDto.imageUrl
                            )
                        }
                        return@withContext WarehousesWithCount(wrapper.total, wrapper.maxWarehousesAllowed,warehouses)
                    }
                }
                WarehousesWithCount(0, 0,emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                WarehousesWithCount(0, 0,emptyList())
            }
        }

    override suspend fun getWarehouseById(warehouseId: String): WarehouseResponse = withContext(
        Dispatchers.IO) {
        try {
            val response = service.getWarehouseById(warehouseId)
            if (response.isSuccessful) {
                response.body()?.let { warehouseDto ->
                    return@withContext WarehouseResponse(
                        id = warehouseDto.warehouseId,
                        name = warehouseDto.name,
                        street = warehouseDto.addressStreet,
                        city = warehouseDto.addressCity,
                        district = warehouseDto.addressDistrict,
                        postalCode = warehouseDto.addressPostalCode,
                        country = warehouseDto.addressCountry,
                        temperatureMin = warehouseDto.temperatureMin,
                        temperatureMax = warehouseDto.temperatureMax,
                        capacity = warehouseDto.capacity,
                        imageUrl = warehouseDto.imageUrl
                    )
                }
            } else {
                throw Exception("Error fetching warehouse: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        throw Exception("Failed to fetch warehouse")
    }

    override suspend fun registerWarehouse(warehouse: WarehouseRequest, accountId: String, imageFile: File?): WarehouseResponse =
        withContext(Dispatchers.IO) {
            try {

                val (fields, imagePart) = warehouse.toMultipart(imageFile)

                val response = service.createWarehouse(
                    accountId = accountId,
                    fields = fields,
                    image = imagePart
                )

                if (response.isSuccessful) {
                    response.body()?.let { warehouseDto ->

                        return@withContext WarehouseResponse(
                            id = warehouseDto.warehouseId,
                            name = warehouseDto.name,
                            street = warehouseDto.addressStreet,
                            city = warehouseDto.addressCity,
                            district = warehouseDto.addressDistrict,
                            postalCode = warehouseDto.addressPostalCode,
                            country = warehouseDto.addressCountry,
                            temperatureMin = warehouseDto.temperatureMin,
                            temperatureMax = warehouseDto.temperatureMax,
                            capacity = warehouseDto.capacity,
                            imageUrl = warehouseDto.imageUrl
                        )
                    }
                } else {
                    throw Exception("Error registering warehouse: ${response.code()} ${response.message()}")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            throw Exception("Failed to register warehouse")
        }

    override suspend fun updateWarehouse(warehouse: WarehouseResponse): WarehouseResponse {
        TODO("Not yet implemented")
    }

    /** Deletes a warehouse by its unique identifier.
     *
     * @param warehouseId The unique identifier of the warehouse to be deleted.
     */
    override suspend fun deleteWarehouse(warehouseId: String) = withContext(Dispatchers.IO) {
        try {
            val response = service.deleteWarehouse(warehouseId)
            if (!response.isSuccessful) {
                throw Exception("Error deleting warehouse: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Failed to delete warehouse")
        }

    }
}