package com.liquotrack.stocksip.features.procurementordering.purchaseorders.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM shopping_cart ORDER BY addedAt DESC")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM shopping_cart WHERE productId = :productId LIMIT 1")
    suspend fun getCartItemByProductId(productId: String): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity)

    @Update
    suspend fun updateCartItem(item: CartItemEntity)

    @Delete
    suspend fun deleteCartItem(item: CartItemEntity)

    @Query("DELETE FROM shopping_cart")
    suspend fun clearCart()

    @Query("SELECT SUM(subTotal) FROM shopping_cart")
    fun getCartTotal(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM shopping_cart")
    fun getCartItemCount(): Flow<Int>
}