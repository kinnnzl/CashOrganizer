package com.example.cashorganizer

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashorganizer.Fragment.DateStatementFragment
import com.example.cashorganizer.adapter.TableStatementRowAdapter
import com.example.cashorganizer.model.StatementViewModel
import com.example.cashorganizer.share.StatementDateInterface
import com.example.cashorganizer.utilities.UtilFunction.Function.convertValueToFormatMoney
import com.example.cashorganizer.utilities.UtilFunction.Validation.validateDateStatement
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.SimpleDateFormat

class StatementActivity : AppCompatActivity(), StatementDateInterface {
    private lateinit var btnEditIncomeType: MaterialButton
    private lateinit var txtHeaderIncomeType: TextView
    private lateinit var icIncomeTypeBack: AppCompatImageView
    private lateinit var btnSearchStatement: MaterialButton
    private lateinit var btnStartDate: MaterialButton
    private lateinit var btnEndDate: MaterialButton
    private lateinit var tableStatementLayout: LinearLayout
    private lateinit var valueSummaryIncome: TextView
    private lateinit var valueSummaryExpenses: TextView
    private lateinit var valueSummaryBalance: TextView
    private lateinit var dialogDateStatement: DateStatementFragment
    private var statementList: ArrayList<StatementViewModel> = ArrayList()
    private lateinit var recyclerviewStatement : RecyclerView
    private lateinit var tableStatementRowAdapter: TableStatementRowAdapter
    private lateinit var sharedCashBox : SharedPreferences
    private var gson = Gson()
    private var startDay: String = ""
    private var startMonth: String = ""
    private var startYear: String = ""
    private var endDay: String = ""
    private var endMonth: String = ""
    private var endYear: String = ""
    private val fullMonth: List<String> = listOf("มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statement)

        // Initialize
        setupView()
        setupValue()
        setupClickListeners()
    }

    private fun setupView() {
        btnEditIncomeType = findViewById(R.id.btnEditIncomeType)
        txtHeaderIncomeType = findViewById(R.id.txtHeaderIncomeType)
        icIncomeTypeBack = findViewById(R.id.icIncomeTypeBack)
        recyclerviewStatement = findViewById(R.id.table_statement_recycler_view)
        btnSearchStatement = findViewById(R.id.btnSearchStatement)
        tableStatementLayout = findViewById(R.id.table_statement_layout)
        btnStartDate = findViewById(R.id.btnStartDate)
        btnEndDate = findViewById(R.id.btnEndDate)
        valueSummaryIncome = findViewById(R.id.valueSummaryIncome)
        valueSummaryExpenses = findViewById(R.id.valueSummaryExpenses)
        valueSummaryBalance = findViewById(R.id.valueSummaryBalance)
        dialogDateStatement = DateStatementFragment()
        sharedCashBox = getSharedPreferences("CashBox" , Context.MODE_PRIVATE)
    }

    private fun setupValue() {
        btnEditIncomeType.visibility = View.GONE
        tableStatementLayout.visibility = View.INVISIBLE
        txtHeaderIncomeType.text = "Statement"

        if (!sharedCashBox.getString("StatementList", "").isNullOrEmpty()) {
            val type: Type = object : TypeToken<List<StatementViewModel?>?>() {}.type
            statementList = gson.fromJson(sharedCashBox.getString("StatementList", ""), type)
        }
    }

    private fun showDialogDateStatementPicker() {
        dialogDateStatement.show(supportFragmentManager, "dialogDateStatement")
    }

    private fun setupClickListeners() {
        icIncomeTypeBack.setOnClickListener {
            finish()
        }

        btnSearchStatement.setOnClickListener {
            var messageValidateDate = validateDateStatement(startDay, startMonth, startYear, endDay, endMonth, endYear)
            if (!messageValidateDate.isNullOrEmpty()) {
                Toast.makeText(this, messageValidateDate, Toast.LENGTH_SHORT).show()
            }
            else {
                if (statementList.isNotEmpty()) {
                    var filterStatementByDate = statementList.filter { f -> filterStatement(f) }
                    tableStatementRowAdapter = TableStatementRowAdapter(filterStatementByDate)
                    recyclerviewStatement.layoutManager = LinearLayoutManager(this)
                    recyclerviewStatement.adapter = tableStatementRowAdapter
                    tableStatementLayout.visibility = View.VISIBLE

                    setSummaryStatement(filterStatementByDate)
                }
            }
        }

        btnStartDate.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("StartDate", true)
            dialogDateStatement.arguments = bundle
            showDialogDateStatementPicker()
        }

        btnEndDate.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("StartDate", false)
            dialogDateStatement.arguments = bundle
            showDialogDateStatementPicker()
        }
    }

    private fun setSummaryStatement(statementList: List<StatementViewModel>) {
        var summaryIncome = statementList.sumOf { it?.incomeValue ?: 0.00 }
        var summaryExpenses = statementList.sumOf { it?.expensesValue ?: 0.00 }
        valueSummaryIncome.text = convertValueToFormatMoney(summaryIncome.toString())
        valueSummaryExpenses.text = convertValueToFormatMoney(summaryExpenses.toString())
        valueSummaryBalance.text = convertValueToFormatMoney((summaryIncome - summaryExpenses).toString())
    }

    private fun filterStatement(statement: StatementViewModel): Boolean {
        val startDate = SimpleDateFormat("dd-MM-yyyy").parse("$startDay-$startMonth-$startYear")
        val endDate = SimpleDateFormat("dd-MM-yyyy").parse("$endDay-$endMonth-$endYear")
        val statementDate = SimpleDateFormat("dd-MM-yyyy").parse("${statement.incomeDay}-${statement.incomeMonth}-${statement.incomeYear}")
        if (statementDate != null) {
            return statementDate in startDate..endDate
        }
        return false
    }

    override fun transferStatementDate(
        day: String,
        month: Int,
        year: String,
        isStartDate: Boolean
    ) {
        if (isStartDate) {
            startDay = day
            startMonth = month.toString()
            startYear = year
            btnStartDate.text = "$day ${fullMonth[month!!]} $year"
        }
        else {
            endDay = day
            endMonth = month.toString()
            endYear = year
            btnEndDate.text = "$day ${fullMonth[month!!]} $year"
        }
    }
}