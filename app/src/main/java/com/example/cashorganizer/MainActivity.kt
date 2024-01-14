package com.example.cashorganizer

import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.cashorganizer.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var txtPeriod: TextView
    private lateinit var btnDatePicker: CardView
    private val calendar = Calendar.getInstance()
    private val fullMonth: List<String> = listOf("มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Initialize views
        txtPeriod = findViewById(R.id.txtPeriod)
        btnDatePicker = findViewById(R.id.cardPeriod)
        initUI()

        // Action click
        btnDatePicker.setOnClickListener {
            // Show the DatePicker dialog
            showDialogDatePicker()
        }
    }

    private fun showDialogDatePicker() {
        // Create a DatePickerDialog
        var dialogDatePeriod = DatePeriodFragment()
        dialogDatePeriod.show(supportFragmentManager, "customDialog")
    }

    private fun initUI() {
        binding.customToolbar.ivAccount.setOnClickListener{
            Toast.makeText(this, "Account clicked", Toast.LENGTH_SHORT).show()
        }
    }
}