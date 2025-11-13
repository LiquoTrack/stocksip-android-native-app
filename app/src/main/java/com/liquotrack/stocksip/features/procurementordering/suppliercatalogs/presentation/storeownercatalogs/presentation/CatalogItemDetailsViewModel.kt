package com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.presentation.storeownercatalogs.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liquotrack.stocksip.features.procurementordering.purchaseorders.domain.repositories.CartRepository
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.CatalogItem
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.repositories.CatalogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CartEvent {
    object Success : CartEvent()
    data class Error(val message: String) : CartEvent()
}

@HiltViewModel
class CatalogItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val catalogRepository: CatalogRepository,
    private val cartRepository: CartRepository
) : ViewModel() {


    private val productId: String = savedStateHandle["productId"] ?: ""
    val catalogId: String = savedStateHandle["catalogId"] ?: ""

    private val _catalogItem = MutableStateFlow<CatalogItem?>(null)
    val catalogItem: StateFlow<CatalogItem?> = _catalogItem.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _cartEvent = MutableSharedFlow<CartEvent>()
    val cartEvent = _cartEvent.asSharedFlow()

    init {
        loadItem()
    }

    private fun loadItem() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _catalogItem.value = catalogRepository.getCatalogItemById(catalogId, productId)
            } catch (e: Exception) {
                _catalogItem.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToCart(catalogItem: CatalogItem, quantity: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                cartRepository.addToCart(catalogItem, quantity)
                _cartEvent.emit(CartEvent.Success)
            } catch (e: Exception) {
                _cartEvent.emit(CartEvent.Error(e.message ?: "Error adding to cart"))
            } finally {
                _isLoading.value = false
            }
        }
    }
}
