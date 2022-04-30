package com.example.stockcomperator.utils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class DateAxisFormatter : IndexAxisValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val index = value.toLong()
        return DateUtils().getDateTime(index)
    }
}