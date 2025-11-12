package com.liquotrack.stocksip.features.inventorymanagement.inventories.presentation.inventoryexitform

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class InventoryExitViewModel @Inject constructor() : ViewModel() {

    /**
     * Temporal list of products to showcase the screen styling.
     * Replace with real data source integration when available.
     */
    private val _products = MutableStateFlow(
        listOf(
            InventoryExitProductUi(
                id = "1",
                name = "Vino Blanco",
                imageUrl = "https://images.unsplash.com/photo-1527169402691-feff5539e52c?auto=format&fit=crop&w=160&q=80",
                quantity = 1
            ),
            InventoryExitProductUi(
                id = "2",
                name = "Vino Tinto",
                imageUrl = "https://images.unsplash.com/photo-1514361892635-6e1226204fcc?auto=format&fit=crop&w=160&q=80",
                quantity = 1
            ),
            InventoryExitProductUi(
                id = "3",
                name = "Whisky Blanco",
                imageUrl = "https://images.unsplash.com/photo-1504274066655-1ac2dd512651?auto=format&fit=crop&w=160&q=80",
                quantity = 1
            )
        )
    )
    val products: StateFlow<List<InventoryExitProductUi>> = _products.asStateFlow()

    fun increaseQuantity(productId: String) {
        _products.update { list ->
            list.map { product ->
                if (product.id == productId) product.copy(quantity = product.quantity + 1) else product
            }
        }
    }

    fun decreaseQuantity(productId: String) {
        _products.update { list ->
            list.map { product ->
                if (product.id == productId && product.quantity > 1) {
                    product.copy(quantity = product.quantity - 1)
                } else {
                    product
                }
            }
        }
    }
}

data class InventoryExitProductUi(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val quantity: Int
)