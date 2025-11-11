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

    // Lista temporal de items a agregar cuando se cree el catálogo
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

    /**
     * Crea un catálogo y agrega los items pendientes si existen
     */
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

                Log.d("CATALOG", "=== INICIANDO CREACIÓN DE CATÁLOGO ===")
                Log.d("CATALOG", "Account ID: $accountId")
                Log.d("CATALOG", "Pending items: ${_pendingItems.value.size}")
                _pendingItems.value.forEach {
                    Log.d("CATALOG", "  - Product: ${it.productId}, Warehouse: ${it.warehouseId}, Stock: ${it.stock}")
                }

                // Crear el catálogo
                val createdCatalog = repository.createCatalog(accountId, name, description, contactEmail)
                Log.d("CATALOG", "✅ Catálogo creado con ID: ${createdCatalog.id}")

                // Si hay items pendientes, agregarlos al catálogo recién creado
                if (_pendingItems.value.isNotEmpty()) {
                    Log.d("CATALOG", "Agregando ${_pendingItems.value.size} items al catálogo ${createdCatalog.id}")

                    var successCount = 0
                    var failCount = 0

                    _pendingItems.value.forEach { item ->
                        try {
                            Log.d("CATALOG", "Agregando item: productId=${item.productId}, warehouseId=${item.warehouseId}, stock=${item.stock}")

                            val updatedCatalog = repository.addCatalogItem(
                                createdCatalog.id,
                                item.productId,
                                item.warehouseId,
                                item.stock
                            )

                            successCount++
                            Log.d("CATALOG", "✅ Item agregado. Catálogo ahora tiene ${updatedCatalog.catalogItems.size} items")
                        } catch (e: Exception) {
                            failCount++
                            Log.e("CATALOG", "❌ Error agregando item ${item.productId}: ${e.message}", e)
                        }
                    }

                    Log.d("CATALOG", "=== RESUMEN ===")
                    Log.d("CATALOG", "Items agregados exitosamente: $successCount")
                    Log.d("CATALOG", "Items fallidos: $failCount")

                    // Limpiar items pendientes después de agregarlos
                    clearPendingItems()
                } else {
                    Log.d("CATALOG", "No hay items pendientes para agregar")
                }

                loadCatalogsByAccount()
                Log.d("CATALOG", "=== PROCESO COMPLETADO ===")
            } catch (e: Exception) {
                Log.e("CATALOG", "❌ Error creando catálogo", e)
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

    /**
     * Agrega un item al catálogo (modo edición)
     */
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

    /**
     * Agrega un item a la lista pendiente (modo creación)
     */
    fun addPendingItem(productId: String, warehouseId: String, stock: Int) {
        val newItem = PendingCatalogItem(productId, warehouseId, stock)
        val currentItems = _pendingItems.value.toMutableList()

        // Evitar duplicados
        if (!currentItems.any { it.productId == productId }) {
            currentItems.add(newItem)
            _pendingItems.value = currentItems
            Log.d("CATALOG", "✅ Item pendiente agregado:")
            Log.d("CATALOG", "  ProductId: $productId")
            Log.d("CATALOG", "  WarehouseId: $warehouseId")
            Log.d("CATALOG", "  Stock: $stock")
            Log.d("CATALOG", "  Total pending items: ${currentItems.size}")
        } else {
            Log.d("CATALOG", "⚠️ Item $productId ya existe en pending items")
        }
    }

    /**
     * Remueve un item de la lista pendiente
     */
    fun removePendingItem(productId: String) {
        _pendingItems.value = _pendingItems.value.filter { it.productId != productId }
    }

    /**
     * Limpia la lista de items pendientes
     */
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