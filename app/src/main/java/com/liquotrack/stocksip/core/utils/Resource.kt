package com.liquotrack.stocksip.core.utils

/**
 * A generic class that holds a value with its loading status.
 * @param <T> The type of data being handled.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Indicates the successful state of a resource.
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Indicates that there is no data available.
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}