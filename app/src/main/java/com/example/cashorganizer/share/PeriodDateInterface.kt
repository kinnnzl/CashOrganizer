package com.example.cashorganizer.share

interface PeriodDateInterface {
    fun transferCurrentDate(currentDay: String, currentMonth: Int, currentYear: String)
    fun transferCustomDate()
    fun transferConfirmDate(currentDay: String, currentMonth: Int, currentYear: String, endDay: String, endMonth: Int, endYear: String)
    fun transferCustomDate(startDay: String, startMonth: Int, startYear: String, endDay: String, endMonth: Int, endYear: String, isStartDate: Boolean)
    fun transferEnterCustomDate(startDay: String, startMonth: Int, startYear: String, endDay: String, endMonth: Int, endYear: String)
}