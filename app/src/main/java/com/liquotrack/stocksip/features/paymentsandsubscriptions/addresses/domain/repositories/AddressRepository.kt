package com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.domain.repositories

import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.remote.models.AddressDto
import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.remote.models.AddressRequestDto

interface AddressRepository {
    suspend fun getAddresses(accountId: String): Result<List<AddressDto>>
    suspend fun addAddress(accountId: String, address: AddressRequestDto): Result<AddressDto>
}
