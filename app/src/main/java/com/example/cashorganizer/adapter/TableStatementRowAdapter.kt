package com.example.cashorganizer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cashorganizer.R
import com.example.cashorganizer.model.StatementViewModel
import com.example.cashorganizer.utilities.UtilFunction.Function.convertValueToFormatMoney

class TableStatementRowAdapter(private var statementList: List<StatementViewModel?>) :
    RecyclerView.Adapter<TableStatementRowAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.table_statement_row_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.columnDate.text = "${statementList[i]?.incomeDay}/${statementList[i]?.incomeMonth}/${statementList[i]?.incomeYear}"
        viewHolder.columnCategory.text = statementList[i]?.categoryType
        viewHolder.columnIncome.text = getValueFormatMoney(statementList[i]?.incomeValue)
        viewHolder.columnExpenses.text = getValueFormatMoney(statementList[i]?.expensesValue)
        viewHolder.columnBalance.text = getValueFormatMoney(statementList[i]?.balance)
        viewHolder.columnIncomeType.text = statementList[i]?.incomeType
    }

    private fun getValueFormatMoney(value: Double?): String {
        return if (value != 0.00) convertValueToFormatMoney(value.toString())
        else ""
    }

    override fun getItemCount(): Int {
        return statementList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val columnDate: TextView = itemView.findViewById(R.id.column_date)
        val columnCategory: TextView = itemView.findViewById(R.id.column_category)
        val columnIncome: TextView = itemView.findViewById(R.id.column_income)
        val columnExpenses: TextView = itemView.findViewById(R.id.column_expenses)
        val columnBalance: TextView = itemView.findViewById(R.id.column_balance)
        val columnIncomeType: TextView = itemView.findViewById(R.id.column_income_type)
    }
}