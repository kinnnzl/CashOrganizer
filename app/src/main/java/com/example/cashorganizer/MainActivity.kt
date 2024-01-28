package com.example.cashorganizer

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import com.example.cashorganizer.Fragment.DatePeriodFragment
import com.example.cashorganizer.databinding.ActivityMainBinding
import com.example.cashorganizer.Fragment.CurrentDatePeriodFragment
import com.example.cashorganizer.Fragment.CustomDatePeriodFragment
import com.example.cashorganizer.share.PeriodDateInterface
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity(), PeriodDateInterface {
    private lateinit var dialogConfirmCurrentDatePeriod: CurrentDatePeriodFragment
    private lateinit var dialogConfirmCustomDatePeriod: CustomDatePeriodFragment
    private lateinit var dialogDatePeriod: DatePeriodFragment
    private lateinit var binding:ActivityMainBinding
    private lateinit var txtPeriod: TextView
    private lateinit var btnDatePicker: CardView
    lateinit var sharedPeriodDate : SharedPreferences
    private lateinit var startDate: Date
    private lateinit var endDate: Date
    private var currentMonth: Int = -1
    private var currentYear: String = ""
    private val fullMonth: List<String> = listOf("มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม")
    private val shortMonth: List<String> = listOf("ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.", "พ.ค.", "มิ.ย.", "ก.ค.", "ส.ค.", "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค.")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Initialize
        setupView()
        setupValue()
        setupClickListeners()
    }

    private fun setupView() {
        txtPeriod = findViewById(R.id.txtPeriod)
        btnDatePicker = findViewById(R.id.cardPeriod)
        dialogDatePeriod = DatePeriodFragment()
        dialogConfirmCurrentDatePeriod = CurrentDatePeriodFragment()
        dialogConfirmCustomDatePeriod = CustomDatePeriodFragment()
        sharedPeriodDate = getSharedPreferences("PeriodDate" , Context.MODE_PRIVATE)
    }

    private fun setupValue() {
        startDate = SimpleDateFormat("dd-MM-yyyy").parse(sharedPeriodDate.getString("StartDate", ""))
        if (sharedPeriodDate.getString("StartDate", "") != "" && sharedPeriodDate.getString("EndDate", "") != "") {
            endDate = SimpleDateFormat("dd-MM-yyyy").parse(sharedPeriodDate.getString("EndDate", ""))
            txtPeriod.text =
                "รอบวันที่ ${DateFormat.format("dd", startDate)} " +
                        "${shortMonth[DateFormat.format("MM", startDate).toString().toInt() - 1]} " +
                        "${DateFormat.format("yyyy", startDate)} - " +
                        "${DateFormat.format("dd", endDate)} " +
                        "${shortMonth[DateFormat.format("MM", endDate).toString().toInt() - 1]} " +
                        "${DateFormat.format("yyyy", endDate)}"
        }
        else {
            txtPeriod.text = "รอบวันที่ ${DateFormat.format("dd", startDate)} " +
                    "${shortMonth[DateFormat.format("MM", startDate).toString().toInt() - 1]} " +
                    "${DateFormat.format("yyyy", startDate)}"
        }
    }

    private fun showDialogDatePicker() {
        dialogDatePeriod.show(supportFragmentManager, "dialogDatePeriod")
    }

    private fun setupClickListeners() {
        binding.customToolbar.ivAccount.setOnClickListener{
            Toast.makeText(this, "Account clicked", Toast.LENGTH_SHORT).show()
        }
        btnDatePicker.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("IsSelectStartDate", false)
            bundle.putBoolean("IsSelectEndDate", false)
            dialogDatePeriod.arguments = bundle
            showDialogDatePicker()
        }
    }

    private fun openConfirmCurrentDate() {
        dialogConfirmCurrentDatePeriod.show(supportFragmentManager, "dialogConfirmCurrentDatePeriod")
    }

    private fun openConfirmCustomDate() {
        dialogConfirmCustomDatePeriod.show(supportFragmentManager, "dialogConfirmCustomDatePeriod")
    }

    override fun transferCurrentDate(currentDay: String, currentMonth: Int, currentYear: String) {
        val bundle = Bundle()
        bundle.putString("CurrentDay", currentDay)
        bundle.putInt("CurrentMonth", currentMonth)
        bundle.putString("CurrentYear", currentYear)
        dialogConfirmCurrentDatePeriod.arguments = bundle
        openConfirmCurrentDate()
    }

    override fun transferCustomDate() {
        val bundle = Bundle()
        bundle.putBoolean("IsFirstOpenCustomDate", true)
        dialogConfirmCustomDatePeriod.arguments = bundle
        openConfirmCustomDate()
    }

    override fun transferConfirmDate(
        currentDay: String,
        currentMonth: Int,
        currentYear: String,
        endDay: String,
        endMonth: Int,
        endYear: String
    ) {
        val editSharePeriodDate = sharedPeriodDate.edit()
        editSharePeriodDate.putString("StartDate", "$currentDay-${currentMonth + 1}-$currentYear")
        if (endDay == "" && endMonth == -1 && endYear == "") {
            txtPeriod.text = "รอบวันที่ $currentDay ${fullMonth[currentMonth!!]} $currentYear"
            editSharePeriodDate.putString("EndDate", "")
        }
        else {
            txtPeriod.text = "รอบวันที่ $currentDay ${shortMonth[currentMonth!!]} $currentYear - $endDay ${shortMonth[endMonth!!]} $endYear"
            editSharePeriodDate.putString("EndDate", "$endDay-${endMonth + 1}-$endYear")
        }
        editSharePeriodDate.apply()
    }

    override fun transferCustomDate(
        startDay: String,
        startMonth: Int,
        startYear: String,
        endDay: String,
        endMonth: Int,
        endYear: String,
        isStartDate: Boolean
    ) {
        val bundle = Bundle()
        bundle.putBoolean("IsSelectStartDate", isStartDate)
        bundle.putBoolean("IsSelectEndDate", !isStartDate)
        bundle.putString("StartDay", startDay)
        bundle.putInt("StartMonth", startMonth)
        bundle.putString("StartYear", startYear)
        bundle.putString("EndDay", endDay)
        bundle.putInt("EndMonth", endMonth)
        bundle.putString("EndYear", endYear)
        dialogDatePeriod.arguments = bundle
        showDialogDatePicker()
    }

    override fun transferEnterCustomDate(
        startDay: String,
        startMonth: Int,
        startYear: String,
        endDay: String,
        endMonth: Int,
        endYear: String
    ) {
        val bundle = Bundle()
        bundle.putString("StartDay", startDay)
        bundle.putInt("StartMonth", startMonth)
        bundle.putString("StartYear", startYear)
        bundle.putString("EndDay", endDay)
        bundle.putInt("EndMonth", endMonth)
        bundle.putString("EndYear", endYear)
        bundle.putBoolean("IsFirstOpenCustomDate", false)
        dialogConfirmCustomDatePeriod.arguments = bundle
        openConfirmCustomDate()
    }
}