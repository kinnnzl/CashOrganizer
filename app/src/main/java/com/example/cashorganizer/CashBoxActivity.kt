package com.example.cashorganizer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.example.cashorganizer.Fragment.DateIncomeFragment
import com.example.cashorganizer.databinding.ActivityCashBoxBinding
import com.example.cashorganizer.share.IncomeDateInterface
import com.example.cashorganizer.utilities.RequestCode
import com.example.cashorganizer.utilities.UtilFunction
import com.example.cashorganizer.utilities.UtilFunction.Validation.addCurrencyFormatter
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class CashBoxActivity : AppCompatActivity(), IncomeDateInterface {
    private lateinit var binding: ActivityCashBoxBinding
    private lateinit var icClose: AppCompatImageView
    private lateinit var txtCashBoxPeriodDate: TextView
    private lateinit var txtCashBoxCategory: TextView
    private lateinit var cardMoneyBag: MaterialCardView
    private lateinit var btnIncomeType: TextView
    private lateinit var icSelectCalendar: AppCompatImageView
    private lateinit var dialogDatePeriod: DateIncomeFragment
    private lateinit var btnSaveIncome: MaterialButton
    private lateinit var valueAmountCashBox: EditText
    private lateinit var incomeDesc: EditText
    private var incomeType: String? = ""
    private var categoryType: String? = ""
    private var incomeDay: String = ""
    private var incomeMonth: Int = -1
    private var incomeYear: String = ""
    private val fullMonth: List<String> = listOf("มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCashBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupValue()
        setupClickListeners()
        setupOnChangeListeners()
    }

    private fun setupView() {
        icClose = findViewById(R.id.icClose)
        cardMoneyBag = findViewById(R.id.cardMoneyBag)
        txtCashBoxCategory = findViewById(R.id.txtCashBoxCategory)
        txtCashBoxPeriodDate = findViewById(R.id.txtCashBoxPeriodDate)
        btnIncomeType = findViewById(R.id.btnIncomeType)
        icSelectCalendar = findViewById(R.id.icSelectCalendar)
        btnSaveIncome = findViewById(R.id.btnSaveIncome)
        valueAmountCashBox = findViewById(R.id.valueAmountCashBox)
        incomeDesc = findViewById(R.id.incomeDesc)
        dialogDatePeriod = DateIncomeFragment()
        valueAmountCashBox.addCurrencyFormatter()
    }

    private fun setupValue() {

    }

    private fun setupClickListeners() {
        icClose.setOnClickListener {
            finish()
        }

        txtCashBoxPeriodDate.setOnClickListener {
            showDialogDateIncomePicker()
        }

        icSelectCalendar.setOnClickListener {
            showDialogDateIncomePicker()
        }

        btnIncomeType.setOnClickListener {
            val intent = Intent(this, IncomeTypeActivity::class.java)
            startActivityForResult(intent, RequestCode.INCOME_TYPE)
        }

        cardMoneyBag.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivityForResult(intent, RequestCode.CATEGORY_TYPE)
        }

        txtCashBoxCategory.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivityForResult(intent, RequestCode.CATEGORY_TYPE)
        }

        btnSaveIncome.setOnClickListener {
            if (UtilFunction.Validation.validateSaveIncome(incomeDay, incomeMonth, incomeYear, incomeType, categoryType, valueAmountCashBox.text.toString())) {
                Toast.makeText(this, "กรุณาเลือกวันที่รับรายได้, หมวดหมู่, จำนวนเงิน และประเภทรายรับ!", Toast.LENGTH_SHORT).show()
            }
            else {
                val intent = Intent()
                intent.putExtra("IncomeDay", incomeDay)
                intent.putExtra("IncomeMonth", incomeMonth)
                intent.putExtra("IncomeYear", incomeYear)
                intent.putExtra("IncomeType", incomeType)
                intent.putExtra("CategoryType", categoryType)
                intent.putExtra("IncomeValue", valueAmountCashBox.text.toString())
                intent.putExtra("IncomeDesc", incomeDesc.text.toString())
                setResult(RESULT_OK, intent);
                finish()
            }
        }
    }

    private fun setupOnChangeListeners() {
//        valueAmountCashBox.doAfterTextChanged {
//            valueAmountCashBox.setText(convertValueToFormatMoney(valueAmountCashBox.text.toString()))
//        }
    }

    private fun showDialogDateIncomePicker() {
        dialogDatePeriod.show(supportFragmentManager, "dialogDatePeriod")
    }

    override fun transferIncomeDate(day: String, month: Int, year: String) {
        incomeDay = day
        incomeMonth = month
        incomeYear = year
        txtCashBoxPeriodDate.text = "$day ${fullMonth[month!!]} $year"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.INCOME_TYPE) {
            if (resultCode == Activity.RESULT_OK) {
                val dataIncomeType : String? = data?.getStringExtra("incomeType")
                incomeType = dataIncomeType
                btnIncomeType.text = "$dataIncomeType >"
            }
        }
        else if (requestCode == RequestCode.CATEGORY_TYPE) {
            if (resultCode == Activity.RESULT_OK) {
                val dataCategory : String? = data?.getStringExtra("categoryType")
                categoryType = dataCategory
                txtCashBoxCategory.text = dataCategory
            }
        }
    }
}