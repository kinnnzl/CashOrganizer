package com.example.cashorganizer.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date

class PeriodDateViewModel : ViewModel() {
    val currentDay = MutableLiveData<String>()
    val currentMonth = MutableLiveData<Int>()
    val currentYear = MutableLiveData<String>()

    fun setCurrentDay(day: String) {
        currentDay.value = day
    }
    fun setCurrentMonth(month: Int) {
        currentMonth.value = month
    }

    fun setCurrentYear(year: String) {
        currentYear.value = year
    }
}