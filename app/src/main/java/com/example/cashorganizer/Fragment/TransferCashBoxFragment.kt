package com.example.cashorganizer.Fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.DialogFragment
import com.example.cashorganizer.R
import com.example.cashorganizer.model.PlanMoneyViewModel
import com.example.cashorganizer.share.IncomeDateInterface
import com.example.cashorganizer.share.TransferCashBoxInterface
import com.example.cashorganizer.utilities.UtilFunction.Function.addCurrencyFormatter
import com.example.cashorganizer.utilities.UtilFunction.Function.convertValueToFormatMoney
import com.example.cashorganizer.utilities.UtilFunction.Validation.validateTransferCashBox
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class TransferCashBoxFragment: DialogFragment() {
    private lateinit var valueCashBox: TextView
    private lateinit var planMoneyName: TextView
    private lateinit var icClose: AppCompatImageView
    private lateinit var btnSetAllCashBox: TextView
    private lateinit var inputValueTransfer: EditText
    private lateinit var btnSubmitTransfer: MaterialButton
    private var planMoneyList: ArrayList<PlanMoneyViewModel> = ArrayList()
    private lateinit var valueTransferCashBox: String
    private var indexTransferPlanMoney: Int = -1
    private lateinit var transferCashBoxInterface: TransferCashBoxInterface
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transfer_cashbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupValue()
        setupClickListeners()
    }

    private fun setupView(view: View) {
        valueCashBox = view.findViewById(R.id.valueCashBox)
        planMoneyName = view.findViewById(R.id.planMoneyName)
        icClose = view.findViewById(R.id.icClose)
        btnSetAllCashBox = view.findViewById(R.id.btnSetAllCashBox)
        inputValueTransfer = view.findViewById(R.id.inputValueTransfer)
        btnSubmitTransfer = view.findViewById(R.id.btnSubmitTransfer)
        transferCashBoxInterface = activity as TransferCashBoxInterface

        inputValueTransfer.addCurrencyFormatter()
    }

    private fun setupValue() {
        indexTransferPlanMoney = arguments?.getInt("IndexTransferPlanMoney") ?: -1
        valueTransferCashBox = arguments?.getString("ValueCashBox") ?: "0.00"
        var planMoneyListJson = arguments?.getString("PlanMoneyList") ?: ""
        if (!planMoneyListJson.isNullOrEmpty()) {
            val gson = Gson()
            val type: Type = object : TypeToken<ArrayList<PlanMoneyViewModel?>?>() {}.type
            planMoneyList = gson.fromJson(planMoneyListJson, type)
            valueCashBox.text = valueTransferCashBox
            planMoneyName.text = planMoneyList[indexTransferPlanMoney].planMoneyName
        }
    }

    private fun setupClickListeners() {
        icClose.setOnClickListener {
            dismiss()
        }

        btnSetAllCashBox.setOnClickListener {
            inputValueTransfer.setText(valueTransferCashBox)
        }

        btnSubmitTransfer.setOnClickListener {
            var validateMessage = validateTransferCashBox(inputValueTransfer.text.toString(), valueTransferCashBox)
            if (validateMessage.isNullOrEmpty()) {
                transferCashBoxInterface.transferCashBoxToPlanMoney(indexTransferPlanMoney, inputValueTransfer.text.toString())
                inputValueTransfer.setText("0.00")
                dismiss()
            }
            else {
                Toast.makeText(activity, validateMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
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