package com.example.cashorganizer.utilities

import java.text.SimpleDateFormat

class UtilFunction {
    object Validation {
        @JvmStatic
        fun validateStartAndEndCustomDate(startDay: String, startMonth: Int, startYear: String,
                                          endDay: String, endMonth: Int, endYear: String): Boolean {
            if ((startDay != "" && startMonth != -1 && startYear != "") && (endDay != "" && endMonth != -1 && endYear != "")) {
                val startDate = SimpleDateFormat("dd-MM-yyyy").parse("$startDay-$startMonth-$startYear")
                val endDate = SimpleDateFormat("dd-MM-yyyy").parse("$endDay-$endMonth-$endYear")
                if (startDate > endDate) return true
            }
            else { return false }
            return false
        }

        fun validateSubmitCustomDate(startDay: String, startMonth: Int, startYear: String,
                                     endDay: String, endMonth: Int, endYear: String) : Boolean{
            return (startDay == "" && startMonth == -1 && startYear == "") && (endDay == "" && endMonth == -1 && endYear == "")
        }
    }
}