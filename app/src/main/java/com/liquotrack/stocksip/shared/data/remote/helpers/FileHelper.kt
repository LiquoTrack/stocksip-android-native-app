package com.liquotrack.stocksip.shared.data.remote.helpers

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun File.toImageRequestBody(): RequestBody {
    val mimeType = when (extension.lowercase()) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "webp" -> "image/webp"
        else -> "application/octet-stream"
    }
    return asRequestBody(mimeType.toMediaType())
}
