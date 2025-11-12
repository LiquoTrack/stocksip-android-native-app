package com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.remote.services

import com.liquotrack.stocksip.features.paymentsandsubscriptions.addresses.data.remote.models.AddressDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AddressService {
    @GET("accounts/{accountId}/addresses")
    suspend fun getAddresses(@Path("accountId") accountId: String): Response<List<AddressDto>>

    @POST("accounts/{accountId}/addresses")
    suspend fun addAddress(
        @Path("accountId") accountId: String,
        @Body address: AddressDto
    ): Response<AddressDto>
}