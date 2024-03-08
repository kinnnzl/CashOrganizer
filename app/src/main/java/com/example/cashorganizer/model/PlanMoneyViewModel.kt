package com.example.cashorganizer.model

import java.io.Serializable

data class PlanMoneyViewModel(val planMoneyName: String, var amount: Double, val isTarget: Boolean,
                              val amountTarget: Double, val planMoneyType: String): Serializable