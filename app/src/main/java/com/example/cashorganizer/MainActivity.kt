package com.example.cashorganizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cashorganizer.Fragment.DatePeriodFragment
import com.example.cashorganizer.databinding.ActivityMainBinding
import com.example.cashorganizer.model.PeriodDateViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var txtPeriod: TextView
    private lateinit var btnDatePicker: CardView
    private lateinit var periodDateViewModelFragment: PeriodDateViewModel
    private var currentDay: String = ""
    private var currentMonth: Int = -1
    private var currentYear: String = ""
    private val fullMonth: List<String> = listOf("มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Initialize
        setupView()
        setupEventAction()
        setUpViewModel()
    }

    private fun setupView() {
        txtPeriod = findViewById(R.id.txtPeriod)
        btnDatePicker = findViewById(R.id.cardPeriod)
    }

    private fun showDialogDatePicker() {
        var dialogDatePeriod = DatePeriodFragment()
        dialogDatePeriod.show(supportFragmentManager, "dialogDatePeriod")
    }

    private fun setupEventAction() {
        binding.customToolbar.ivAccount.setOnClickListener{
            Toast.makeText(this, "Account clicked", Toast.LENGTH_SHORT).show()
        }
        btnDatePicker.setOnClickListener {
            showDialogDatePicker()
        }
    }

    private fun setUpViewModel() {
        periodDateViewModelFragment = ViewModelProvider(this)[PeriodDateViewModel::class.java]
        periodDateViewModelFragment.currentDay.observe(this, Observer {
            currentDay = it
            periodDateViewModelFragment.currentMonth.observe(this, Observer {
                currentMonth = it
                periodDateViewModelFragment.currentYear.observe(this, Observer {
                    currentYear = it
                    Toast.makeText(this, "$currentDay/${fullMonth[currentMonth]}/$currentYear", Toast.LENGTH_SHORT).show()
                })
            })
        })
    }
}