package com.client.cricketfestivalplanner.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {

    private val displayFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val storageFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun formatForDisplay(dateString: String): String {
        return try {
            val date = storageFormat.parse(dateString)
            date?.let { displayFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }

    fun formatForStorage(date: Date): String {
        return storageFormat.format(date)
    }

    fun getCurrentDateForStorage(): String {
        return storageFormat.format(Date())
    }

    fun isValidDate(dateString: String): Boolean {
        return try {
            storageFormat.isLenient = false
            storageFormat.parse(dateString) != null
        } catch (e: Exception) {
            false
        }
    }
}