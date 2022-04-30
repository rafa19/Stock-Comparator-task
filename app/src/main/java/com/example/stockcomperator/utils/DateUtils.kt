package com.example.stockcomperator.utils

import java.text.SimpleDateFormat
import java.util.*

open class DateUtils {
    private val dateMonthFormat = SimpleDateFormat("dd/MM", Locale.getDefault())

    fun getDateTime(timestamp: Long): String {
        return try {
            val netDate = Date(timestamp * 1000)
            dateMonthFormat.format(netDate)
        } catch (e: Exception) {
            ""
        }
    }
}