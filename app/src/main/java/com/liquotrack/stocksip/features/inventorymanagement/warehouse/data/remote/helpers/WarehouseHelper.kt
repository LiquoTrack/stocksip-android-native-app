package com.liquotrack.stocksip.features.inventorymanagement.warehouse.data.remote.helpers

import com.liquotrack.stocksip.features.inventorymanagement.warehouse.domain.models.WarehouseRequest
import com.liquotrack.stocksip.shared.data.remote.helpers.toImageRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MultipartBody
import java.io.File

fun WarehouseRequest.toMultipart(imageFile: File?): Pair<Map<String, RequestBody>, MultipartBody.Part?> {
    val fields = mapOf(
        "Name" to name.toRequestBody("text/plain".toMediaTypeOrNull()),
        "AddressStreet" to street.toRequestBody("text/plain".toMediaTypeOrNull()),
        "AddressCity" to city.toRequestBody("text/plain".toMediaTypeOrNull()),
        "AddressDistrict" to district.toRequestBody("text/plain".toMediaTypeOrNull()),
        "AddressPostalCode" to postalCode.toRequestBody("text/plain".toMediaTypeOrNull()),
        "AddressCountry" to country.toRequestBody("text/plain".toMediaTypeOrNull()),
        "Capacity" to capacity.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
        "TemperatureMin" to temperatureMin.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
        "TemperatureMax" to temperatureMax.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    )

    val imagePart = imageFile?.let { file ->
        MultipartBody.Part.createFormData(
            "image",
            file.name,
            file.toImageRequestBody()
        )
    }

    return Pair(fields, imagePart)
}
