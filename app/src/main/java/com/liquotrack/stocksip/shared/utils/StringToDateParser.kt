package com.liquotrack.stocksip.shared.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Converts a date string in the format dd-MM-yyyy to a Date object.
 *
 * @param dateString The date string to convert.
 * @return The corresponding Date object, or null if the input string is invalid.
 */
fun stringToDate(dateString: String): Date? {
    return try {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        formatter.isLenient = false
        formatter.parse(dateString)
    } catch (e: Exception) {
        null
    }
}