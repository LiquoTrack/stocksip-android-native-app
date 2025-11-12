package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.Catalog
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.repositories.CatalogRepository
import com.liquotrack.stocksip.shared.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PendingCatalogItem(
    val productId: String,
    val warehouseId: String,
    val stock: Int
)

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val repository: CatalogRepository,
    private val tokenModel: TokenManager
) : ViewModel() {

    private val _catalogs = MutableStateFlow<List<Catalog>>(emptyList())
    val catalogs: StateFlow<List<Catalog>> = _catalogs.asStateFlow()

    private val _selectedCatalog = MutableStateFlow<Catalog?>(null)
    val selectedCatalog: StateFlow<Catalog?> = _selectedCatalog.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _pendingItems = MutableStateFlow<List<PendingCatalogItem>>(emptyList())
    val pendingItems: StateFlow<List<PendingCatalogItem>> = _pendingItems.asStateFlow()

    fun loadCatalogs() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _catalogs.value = repository.getAllCatalogs()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadCatalogsByAccount() {
        viewModelScope.launch {
            val accountId = tokenModel.getAccountId()
            accountId?.let {
                _catalogs.value = repository.getAllCatalogsByAccountId(it)
            }
        }
    }

    fun loadCatalogDetail(catalogId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _selectedCatalog.value = repository.getCatalogById(catalogId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createCatalog(name: String, description: String, contactEmail: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val accountId = tokenModel.getAccountId()
                if (accountId == null) {
                    _error.value = "Account ID not found"
                    _isLoading.value = false
                    return@launch
                }


                _pendingItems.value.forEach {
                    Log.d("CATALOG", "  - Product: ${it.productId}, Warehouse: ${it.warehouseId}, Stock: ${it.stock}")
                }

                val createdCatalog = repository.createCatalog(accountId, name, description, contactEmail)

                if (_pendingItems.value.isNotEmpty()) {

                    var successCount = 0
                    var failCount = 0

                    _pendingItems.value.forEach { item ->
                        try {

                            val updatedCatalog = repository.addCatalogItem(
                                createdCatalog.id,
                                item.productId,
                                item.warehouseId,
                                item.stock
                            )

                            successCount++
                        } catch (e: Exception) {
                            failCount++
                        }
                    }

                    clearPendingItems()
                } else {
                    Log.d("CATALOG", "No items to add to the new catalog.")
                }

                loadCatalogsByAccount()
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateCatalog(catalogId: String, name: String, description: String, contactEmail: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.updateCatalog(catalogId, name, description, contactEmail)
                loadCatalogsByAccount()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun publishCatalog(catalogId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.publishCatalog(catalogId)
                loadCatalogsByAccount()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun unpublishCatalog(catalogId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.unpublishCatalog(catalogId)
                loadCatalogsByAccount()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCatalogItem(catalogId: String, productId: String, warehouseId: String, stock: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val updatedCatalog = repository.addCatalogItem(catalogId, productId, warehouseId, stock)
                _selectedCatalog.value = updatedCatalog
                loadCatalogsByAccount()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addPendingItem(productId: String, warehouseId: String, stock: Int) {
        val newItem = PendingCatalogItem(productId, warehouseId, stock)
        val currentItems = _pendingItems.value.toMutableList()

        if (!currentItems.any { it.productId == productId }) {
            currentItems.add(newItem)
            _pendingItems.value = currentItems
            Log.d("CATALOG", "  ProductId: $productId")
            Log.d("CATALOG", "  WarehouseId: $warehouseId")
            Log.d("CATALOG", "  Stock: $stock")
            Log.d("CATALOG", "  Total pending items: ${currentItems.size}")
        } else {
            Log.d("CATALOG", "  Item with ProductId $productId already exists in pending items. Skipping addition.")
        }
    }

    fun removePendingItem(productId: String) {
        _pendingItems.value = _pendingItems.value.filter { it.productId != productId }
    }

    fun clearPendingItems() {
        _pendingItems.value = emptyList()
    }

    fun removeCatalogItem(catalogId: String, productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.removeCatalogItem(catalogId, productId)
                loadCatalogsByAccount()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setError(message: String) {
        _error.value = message
    }
}