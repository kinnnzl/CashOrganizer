package com.example.cashorganizer.Fragment

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CalendarView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.cashorganizer.R
import com.example.cashorganizer.share.PeriodDateInterface
import com.example.cashorganizer.utilities.UtilFunction
import com.google.android.material.button.MaterialButton
import java.util.Calendar


class DatePeriodFragment: DialogFragment() {
    private lateinit var btnCurrentDatePeriod: MaterialButton
    private lateinit var btnCustomPeriodDate: MaterialButton
    private lateinit var periodDateInterface: PeriodDateInterface
    private lateinit var calendar: CalendarView
    private var currentDay: String = ""
    private var currentMonth: Int = -1
    private var currentYear: String = ""
    private var isSelectStartDate: Boolean = false
    private var isSelectEndDate: Boolean = false
    private var startDay: String = ""
    private var startMonth: Int = -1
    private var startYear: String = ""
    private var endDay: String = ""
    private var endMonth: Int = -1
    private var endYear: String = ""
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
        periodDateInterface = activity as PeriodDateInterface
        setupView(view)
        setupValue()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        btnCurrentDatePeriod.setOnClickListener {
            currentDay = if (currentDay == "") Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString() else currentDay
            currentMonth = if (currentMonth == -1) Calendar.getInstance().get(Calendar.MONTH) else currentMonth
            currentYear = if (currentYear == "") Calendar.getInstance().get(Calendar.YEAR).toString() else currentYear
            periodDateInterface.transferCurrentDate(currentDay, currentMonth, currentYear)
            dismiss()
        }

        calendar.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
            currentDay = dayOfMonth.toString()
            currentMonth = month
            currentYear = year.toString()
        })

        btnCustomPeriodDate.setOnClickListener {
            var isError = false
            currentDay = if (currentDay == "") Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString() else currentDay
            currentMonth = if (currentMonth == -1) Calendar.getInstance().get(Calendar.MONTH) else currentMonth
            currentYear = if (currentYear == "") Calendar.getInstance().get(Calendar.YEAR).toString() else currentYear
            if (isSelectStartDate || isSelectEndDate) {
                if (isSelectStartDate)  {
                    startDay = currentDay
                    startMonth = currentMonth
                    startYear = currentYear
                }
                else if (isSelectEndDate) {
                    endDay = currentDay
                    endMonth = currentMonth
                    endYear = currentYear
                }
                if (UtilFunction.Validation.validateStartAndEndCustomDate(startDay, startMonth, startYear, endDay, endMonth, endYear)) {
                    isError = true
                    Toast.makeText(getActivity(),"วันที่เริ่มต้นต้องมากกว่าวันที่สิ้นสุด!",Toast.LENGTH_SHORT).show();
                }
                else { periodDateInterface.transferEnterCustomDate(startDay, startMonth, startYear, endDay, endMonth, endYear) }
            }
            else {
                periodDateInterface.transferCustomDate()
            }
            if (!isError) dismiss()
        }
    }

    private fun setupView(view: View) {
        calendar = view.findViewById(R.id.calendarView)
        btnCurrentDatePeriod = view.findViewById(R.id.btnCurrentDatePeriod)
        btnCustomPeriodDate = view.findViewById(R.id.btnCustomPeriodDate)
    }

    private fun setupValue() {
        isSelectStartDate = arguments?.getBoolean("IsSelectStartDate") ?: false
        isSelectEndDate = arguments?.getBoolean("IsSelectEndDate") ?: false
        startDay = arguments?.getString("StartDay") ?: ""
        startMonth = arguments?.getInt("StartMonth") ?: -1
        startYear = arguments?.getString("StartYear") ?: ""
        endDay = arguments?.getString("EndDay") ?: ""
        endMonth = arguments?.getInt("EndMonth") ?: -1
        endYear = arguments?.getString("EndYear") ?: ""
        currentDay = ""
        currentMonth = -1
        currentYear = ""
        if (isSelectStartDate) btnCustomPeriodDate.text = "ยืนยันวันที่เริ่มต้น"
        else if (isSelectEndDate) btnCustomPeriodDate.text = "ยืนยันวันที่สิ้นสุด"
        if (isSelectStartDate || isSelectEndDate) btnCurrentDatePeriod.visibility = View.GONE
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