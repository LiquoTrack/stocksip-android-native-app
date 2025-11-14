package com.liquotrack.stocksip.features.inventorymanagement.storage.data.remote.helpers

import com.liquotrack.stocksip.features.inventorymanagement.storage.domain.models.ProductRequest
import com.liquotrack.stocksip.shared.data.remote.helpers.toImageRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun ProductRequest.toMultipart(imageFile: File?): Pair<Map<String, RequestBody>, MultipartBody.Part?> {
    val fields = mapOf(
        "Name" to name.toRequestBody("text/plain".toMediaTypeOrNull()),
        "Type" to productType.toRequestBody("text/plain".toMediaTypeOrNull()),
        "Brand" to brand.toRequestBody("text/plain".toMediaTypeOrNull()),
        "UnitPrice" to unitPrice.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
        "Code" to currencyCode.toRequestBody("text/plain".toMediaTypeOrNull()),
        "Content" to content.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
        "MinimumStock" to minimumStock.toString().toRequestBody("text/plain".toMediaTypeOrNull())
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