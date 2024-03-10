package com.example.cashorganizer.utilities

import android.graphics.Point
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
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

        fun validatePlanMoney(planMoneyName: String, isTarget: Boolean, valueTargetPlanMoney: String): String {
            var result = ""
            if (planMoneyName.isNullOrBlank()) result = "กรุณากรอกชื่อ PLAN MONEY"
            if (isTarget && valueTargetPlanMoney.isNullOrBlank()) result = "$result \nและ จำนวนเงินเงินที่ตั้งเป้าหมาย"
            return result
        }

        fun validateTransferCashBox(value: String, targetValue: String): String {
            var result = ""
            var valueTransfer = value.replace(",", "").toDouble()
            var valueCashBox = targetValue.replace(",", "").toDouble()
            if (valueTransfer > valueCashBox) result = "จำนวนเงินไม่พอ"
            return result
        }

        fun validateDateStatement(startDay: String, startMonth: String, startYear: String, endDay: String, endMonth: String, endYear: String): String {
            var result = ""
            if (startDay.isNullOrEmpty() || startMonth.isNullOrEmpty() || startYear.isNullOrEmpty() || endDay.isNullOrEmpty() ||
                endMonth.isNullOrEmpty() || endYear.isNullOrEmpty()) result = "กรุณาเลือกวันที่เริ่มต้น และวันที่สิ้นสุด"
            if (!startDay.isNullOrEmpty() || !startMonth.isNullOrEmpty() || !startYear.isNullOrEmpty() || !endDay.isNullOrEmpty() ||
                !endMonth.isNullOrEmpty() || !endYear.isNullOrEmpty()) {
                val startDate = SimpleDateFormat("dd-MM-yyyy").parse("$startDay-$startMonth-$startYear")
                val endDate = SimpleDateFormat("dd-MM-yyyy").parse("$endDay-$endMonth-$endYear")
                if (startDate > endDate && result.isNullOrEmpty()) result = "วันที่เริ่มต้น ต้องน้อยกว่าวันที่สิ้นสุด"
                else if (startDate > endDate && !result.isNullOrEmpty()) result = "$result \nและ วันที่เริ่มต้น ต้องน้อยกว่าวันที่สิ้นสุด"
            }
            return result
        }
    }

    object Function {
        @JvmStatic
        fun convertValueToFormatMoney(amount: String): String {
            amount?.replace(",", "")?.replace("฿", "")
            val formatter = DecimalFormat("###,###,##0.00")
            return formatter.format(amount.toDouble())
        }

        fun replaceFormatMoney(value: String): String {
            return value.replace(",", "").replace("฿", "")
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
                            .format(parsed / 100).replace("$", "").replace("฿", "")

                        current = formated
                        this@addCurrencyFormatter.setText(formated)
                        this@addCurrencyFormatter.setSelection(formated.length)
                        this@addCurrencyFormatter.addTextChangedListener(this)
                    }
                }
            })

        }

        fun findPositionAtPoint(point: Point, recyclerView: RecyclerView): Int {
            val location = IntArray(2)
            recyclerView.getLocationOnScreen(location)
            val child = recyclerView.findChildViewUnder(point.x.toFloat(), point.y.toFloat())
            return if (child !== null) recyclerView.getChildAdapterPosition(child)
            else {
                -1
            }
        }
    }
}