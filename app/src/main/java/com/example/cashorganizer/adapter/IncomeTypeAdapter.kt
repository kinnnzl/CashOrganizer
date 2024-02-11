package com.example.cashorganizer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cashorganizer.R
import com.example.cashorganizer.model.IncomeTypeViewModel

class IncomeTypeAdapter(private val mList: List<IncomeTypeViewModel>, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<IncomeTypeAdapter.ViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.income_type_card_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        holder.txtIncomeType.text = ItemsViewModel.text

        setupClickListeners(holder, position)
    }

    private fun setupClickListeners(holder: ViewHolder, position: Int) {
        holder.rowIncomeType.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val txtIncomeType: TextView = itemView.findViewById(R.id.txtIncomeType)
        val rowIncomeType: LinearLayout = itemView.findViewById(R.id.rowIncomeType)
    }
}