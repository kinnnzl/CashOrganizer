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
import com.example.cashorganizer.share.StatementDateInterface
import com.google.android.material.button.MaterialButton
import java.util.Calendar

class DateStatementFragment: DialogFragment() {
    private lateinit var txtHeaderDialog: TextView
    private lateinit var btnCustomPeriodDate: MaterialButton
    private lateinit var calendar: CalendarView
    private lateinit var statementDateInterface: StatementDateInterface
    private var currentDay: String = ""
    private var currentMonth: Int = -1
    private var currentYear: String = ""
    private var isStartDate: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_incom_date_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statementDateInterface = activity as StatementDateInterface
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
        currentDay = ""
        currentMonth = -1
        currentYear = ""
        txtHeaderDialog.text = "เลือกวันที่เริ่มต้น"
        isStartDate = arguments?.getBoolean("StartDate") ?: false
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
            statementDateInterface.transferStatementDate(currentDay, currentMonth, currentYear, isStartDate)
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