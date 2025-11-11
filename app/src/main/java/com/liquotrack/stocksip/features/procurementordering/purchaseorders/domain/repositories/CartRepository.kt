package com.liquotrack.stocksip.features.procurementordering.purchaseorders.domain.repositories

import com.liquotrack.stocksip.features.procurementordering.purchaseorders.data.local.CartDao
import com.liquotrack.stocksip.features.procurementordering.purchaseorders.data.local.CartItemEntity
import com.liquotrack.stocksip.features.procurementordering.suppliercatalogs.domain.models.CatalogItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {

    fun getAllCartItems(): Flow<List<CartItemEntity>> = cartDao.getAllCartItems()

    fun getCartTotal(): Flow<Double> = cartDao.getCartTotal().map { it ?: 0.0 }

    suspend fun addToCart(catalogItem: CatalogItem, quantity: Int) {
        val existingItem = cartDao.getCartItemByProductId(catalogItem.productId)

        // Convertimos unitPrice a Double de forma segura
        val price: Double = when (val p = catalogItem.unitPrice) {
            is Number -> p.toDouble()
            is String -> p.replace("[^0-9.]".toRegex(), "").toDoubleOrNull() ?: 0.0
            else -> 0.0
        }

        if (existingItem != null) {
            val updatedItem = existingItem.copy(
                quantity = existingItem.quantity + quantity,
                subTotal = (existingItem.quantity + quantity) * price
            )
            cartDao.updateCartItem(updatedItem)
        } else {
            val newItem = CartItemEntity(
                productId = catalogItem.productId,
                productName = catalogItem.productName,
                unitPrice = price,
                imageUrl = catalogItem.imageUrl,
                quantity = quantity,
                subTotal = quantity * price
            )
            cartDao.insertCartItem(newItem)
        }
    }

    suspend fun updateQuantity(item: CartItemEntity, newQuantity: Int) {
        val price = item.unitPrice
        val updatedItem = item.copy(
            quantity = newQuantity,
            subTotal = newQuantity * price
        )
        cartDao.updateCartItem(updatedItem)
    }


    suspend fun removeFromCart(item: CartItemEntity) {
        cartDao.deleteCartItem(item)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }
}
