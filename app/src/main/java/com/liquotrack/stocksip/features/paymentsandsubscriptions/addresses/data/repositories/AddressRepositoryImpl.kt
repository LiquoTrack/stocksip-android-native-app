package com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.repositories

import android.util.Log
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.local.AddressDao
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.local.AddressEntity
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.remote.models.AddressDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.remote.models.AddressRequestDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.remote.services.AddressService
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.domain.repositories.AddressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val addressDao: AddressDao,
    private val addressService: AddressService
) : AddressRepository {
    override suspend fun getAddresses(accountId: String): Result<List<AddressDto>> = runCatching {
        withContext(Dispatchers.IO) {
            val entities = addressDao.getAllAddressesOnce()
            entities.map { entity ->
                AddressDto(
                    id = entity.id,
                    street = entity.street,
                    city = entity.city,
                    state = entity.state,
                    country = entity.country,
                    zipCode = entity.zipCode
                )
            }
        }
    }

    override suspend fun addAddress(
        accountId: String,
        address: AddressRequestDto
    ): Result<AddressDto> = runCatching {
        withContext(Dispatchers.IO) {
            val response = addressService.addAddress(
                accountId = accountId,
                address = AddressDto(
                    street = address.street,
                    city = address.city,
                    state = address.state,
                    country = address.country,
                    zipCode = address.zipCode
                )
            )

            if (!response.isSuccessful) {
                throw Exception("Error adding address: ${response.code()} ${response.message()}")
            }

            val savedAddress = response.body() ?: AddressDto(
                id = 0,
                street = address.street,
                city = address.city,
                state = address.state,
                country = address.country,
                zipCode = address.zipCode
            )


            val entity = AddressEntity(
                street = savedAddress.street,
                city = savedAddress.city,
                state = savedAddress.state,
                country = savedAddress.country,
                zipCode = savedAddress.zipCode
            )
            addressDao.insertAddress(entity)

            savedAddress
        }
    }

}
