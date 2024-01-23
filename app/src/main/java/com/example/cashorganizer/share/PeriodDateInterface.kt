package com.example.cashorganizer.share

interface PeriodDateInterface {
    fun transferCurrentDate(currentDay: String, currentMonth: Int, currentYear: String)
    fun transferConfirmCurrentDate(currentDay: String, currentMonth: Int, currentYear: String)
}