package com.example.cashorganizer

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.cashorganizer.Fragment.DateIncomeFragment
import com.example.cashorganizer.Fragment.DatePeriodFragment
import com.example.cashorganizer.databinding.ActivityCashBoxBinding
import com.example.cashorganizer.share.IncomeDateInterface

class CashBoxActivity : AppCompatActivity(), IncomeDateInterface {
    private lateinit var binding: ActivityCashBoxBinding
    private lateinit var icClose: AppCompatImageView
    private lateinit var txtCashBoxPeriodDate: TextView
    private lateinit var btnTypeReceive: TextView
    private lateinit var icSelectCalendar: AppCompatImageView
    private lateinit var dialogDatePeriod: DateIncomeFragment
    private val fullMonth: List<String> = listOf("มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCashBoxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupClickListeners()
    }

    private fun setupView() {
        icClose = findViewById(R.id.icClose)
        txtCashBoxPeriodDate = findViewById(R.id.txtCashBoxPeriodDate)
        icSelectCalendar = findViewById(R.id.icSelectCalendar)
        btnTypeReceive = findViewById(R.id.btnTypeReceive)
        dialogDatePeriod = DateIncomeFragment()
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

        btnTypeReceive.setOnClickListener {
            val intent = Intent(this, IncomeTypeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showDialogDateIncomePicker() {
        dialogDatePeriod.show(supportFragmentManager, "dialogDatePeriod")
    }

    override fun transferIncomeDate(incomeDay: String, incomeMonth: Int, incomeYear: String) {
        txtCashBoxPeriodDate.text = "$incomeDay ${fullMonth[incomeMonth!!]} $incomeYear"
    }
}