package com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.repositories

import android.util.Log
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.helpers.toMultipart
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.services.WarehouseService
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.Warehouse
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseResponse
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
    override suspend fun getAllWarehousesByAccountId(accountId: String): List<WarehouseResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getAllWarehousesByAccountId(accountId)
                if (response.isSuccessful) {
                    response.body()?.let { warehouseDtoList ->
                        return@withContext warehouseDtoList.map { warehouseDto ->
                            WarehouseResponse(
                                id = warehouseDto.id,
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
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext emptyList()
        }

    override suspend fun getWarehouseById(warehouseId: String): WarehouseResponse {
        TODO("Not yet implemented")
    }

    override suspend fun registerWarehouse(warehouse: Warehouse, accountId: String, imageFile: File?): WarehouseResponse =
        withContext(Dispatchers.IO) {
            try {
                Log.d("WAREHOUSE_DEBUG", "🟡 REPOSITORY - Starting registerWarehouse")
                Log.d("WAREHOUSE_DEBUG", "📦 Warehouse data: $warehouse")
                Log.d("WAREHOUSE_DEBUG", "👤 Account ID: $accountId")
                Log.d("WAREHOUSE_DEBUG", "🖼️ Image file: ${imageFile?.name ?: "null"}")

                val (fields, imagePart) = warehouse.toMultipart(imageFile)

                // ✅ LOG DETALLADO de los campos que se envían
                Log.d("WAREHOUSE_DEBUG", "📋 Multipart Fields:")
                fields.forEach { (key, value) ->
                    val fieldValue = if (value is String) value else "[Binary Data]"
                    Log.d("WAREHOUSE_DEBUG", "   - $key: $fieldValue")
                }
                Log.d("WAREHOUSE_DEBUG", "🖼️ Image Part: ${imagePart != null}")

                Log.d("WAREHOUSE_DEBUG", "🚀 Calling API service...")
                val response = service.createWarehouse(
                    accountId = accountId,
                    fields = fields,
                    image = imagePart
                )

                Log.d("WAREHOUSE_DEBUG", "📡 API Response - Code: ${response.code()}, Message: ${response.message()}")

                // ✅ LOG del cuerpo del error si hay 400
                if (!response.isSuccessful) {
                    val errorBody = response.errorBody()?.string()
                    Log.e("WAREHOUSE_DEBUG", "❌ API Error Body: $errorBody")
                }

                if (response.isSuccessful) {
                    response.body()?.let { warehouseDto ->
                        Log.d("WAREHOUSE_DEBUG", "✅ API Success - Warehouse created with ID: ${warehouseDto.id}")
                        return@withContext WarehouseResponse(
                            id = warehouseDto.id,
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
                    Log.e("WAREHOUSE_DEBUG", "❌ API Success but null body")
                } else {
                    Log.e("WAREHOUSE_DEBUG", "❌ API Error: ${response.code()} - ${response.message()}")
                    throw Exception("Error registering warehouse: ${response.code()} ${response.message()}")
                }

            } catch (e: Exception) {
                Log.e("WAREHOUSE_DEBUG", "❌ REPOSITORY Exception: ${e.message}", e)
                throw e
            }
            throw Exception("Failed to register warehouse")
        }

    override suspend fun updateWarehouse(warehouse: WarehouseResponse): WarehouseResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWarehouse(warehouseId: String) {
        TODO("Not yet implemented")
    }
}