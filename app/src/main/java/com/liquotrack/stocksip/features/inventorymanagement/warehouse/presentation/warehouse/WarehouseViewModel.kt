package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseResponse
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseRequest
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehousesWithCount
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

    private val _warehouses = MutableStateFlow<WarehousesWithCount?>(null)
    val warehouses: StateFlow<WarehousesWithCount?> = _warehouses.asStateFlow()

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

    private val _temperatureError = MutableStateFlow<String?>(null)
    val temperatureError: StateFlow<String?> = _temperatureError.asStateFlow()

    private val _isMaxReached = MutableStateFlow(false)
    val isMaxReached = _isMaxReached.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    fun updateWarehouseName(value: String) { _warehouseName.value = value }
    fun updateStreet(value: String) { _street.value = value }
    fun updateCity(value: String) { _city.value = value }
    fun updateDistrict(value: String) { _district.value = value }
    fun updatePostalCode(value: String) { _postalCode.value = value }
    fun updateCountry(value: String) { _country.value = value }
    fun updateCapacity(value: Double) { _capacity.value = value }
    fun updateMinTemp(value: Double) {
        _minTemp.value = value
        validateTemperature()
    }
    fun updateMaxTemp(value: Double) {
        _maxTemp.value = value
        validateTemperature()
    }
    fun updateImageFile(file: File?) { _imageFile.value = file }

    fun clearTemperatureError() { _temperatureError.value = null }

    /**
     * Loads the warehouse data into the form for editing.
     * @param warehouse The [WarehouseResponse] object containing the warehouse data to be edited.
     */
    fun loadWarehouseForEdit(warehouse: WarehouseResponse) {
        _isLoading.value = true
        _editingWarehouse.value = warehouse
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

    fun showDeleteConfirmationDialog(show: Boolean) {
        _showDeleteDialog.value = show
    }

    /**
     * Clears the warehouse form fields.
     */
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

    /**
     * Saves the warehouse data. If editing an existing warehouse, it updates it; otherwise, it creates a new one.
     * @param onSuccess A lambda function to be called upon successful save operation.
     */
    fun saveWarehouse(
        isEditing: Boolean,
        warehouseId: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val accountId =
                    tokenModel.getAccountId() ?: throw Exception("Account ID not found")

                val imageFile = _imageFile.value

                val warehouseRequest = WarehouseRequest(
                    name = _warehouseName.value,
                    street = _street.value,
                    city = _city.value,
                    district = _district.value,
                    postalCode = _postalCode.value,
                    country = _country.value,
                    capacity = _capacity.value,
                    temperatureMin = _minTemp.value,
                    temperatureMax = _maxTemp.value
                )

                if (isEditing && warehouseId != null) {

                    val updated = repository.updateWarehouse(
                        warehouseRequest,
                        warehouseId,
                        imageFile
                    )

                    _warehouses.value = _warehouses.value?.copy(
                        warehouses = _warehouses.value?.warehouses.orEmpty().map {
                            if (it.id == updated.id) updated else it
                        }
                    )

                } else {
                    val created = repository.registerWarehouse(
                        warehouseRequest,
                        accountId,
                        imageFile
                    )

                    _warehouses.value = _warehouses.value?.copy(
                        warehouses = _warehouses.value?.warehouses.orEmpty() + created
                    )
                }

                clearForm()
                onSuccess()

            } catch (e: Exception) {
                Log.e("WAREHOUSE", "Error saving warehouse", e)
            }
        }
    }


    /**
     * Fetches a warehouse by its ID and updates the selectedWarehouse state.
     * @param id The ID of the warehouse to be fetched.
     */
    fun getWarehouseById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val warehouse = repository.getWarehouseById(id)
            _selectedWarehouse.value = warehouse
            _isLoading.value = false
        }
    }

    /**
     * Fetches all warehouses associated with the current account ID and updates the warehouses state.
     */
    fun getAllWarehousesByAccountId() {
        viewModelScope.launch {
            val accountId = tokenModel.getAccountId()
            accountId?.let {
                _warehouses.value = repository.getAllWarehousesByAccountId(it)
            }
            Log.d("WAREHOUSE", "Received: ${_warehouses.value}")
        }

    }

    /**
     * Deletes the currently editing warehouse by its ID and updates the warehouses state.
     */
    fun deleteWarehouseById(warehouseId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.deleteWarehouse(warehouseId)

                _warehouses.value = _warehouses.value?.copy(
                    warehouses = _warehouses.value?.warehouses.orEmpty().filter { it.id != warehouseId }
                )

                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Validates the temperature inputs to ensure minimum temperature is less than maximum temperature.
     * @return True if the temperature inputs are valid, false otherwise.
     */
    private fun validateTemperature(): Boolean {
        return if (_minTemp.value >= _maxTemp.value) {
            _temperatureError.value = "Minimum temperature must be less than maximum temperature"
            false
        } else {
            _temperatureError.value = null
            true
        }
    }

    /**
     * Validates if the maximum number of warehouses has been reached.
     * @return True if the current number of warehouses is less than the maximum allowed, false otherwise.
     */
    fun validateMaxWarehouses() {
        val data = _warehouses.value
        if (data == null) {
            _isMaxReached.value = false
            return
        }

        val currentCount = data.total
        val maxAllowed = data.maxWarehousesAllowed

        _isMaxReached.value = currentCount >= maxAllowed
    }

    /**
     * Sets the warehouse to be edited.
     */
    init {
        getAllWarehousesByAccountId()
    }
}