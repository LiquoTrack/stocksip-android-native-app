package com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.remote.models.AddressDto

@Entity(tableName = "addresses")
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "street")
    val street: String?,

    @ColumnInfo(name = "city")
    val city: String?,

    @ColumnInfo(name = "state")
    val state: String? = null,

    @ColumnInfo(name = "country")
    val country: String? = null,

    @ColumnInfo(name = "zip_code")
    val zipCode: String? = null,
)

fun AddressEntity.toDto(): AddressDto {
    return AddressDto(
        id = id,
        street = street,
        city = city,
        state = state,
        country = country,
        zipCode = zipCode
    )
}

fun AddressDto.toEntity(id: Int = 0): AddressEntity {
    return AddressEntity(
        id = id,
        street = street ?: "",
        city = city ?: "",
        state = state,
        country = country,
        zipCode = zipCode
    )
}