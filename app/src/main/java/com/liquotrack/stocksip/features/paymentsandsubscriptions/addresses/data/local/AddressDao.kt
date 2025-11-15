package com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {

    @Query("SELECT * FROM addresses")
    fun getAllAddresses(): Flow<List<AddressEntity>>

    @Query("SELECT * FROM addresses")
    suspend fun getAllAddressesOnce(): List<AddressEntity>

    @Query("SELECT * FROM addresses WHERE id = :id")
    suspend fun getAddressById(id: Int): AddressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity): Long

    @Update
    suspend fun updateAddress(address: AddressEntity)

    @Delete
    suspend fun deleteAddress(address: AddressEntity)

    @Query("DELETE FROM addresses WHERE id = :id")
    suspend fun deleteAddressById(id: Int)

    @Query("DELETE FROM addresses")
    suspend fun deleteAllAddresses()

    @Query("SELECT COUNT(*) FROM addresses")
    suspend fun getAddressCount(): Int
}