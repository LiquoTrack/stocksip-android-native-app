package com.liquotrack.stocksip.features.inventorymanagement.warehouse.presentation.warehouse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.Warehouse
import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.repositories.WarehouseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WarehouseViewModel @Inject constructor(private val repository: WarehouseRepository) : ViewModel() {

    private val _warehouses = MutableStateFlow<List<Warehouse>>(emptyList())
    val warehouses: StateFlow<List<Warehouse>> = _warehouses

    fun getAlWarehousesByAccountId() {
        viewModelScope.launch {
            _warehouses.value = repository.getAllByAccountIdWarehouses("68e49ffad906e587b9a91e4b")
            Log.d("WAREHOUSE", "Received: ${_warehouses.value}" )
        }
    }

    init {
        getAlWarehousesByAccountId()
    }
}