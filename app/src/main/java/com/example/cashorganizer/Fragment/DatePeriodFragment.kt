package com.example.cashorganizer.Fragment

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.cashorganizer.R
import com.example.cashorganizer.model.PeriodDateViewModel
import com.google.android.material.button.MaterialButton
import java.util.Calendar
import kotlin.time.Duration.Companion.days


class DatePeriodFragment: DialogFragment() {
    private lateinit var btnCurrentDatePeriod: MaterialButton
    private lateinit var periodDateViewModel: PeriodDateViewModel
    private lateinit var calendar: CalendarView
    private var currentDay: String = ""
    private var currentMonth: Int = -1
    private var currentYear: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewPeriodDialog: View = inflater.inflate(R.layout.fragment_period_date_dialog, container, false)
        return viewPeriodDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        periodDateViewModel = ViewModelProvider(requireActivity()).get(PeriodDateViewModel::class.java)
        setupView(view)
        setupClickListeners(view)
    }

    private fun setupClickListeners(view: View) {
        btnCurrentDatePeriod.setOnClickListener {
            currentDay = if (currentDay == "") Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString() else currentDay
            currentMonth = if (currentMonth == -1) Calendar.getInstance().get(Calendar.MONTH) else currentMonth
            currentYear = if (currentYear == "") Calendar.getInstance().get(Calendar.YEAR).toString() else currentYear
            periodDateViewModel.setCurrentDay(currentDay)
            periodDateViewModel.setCurrentMonth(currentMonth)
            periodDateViewModel.setCurrentYear(currentYear)
            dismiss()
        }
        calendar.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
            currentDay = dayOfMonth.toString()
            currentMonth = month
            currentYear = year.toString()
        })
    }

    private fun setupView(view: View) {
        calendar = view.findViewById(R.id.calendarView)
        btnCurrentDatePeriod = view.findViewById(R.id.btnCurrentDatePeriod)
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        val size = Point()
        val display = window!!.windowManager.defaultDisplay
        display.getSize(size)
        val width = size.x
        dialog?.window?.setLayout(
            ((width * 0.90).toInt()),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}