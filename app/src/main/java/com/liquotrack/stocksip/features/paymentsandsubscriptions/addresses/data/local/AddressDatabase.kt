package com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AddressEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AddressDatabase : RoomDatabase() {
    abstract fun addressDao(): AddressDao

    companion object {
        const val DATABASE_NAME = "address_database"
    }
}