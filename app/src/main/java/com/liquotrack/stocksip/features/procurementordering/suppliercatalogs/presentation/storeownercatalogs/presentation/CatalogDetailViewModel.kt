package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation.storeownercatalogs.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.Catalog
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.SupplierInfo
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.repositories.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogDetailViewModel @Inject constructor(
    private val repository: CatalogRepository
) : ViewModel() {

    private val _catalog = MutableStateFlow<Catalog?>(null)
    val catalog: StateFlow<Catalog?> = _catalog.asStateFlow()

    private val _supplierInfo = MutableStateFlow<SupplierInfo?>(null)
    val supplierInfo: StateFlow<SupplierInfo?> = _supplierInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadCatalog(catalogId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // 1️⃣ Obtener catálogo
                val catalogResult = repository.getCatalogById(catalogId)
                _catalog.value = catalogResult

                // 2️⃣ Obtener el proveedor (con toda la info del negocio)
                val supplierResult = repository.getSupplierById(catalogResult.ownerAccount)
                _supplierInfo.value = supplierResult

            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
