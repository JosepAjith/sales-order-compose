package com.joseph.salesorderapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    // Format: 2025-06-13 16:45:00
    fun getCurrentTimestamp(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(Date())
    }

    // Format: 13-06-2025 (only date)
    fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(Date())
    }

    fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    fun getCurrentTimeOnly(): String {
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return formatter.format(Date())
    }

}
