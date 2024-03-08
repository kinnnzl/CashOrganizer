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
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.cashorganizer.R
import com.example.cashorganizer.share.IncomeDateInterface
import com.google.android.material.button.MaterialButton
import java.util.Calendar

class DateIncomeFragment: DialogFragment() {
    private lateinit var txtHeaderDialog: TextView
    private lateinit var btnCustomPeriodDate: MaterialButton
    private lateinit var calendar: CalendarView
    private lateinit var incomeDateInterface: IncomeDateInterface
    private var currentDay: String = ""
    private var currentMonth: Int = -1
    private var currentYear: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_incom_date_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incomeDateInterface = activity as IncomeDateInterface
        setupView(view)
        setupValue()
        setupClickListeners()
    }

    private fun setupView(view: View) {
        calendar = view.findViewById(R.id.calendarView)
        txtHeaderDialog = view.findViewById(R.id.txtHeaderDialog)
        btnCustomPeriodDate = view.findViewById(R.id.btnCustomPeriodDate)
    }

    private fun setupValue() {
        txtHeaderDialog.text = "เลือกวันที่รับรายได้"
    }

    private fun setupClickListeners() {
        calendar.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
            currentDay = dayOfMonth.toString()
            currentMonth = month
            currentYear = year.toString()
        })

        btnCustomPeriodDate.setOnClickListener {
            currentDay = if (currentDay == "") Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString() else currentDay
            currentMonth = if (currentMonth == -1) Calendar.getInstance().get(Calendar.MONTH) else currentMonth
            currentYear = if (currentYear == "") Calendar.getInstance().get(Calendar.YEAR).toString() else currentYear
            incomeDateInterface.transferIncomeDate(currentDay, currentMonth, currentYear)
            dismiss()
        }
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