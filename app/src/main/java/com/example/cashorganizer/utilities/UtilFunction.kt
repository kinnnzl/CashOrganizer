package com.example.cashorganizer.utilities

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat
import java.text.NumberFormat
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

        fun validateSaveIncome(incomeDay: String, incomeMonth: Int, incomeYear: String, incomeType: String?, categoryType: String?, valueAmount: String): Boolean {
            var result = false
            if ((incomeDay == "" && incomeMonth == -1 && incomeYear == "") || incomeType == "" || categoryType == "") result = true
            if (valueAmount == "" || valueAmount == "0.00") return true
            return result
        }

        fun convertValueToFormatMoney(amount: String): String {
            val formatter = DecimalFormat("###,###,##0.00")
            return formatter.format(amount.toDouble())
        }

        fun EditText.addCurrencyFormatter() {
            this.addTextChangedListener(object: TextWatcher {
                private var current = ""

                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    if (s.toString() != current) {
                        this@addCurrencyFormatter.removeTextChangedListener(this)
                        val cleanString = s.toString().replace("\\D".toRegex(), "")
                        val parsed = if (cleanString.isBlank()) 0.0 else cleanString.toDouble()
                        val formated = NumberFormat.getCurrencyInstance()
                            .format(parsed / 100).replace("$", "")

                        current = formated
                        this@addCurrencyFormatter.setText(formated)
                        this@addCurrencyFormatter.setSelection(formated.length)
                        this@addCurrencyFormatter.addTextChangedListener(this)
                    }
                }
            })

        }
    }
}