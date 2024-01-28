package com.example.cashorganizer.Fragment

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.cashorganizer.R
import com.example.cashorganizer.model.PeriodDateViewModel
import com.example.cashorganizer.share.PeriodDateInterface
import com.google.android.material.button.MaterialButton

class CurrentDatePeriodFragment: DialogFragment() {
    private lateinit var periodDateInterface: PeriodDateInterface
    private lateinit var headerTextDialog: TextView
    private lateinit var currentDatePeriod: TextView
    private lateinit var btnConfirmCurrentPeriodDate: MaterialButton
    private var currentDay: String = ""
    private var currentMonth: Int = -1
    private var currentYear: String = ""
    private val fullMonth: List<String> = listOf("มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewPeriodDialog: View = inflater.inflate(R.layout.fragment_current_period_date_dialog, container, false)
        return viewPeriodDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        periodDateInterface = activity as PeriodDateInterface
        setupView(view)
        setupValue()
        setupClickListeners(view)
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

    private fun setupView(view: View) {
        headerTextDialog = view.findViewById(R.id.txtHeaderDialog)
        currentDatePeriod = view.findViewById(R.id.txtCurrentDate)
        btnConfirmCurrentPeriodDate = view.findViewById(R.id.btnConfirmCurrentPeriodDate)
    }

    private fun setupValue() {
        currentDay = arguments?.getString("CurrentDay") ?: ""
        currentMonth = arguments?.getInt("CurrentMonth") ?: -1
        currentYear = arguments?.getString("CurrentYear") ?: ""

        headerTextDialog.text = "วันนี้"
        currentDatePeriod.text = "$currentDay ${fullMonth[currentMonth!!]} $currentYear"
    }

    private fun setupClickListeners(view: View) {
        btnConfirmCurrentPeriodDate.setOnClickListener {
            periodDateInterface.transferConfirmDate(currentDay, currentMonth, currentYear, "", -1, "")
            dismiss()
        }
    }
}