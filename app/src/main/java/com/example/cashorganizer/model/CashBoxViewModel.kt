package com.example.cashorganizer.model

import java.io.Serializable

data class CashBoxViewModel(val incomeDay: Int, val incomeMonth: Int, val incomeYear: Int,
                            val incomeType: String, val categoryType: String, val incomeValue: Double,
                            var incomeDesc: String): Serializable