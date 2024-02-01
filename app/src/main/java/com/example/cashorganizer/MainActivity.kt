package com.example.cashorganizer

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.DragEvent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import com.example.cashorganizer.Fragment.DatePeriodFragment
import com.example.cashorganizer.databinding.ActivityMainBinding
import com.example.cashorganizer.Fragment.CurrentDatePeriodFragment
import com.example.cashorganizer.Fragment.CustomDatePeriodFragment
import com.example.cashorganizer.share.PeriodDateInterface
import com.example.cashorganizer.utilities.MyDragShadowBuilder
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity(), PeriodDateInterface {
    private lateinit var dialogConfirmCurrentDatePeriod: CurrentDatePeriodFragment
    private lateinit var dialogConfirmCustomDatePeriod: CustomDatePeriodFragment
    private lateinit var dialogDatePeriod: DatePeriodFragment
    private lateinit var btnAddCashBox: AppCompatImageView
    private lateinit var binding:ActivityMainBinding
    private lateinit var txtPeriod: TextView
    private lateinit var btnDatePicker: CardView
    private lateinit var cardCashbox: MaterialCardView
    lateinit var sharedPeriodDate : SharedPreferences
    private lateinit var startDate: Date
    private lateinit var endDate: Date
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
        cardCashbox = findViewById(R.id.cardCashbox)
        btnAddCashBox = findViewById(R.id.btnAddCashBox)
        dialogDatePeriod = DatePeriodFragment()
        dialogConfirmCurrentDatePeriod = CurrentDatePeriodFragment()
        dialogConfirmCustomDatePeriod = CustomDatePeriodFragment()
        sharedPeriodDate = getSharedPreferences("PeriodDate" , Context.MODE_PRIVATE)
    }

    private fun setupValue() {
        if (sharedPeriodDate.getString("StartDate", "") != "") {
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

        cardCashbox.setOnLongClickListener { v ->
            val item = ClipData.Item(v.tag as? CharSequence)
            val dragData = ClipData(
                v.tag as? CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item)

            val myShadow = MyDragShadowBuilder(v)
            v.startDragAndDrop(dragData,
                myShadow,
                null,
                0
            )
            true
        }

        cardCashbox.setOnDragListener { v, e ->
            when (e.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    if (e.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        (v as? ImageView)?.setColorFilter(Color.BLUE)
                        v.invalidate()
                        true
                    } else {
                        false
                    }
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    (v as? ImageView)?.setColorFilter(Color.GREEN)
                    v.invalidate()
                    true
                }

                DragEvent.ACTION_DRAG_LOCATION ->
                    true
                DragEvent.ACTION_DRAG_EXITED -> {
                    (v as? ImageView)?.setColorFilter(Color.BLUE)
                    v.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    val item: ClipData.Item = e.clipData.getItemAt(0)
                    val dragData = item.text
                    Toast.makeText(this, "Dragged data is $dragData", Toast.LENGTH_LONG).show()
                    (v as? ImageView)?.clearColorFilter()
                    v.invalidate()
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    (v as? ImageView)?.clearColorFilter()
                    v.invalidate()
                    when(e.result) {
                        true ->
                            Toast.makeText(this, "The drop was handled.", Toast.LENGTH_LONG)
                        else ->
                            Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_LONG)
                    }.show()
                    true
                }
                else -> {
                    Log.e("DragDrop Example", "Unknown action type received by View.OnDragListener.")
                    false
                }
            }
        }

        btnAddCashBox.setOnClickListener {
            val intent = Intent(this, CashBoxActivity::class.java)
            startActivity(intent)
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