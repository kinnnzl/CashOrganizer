package com.example.cashorganizer.Fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.DialogFragment
import com.example.cashorganizer.CashBoxActivity
import com.example.cashorganizer.R
import com.example.cashorganizer.share.AddPlanMoneyInterface
import com.example.cashorganizer.share.PeriodDateInterface
import com.example.cashorganizer.utilities.RequestCode
import com.google.android.material.card.MaterialCardView

class AddPlanMoneyFragment: DialogFragment() {
    private lateinit var icCloseAddPlanMoney: AppCompatImageView
    private lateinit var cardAddIncome: MaterialCardView
    private lateinit var cardAddExpenses: MaterialCardView
    private lateinit var addPlanMoneyInterface: AddPlanMoneyInterface
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewAddPlanMoneyDialog: View = inflater.inflate(R.layout.fragment_add_plan_money_dialog, container, false)
        viewAddPlanMoneyDialog.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return viewAddPlanMoneyDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addPlanMoneyInterface = activity as AddPlanMoneyInterface
        setupView(view)
        setupValue()
        setupClickListeners()
    }

    private fun setupView(view: View) {
        icCloseAddPlanMoney = view.findViewById(R.id.icCloseAddPlanMoney)
        cardAddIncome = view.findViewById(R.id.cardAddIncome)
        cardAddExpenses = view.findViewById(R.id.cardAddExpenses)
    }

    private fun setupValue() {

    }

    private fun setupClickListeners() {
        icCloseAddPlanMoney.setOnClickListener {
            dismiss()
        }

        cardAddIncome.setOnClickListener {
            addPlanMoneyInterface.onClickAddIncome()
            dismiss()
        }

        cardAddExpenses.setOnClickListener {
            addPlanMoneyInterface.onClickAddExpenses()
            dismiss()
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
        dialog?.window?.setGravity(Gravity.CENTER_VERTICAL or Gravity.BOTTOM)
    }
}