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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.cashorganizer.R
import com.example.cashorganizer.share.PeriodDateInterface
import com.example.cashorganizer.utilities.UtilFunction
import com.google.android.material.button.MaterialButton

class CustomDatePeriodFragment: DialogFragment() {
    private lateinit var headerTextDialog: TextView
    private lateinit var txtCustomStartDate: TextView
    private lateinit var btnCustomStartDate: MaterialButton
    private lateinit var btnCustomEndDate: MaterialButton
    private lateinit var btnConfirmCurrentPeriodDate: MaterialButton
    private lateinit var periodDateInterface: PeriodDateInterface
    private var startDay: String = ""
    private var startMonth: Int = -1
    private var startYear: String = ""
    private var endDay: String = ""
    private var endMonth: Int = -1
    private var endYear: String = ""
    private var isFirstOpenCustomDate: Boolean = false
    private val fullMonth: List<String> = listOf("มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewCustomPeriodDateDialog: View = inflater.inflate(R.layout.fragment_custom_period_date_dialog, container, false)
        return viewCustomPeriodDateDialog
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
        txtCustomStartDate = view.findViewById(R.id.txtCustomStartDate)
        btnCustomStartDate = view.findViewById(R.id.btnCustomStartDate)
        btnCustomEndDate = view.findViewById(R.id.btnCustomEndDate)
        btnConfirmCurrentPeriodDate = view.findViewById(R.id.btnConfirmCurrentPeriodDate)
    }

    private fun setupValue() {
        isFirstOpenCustomDate = arguments?.getBoolean("IsFirstOpenCustomDate") ?: false

        headerTextDialog.text = "กำหนดเอง"
        if (!isFirstOpenCustomDate) {
            startDay = arguments?.getString("StartDay") ?: ""
            startMonth = arguments?.getInt("StartMonth") ?: -1
            startYear = arguments?.getString("StartYear") ?: ""
            endDay = arguments?.getString("EndDay") ?: ""
            endMonth = arguments?.getInt("EndMonth") ?: -1
            endYear = arguments?.getString("EndYear") ?: ""

            if (startDay != "" && startYear != "") btnCustomStartDate.text = "$startDay ${fullMonth[startMonth]} $startYear"
            if (endDay != "" && endYear != "") btnCustomEndDate.text = "$endDay ${fullMonth[endMonth]} $endYear"
        }
    }

    private fun setupClickListeners(view: View) {
        btnCustomStartDate.setOnClickListener {
            periodDateInterface.transferCustomDate(startDay, startMonth, startYear, endDay, endMonth, endYear, true)
            dismiss()
        }

        btnCustomEndDate.setOnClickListener {
            periodDateInterface.transferCustomDate(startDay, startMonth, startYear, endDay, endMonth, endYear, false)
            dismiss()
        }

        btnConfirmCurrentPeriodDate.setOnClickListener {
            if (UtilFunction.Validation.validateSubmitCustomDate(startDay, startMonth, startYear, endDay, endMonth, endYear))
                Toast.makeText(getActivity(),"กรุณาเลือกวันที่เริ่มต้น หรือวันที่สิ้นสุด!", Toast.LENGTH_SHORT).show();
            else {
                periodDateInterface.transferConfirmDate(startDay, startMonth, startYear, endDay, endMonth, endYear)
                dismiss()
            }
        }
    }
}