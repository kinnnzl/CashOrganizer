package com.example.cashorganizer

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.cashorganizer.model.PlanMoneyViewModel
import com.example.cashorganizer.utilities.PlanMoneyType
import com.example.cashorganizer.utilities.UtilFunction
import com.example.cashorganizer.utilities.UtilFunction.Function.addCurrencyFormatter
import com.google.android.material.button.MaterialButton
import java.util.Date

class AddPlanMoney : AppCompatActivity() {
    private lateinit var btnEditIncomeType: MaterialButton
    private lateinit var txtHeaderIncomeType: TextView
    private lateinit var icIncomeTypeBack: AppCompatImageView
    private lateinit var icClosePlanMoneyName: AppCompatImageView
    private lateinit var cardAmountPlanMoney: CardView
    private lateinit var valuePlanMoneyName: EditText
    private lateinit var btnSavePlanMoney: MaterialButton
    private lateinit var txtHeaderPlanMoney: TextView
    private lateinit var layoutSetTarget: LinearLayout
    private lateinit var valueTargetPlanMoney: EditText
    private lateinit var checkBoxTarget: CheckBox
    private lateinit var planMoneyType: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_plan_money)

        // Initialize
        setupView()
        setupValue()
        setupClickListeners()
        setupOnChangeListeners()
    }

    private fun setupView() {
        btnEditIncomeType = findViewById(R.id.btnEditIncomeType)
        txtHeaderIncomeType = findViewById(R.id.txtHeaderIncomeType)
        icIncomeTypeBack = findViewById(R.id.icIncomeTypeBack)
        cardAmountPlanMoney = findViewById(R.id.cardAmountPlanMoney)
        icClosePlanMoneyName = findViewById(R.id.icClosePlanMoneyName)
        valuePlanMoneyName = findViewById(R.id.valuePlanMoneyName)
        btnSavePlanMoney = findViewById(R.id.btnSavePlanMoney)
        checkBoxTarget = findViewById(R.id.checkBoxTarget)
        txtHeaderPlanMoney = findViewById(R.id.txtHeaderPlanMoney)
        layoutSetTarget = findViewById(R.id.layoutSetTarget)
        valueTargetPlanMoney = findViewById(R.id.valueTargetPlanMoney)
        valueTargetPlanMoney.addCurrencyFormatter()
    }

    private fun setupValue() {
        btnEditIncomeType.visibility = View.GONE
        txtHeaderIncomeType.text = "เพิ่ม PLAN MONEY"
        planMoneyType = intent.getStringExtra("PlanMoneyType") ?: ""

        if (planMoneyType == PlanMoneyType.EXPENSES_TYPE) cardAmountPlanMoney.setCardBackgroundColor(
            ContextCompat.getColor(this, R.color.red))
    }

    private fun setupClickListeners() {
        icIncomeTypeBack.setOnClickListener {
            finish()
        }

        icClosePlanMoneyName.setOnClickListener {
            valuePlanMoneyName.text.clear()
        }

        btnSavePlanMoney.setOnClickListener {
            val validateMessage = UtilFunction.Validation.validatePlanMoney(valuePlanMoneyName.text.toString(), checkBoxTarget.isChecked,
                valueTargetPlanMoney.text.toString())
            if (validateMessage.isNotEmpty())
                Toast.makeText(this,"$validateMessage!", Toast.LENGTH_SHORT).show();
            else {
                val planMoneyModel = PlanMoneyViewModel(valuePlanMoneyName.text.toString(), 0.00, checkBoxTarget.isChecked,
                    valueTargetPlanMoney.text.toString().replace(",", "").toDouble(), planMoneyType)
                val intent = Intent()
                intent.putExtra("PlanMoneyModel", planMoneyModel)
                setResult(RESULT_OK, intent);
                finish()
            }
        }

        checkBoxTarget.setOnCheckedChangeListener { _, isChecked ->
            layoutSetTarget.visibility = View.GONE
            if (isChecked) layoutSetTarget.visibility = View.VISIBLE
        }
    }

    private fun setupOnChangeListeners() {
        valuePlanMoneyName.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrBlank()) txtHeaderPlanMoney.text = "ชื่อ PLAN MONEY"
            else { txtHeaderPlanMoney.text = text }
        }
    }
}