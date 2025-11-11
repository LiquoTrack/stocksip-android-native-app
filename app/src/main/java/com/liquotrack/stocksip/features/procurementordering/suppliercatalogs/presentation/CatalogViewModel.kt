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
                    return@launch
                }

                Log.d("CATALOG", "Creando catálogo con accountId: $accountId")

                repository.createCatalog(accountId, name, description, contactEmail)
                loadCatalogsByAccount() // ✅ refresca solo los del usuario
            } catch (e: Exception) {
                Log.e("CATALOG", "Error creando catálogo", e)
                _error.value = e.message ?: "Error desconocido"
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
                loadCatalogs()
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
                loadCatalogs()
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
                loadCatalogs()
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
                loadCatalogs()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeCatalogItem(catalogId: String, productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.removeCatalogItem(catalogId, productId)
                loadCatalogs()
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
