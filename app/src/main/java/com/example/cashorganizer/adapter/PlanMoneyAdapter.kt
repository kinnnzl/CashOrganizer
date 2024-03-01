package com.example.cashorganizer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cashorganizer.R
import com.example.cashorganizer.model.PlanMoneyViewModel
import com.example.cashorganizer.utilities.PlanMoneyType

class PlanMoneyAdapter(private val mList: List<PlanMoneyViewModel>, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<PlanMoneyAdapter.ViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.plan_money_card_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        holder.txtHeaderPlanMoney.text = ItemsViewModel.planMoneyName
        if (ItemsViewModel.isTarget) holder.txtAmountPlanMoney.text = "${ItemsViewModel.amount} / ${ItemsViewModel.amountTarget}"
        if (ItemsViewModel.planMoneyType == PlanMoneyType.EXPENSES_TYPE) holder.cardAmountPlanMoney.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.red))
        holder.icPlanMoney.setImageResource(R.drawable.ic_income_statement);
        holder.icPlanMoney.setColorFilter(R.color.black)
        setupClickListeners(holder, position)
    }

    private fun setupClickListeners(holder: ViewHolder, position: Int) {
        holder.rowPlanMoney.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val txtHeaderPlanMoney: TextView = itemView.findViewById(R.id.txtHeaderPlanMoney)
        val txtAmountPlanMoney: TextView = itemView.findViewById(R.id.txtAmountPlanMoney)
        val rowPlanMoney: LinearLayout = itemView.findViewById(R.id.rowPlanMoney)
        val cardAmountPlanMoney: CardView = itemView.findViewById(R.id.cardAmountPlanMoney)
        val icPlanMoney: ImageView = itemView.findViewById(R.id.icPlanMoney)
    }
}