package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseResponse
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseRequest
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.repositories.WarehouseRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * ViewModel for managing Warehouse data and operations.
 * This ViewModel interacts with the WarehouseRepository to fetch and manage warehouse information.
 * It also utilizes TokenManager to handle authentication tokens.
 */
@HiltViewModel
class WarehouseViewModel @Inject constructor(
    private val repository: WarehouseRepository,
    private val tokenModel: TokenManager
) : ViewModel() {

    private val _warehouses = MutableStateFlow<List<WarehouseResponse>>(emptyList())
    val warehouses: StateFlow<List<WarehouseResponse>> = _warehouses


    private val _warehouseName = MutableStateFlow("")
    val warehouseName: StateFlow<String> = _warehouseName

    private val _street = MutableStateFlow("")
    val street: StateFlow<String> = _street

    private val _city = MutableStateFlow("")
    val cityState: StateFlow<String> = _city

    private val _district = MutableStateFlow("")
    val district: StateFlow<String> = _district

    private val _postalCode = MutableStateFlow("")
    val postalCode: StateFlow<String> = _postalCode

    private val _country = MutableStateFlow("")
    val country: StateFlow<String> = _country

    private val _capacity = MutableStateFlow(0.0)
    val capacity: StateFlow<Double> = _capacity

    private val _minTemp = MutableStateFlow(0.0)
    val minTemp: StateFlow<Double> = _minTemp

    private val _maxTemp = MutableStateFlow(0.0)
    val maxTemp: StateFlow<Double> = _maxTemp

    private val _imageFile = MutableStateFlow<File?>(null)
    val imageFile: StateFlow<File?> = _imageFile

    private val _imageUrl = MutableStateFlow("")
    val imageUrl: StateFlow<String> = _imageUrl

    private val _selectedWarehouse = MutableStateFlow<WarehouseResponse?>(null)
    val selectedWarehouse: StateFlow<WarehouseResponse?> = _selectedWarehouse

    private val _editingWarehouse = MutableStateFlow<WarehouseResponse?>(null)
    val editingWarehouse: StateFlow<WarehouseResponse?> = _editingWarehouse

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun updateWarehouseName(value: String) { _warehouseName.value = value }
    fun updateStreet(value: String) { _street.value = value }
    fun updateCity(value: String) { _city.value = value }
    fun updateDistrict(value: String) { _district.value = value }
    fun updatePostalCode(value: String) { _postalCode.value = value }
    fun updateCountry(value: String) { _country.value = value }
    fun updateCapacity(value: Double) { _capacity.value = value }
    fun updateMinTemp(value: Double) { _minTemp.value = value }
    fun updateMaxTemp(value: Double) { _maxTemp.value = value }
    fun updateImageFile(file: File?) { _imageFile.value = file }


    fun loadWarehouseForEdit(warehouse: WarehouseResponse) {
        _isLoading.value = true
        _warehouseName.value = warehouse.name
        _street.value = warehouse.street
        _city.value = warehouse.city
        _district.value = warehouse.district
        _postalCode.value = warehouse.postalCode
        _country.value = warehouse.country
        _capacity.value = warehouse.capacity
        _minTemp.value = warehouse.temperatureMin
        _maxTemp.value = warehouse.temperatureMax
        _imageFile.value = null
        _isLoading.value = false
    }

    fun clearForm() {
        _editingWarehouse.value = null
        _warehouseName.value = ""
        _street.value = ""
        _city.value = ""
        _district.value = ""
        _postalCode.value = ""
        _country.value = ""
        _capacity.value = 0.0
        _minTemp.value = 0.0
        _maxTemp.value = 0.0
        _imageFile.value = null
        _imageUrl.value = ""
    }

    fun saveWarehouse(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {

                val accountId = tokenModel.getAccountId() ?: throw Exception("Account ID not found")

                val editingWarehouse = _editingWarehouse.value
                val imageFile = _imageFile.value


                if (editingWarehouse != null) {
                    val updatedWarehouse = WarehouseRequest(
                        name = _warehouseName.value,
                        street = _street.value,
                        city = _city.value,
                        district = _district.value,
                        postalCode = _postalCode.value,
                        country = _country.value,
                        capacity = _capacity.value,
                        temperatureMin = _minTemp.value,
                        temperatureMax = _maxTemp.value,
                    )

                    // TODO: Implement updateWarehouse in the repository

                } else {


                    val newWarehouse = WarehouseRequest(
                        name = _warehouseName.value,
                        street = _street.value,
                        city = _city.value,
                        district = _district.value,
                        postalCode = _postalCode.value,
                        country = _country.value,
                        capacity = _capacity.value,
                        temperatureMin = _minTemp.value,
                        temperatureMax = _maxTemp.value,
                    )


                    val createdWarehouse = repository.registerWarehouse(newWarehouse, accountId, imageFile)

                    _warehouses.value = _warehouses.value + createdWarehouse
                }

                clearForm()
                onSuccess()

            } catch (e: Exception) {
                Log.e("WAREHOUSE", "Error saving warehouse", e)
            }
        }
    }

    fun getWarehouseById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val warehouse = repository.getWarehouseById(id)
            _selectedWarehouse.value = warehouse
            _isLoading.value = false
        }
    }

    fun getAllWarehousesByAccountId() {
        viewModelScope.launch {
            val accountId = tokenModel.getAccountId()
            accountId?.let {
                _warehouses.value = repository.getAllWarehousesByAccountId(it)
            }
            Log.d("WAREHOUSE", "Received: ${_warehouses.value}")
        }

    }

    init {
        getAllWarehousesByAccountId()
    }
}