package com.example.cashorganizer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cashorganizer.R
import com.example.cashorganizer.model.CategoryViewModel

class CategoryAdapter(
    private val mList: List<CategoryViewModel>,
    private val itemClickListener: ItemClickListener,
    private val incomeCode: Int
)  : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(position: Int, incomeCode: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_type_card_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        holder.txtIncomeType.text = ItemsViewModel.text

        setupClickListeners(holder, position, incomeCode)
    }

    private fun setupClickListeners(holder: ViewHolder, position: Int, incomeCode: Int) {
        holder.rowIncomeType.setOnClickListener {
            itemClickListener.onItemClick(position, incomeCode)
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