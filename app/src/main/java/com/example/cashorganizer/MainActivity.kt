package com.example.cashorganizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import com.example.cashorganizer.Fragment.DatePeriodFragment
import com.example.cashorganizer.databinding.ActivityMainBinding
import com.example.cashorganizer.Fragment.CurrentDatePeriodFragment
import com.example.cashorganizer.share.PeriodDateInterface

class MainActivity : AppCompatActivity(), PeriodDateInterface {
    private lateinit var dialogConfirmCurrentDatePeriod: CurrentDatePeriodFragment
    private lateinit var dialogDatePeriod: DatePeriodFragment
    private lateinit var binding:ActivityMainBinding
    private lateinit var txtPeriod: TextView
    private lateinit var btnDatePicker: CardView
    private val fullMonth: List<String> = listOf("มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Initialize
        setupView()
        setupClickListeners()
    }

    private fun setupView() {
        txtPeriod = findViewById(R.id.txtPeriod)
        btnDatePicker = findViewById(R.id.cardPeriod)
        dialogDatePeriod = DatePeriodFragment()
        dialogConfirmCurrentDatePeriod = CurrentDatePeriodFragment()
    }

    private fun showDialogDatePicker() {
        dialogDatePeriod.show(supportFragmentManager, "dialogDatePeriod")
    }

    private fun setupClickListeners() {
        binding.customToolbar.ivAccount.setOnClickListener{
            Toast.makeText(this, "Account clicked", Toast.LENGTH_SHORT).show()
        }
        btnDatePicker.setOnClickListener {
            showDialogDatePicker()
        }
    }

    private fun openConfirmCurrentDate() {
        dialogConfirmCurrentDatePeriod.show(supportFragmentManager, "dialogConfirmCurrentDatePeriod")
    }

    override fun transferCurrentDate(currentDay: String, currentMonth: Int, currentYear: String) {
        val bundle = Bundle()
        bundle.putString("CurrentDay", currentDay)
        bundle.putInt("CurrentMonth", currentMonth)
        bundle.putString("CurrentYear", currentYear)
        dialogConfirmCurrentDatePeriod.arguments = bundle
        openConfirmCurrentDate()
    }

    override fun transferConfirmCurrentDate(
        currentDay: String,
        currentMonth: Int,
        currentYear: String
    ) {
        txtPeriod.text = "$currentDay ${fullMonth[currentMonth!!]} $currentYear"
    }
}