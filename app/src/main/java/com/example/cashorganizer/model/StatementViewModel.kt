package com.example.cashorganizer.model

import java.io.Serializable

data class StatementViewModel(val incomeDay: Int, val incomeMonth: Int, val incomeYear: Int,
                              val incomeType: String, val categoryType: String, val incomeValue: Double,
                              var incomeDesc: String, var type: String, var expensesValue: Double, var balance: Double): Serializable