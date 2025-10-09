package com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.repositories

import com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.services.WarehouseService
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.Warehouse
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.repositories.WarehouseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WarehouseRepositoryImpl @Inject constructor(private val service: WarehouseService) : WarehouseRepository {

    override suspend fun getAllByAccountIdWarehouses(accountId: String): List<Warehouse> =
        withContext(Dispatchers.IO) {
            try {
                val response = service.getAllWarehousesByAccountId(accountId)
                if (response.isSuccessful) {
                    response.body()?.let { warehouseDtoList ->
                        return@withContext warehouseDtoList.map { warehouseDto ->
                            Warehouse(
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

    override suspend fun getWarehouseById(warehouseId: String): Warehouse {
        TODO("Not yet implemented")
    }

    override suspend fun registerWarehouse(warehouse: Warehouse): Warehouse {
        TODO("Not yet implemented")
    }

    override suspend fun updateWarehouse(warehouse: Warehouse): Warehouse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWarehouse(warehouseId: String) {
        TODO("Not yet implemented")
    }
}