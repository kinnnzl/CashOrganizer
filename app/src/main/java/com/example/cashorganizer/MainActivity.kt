package com.example.cashorganizer

import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashorganizer.Fragment.TransferCashBoxFragment
import com.example.cashorganizer.adapter.PlanMoneyAdapter
import com.example.cashorganizer.databinding.ActivityMainBinding
import com.example.cashorganizer.model.CashBoxViewModel
import com.example.cashorganizer.model.PlanMoneyViewModel
import com.example.cashorganizer.share.TransferCashBoxInterface
import com.example.cashorganizer.utilities.MyDragShadowBuilder
import com.example.cashorganizer.utilities.PlanMoneyType
import com.example.cashorganizer.utilities.RequestCode
import com.example.cashorganizer.utilities.UtilFunction.Function.convertValueToFormatMoney
import com.example.cashorganizer.utilities.UtilFunction.Function.findPositionAtPoint
import com.example.cashorganizer.utilities.UtilFunction.Function.replaceFormatMoney
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class MainActivity : AppCompatActivity(), TransferCashBoxInterface,
    PlanMoneyAdapter.ItemClickListener {
    private lateinit var binding:ActivityMainBinding
    private lateinit var dialogTransferCashBox: TransferCashBoxFragment
    private lateinit var btnAddCashBox: AppCompatImageView
    private lateinit var valueCashBox: TextView
    private lateinit var cardCashBox: MaterialCardView
    private lateinit var cardAddPlanMoney: CardView
    private lateinit var txtValueSummaryIncome: TextView
    private lateinit var recyclerviewPlanMoney: RecyclerView
    private lateinit var sharedCashBox : SharedPreferences
    private lateinit var sharedPlanMoney : SharedPreferences
    private var planMoneyList: ArrayList<PlanMoneyViewModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Initialize
        setupView()
        setupValue()
        setupClickListeners()
    }

    private fun setupView() {
        //region Setup view cash box
        cardCashBox = findViewById(R.id.cardCashbox)
        btnAddCashBox = findViewById(R.id.btnAddCashBox)
        valueCashBox = findViewById(R.id.valueCashBox)
        sharedCashBox = getSharedPreferences("CashBox" , Context.MODE_PRIVATE)
        //endregion

        //region Setup view plan money
        cardAddPlanMoney = findViewById(R.id.cardAddPlanMoney)
        recyclerviewPlanMoney = findViewById(R.id.recyclerviewPlanMoney)
        recyclerviewPlanMoney.layoutManager = GridLayoutManager(this, 2)
        sharedPlanMoney = getSharedPreferences("PlanMoneys" , Context.MODE_PRIVATE)
        //endregion

        dialogTransferCashBox = TransferCashBoxFragment()

        //region Setup view summary
        txtValueSummaryIncome = findViewById(R.id.txtValueSummaryIncome)
        //endregion
    }

    private fun setupValue() {
        //region Setup value cash box
        if (sharedCashBox.getString("CashBoxSummary", "") != "") {
            valueCashBox.text = convertValueToFormatMoney(sharedCashBox.getString("CashBoxSummary", "") ?: "")
        }
        //endregion

        //region Setup value plan money
        if (!sharedPlanMoney.getString("PlanMoneys", "").isNullOrEmpty()) {
            val gson = Gson()
            val type: Type = object : TypeToken<ArrayList<PlanMoneyViewModel?>?>() {}.type
            planMoneyList = gson.fromJson(sharedPlanMoney.getString("PlanMoneys", ""), type)
            setPlanMoneyRecyclerview()
        }
        //endregion

        //region Setup value summary
        setValueSummaryIncome()
        //endregion
    }

    private fun setValueSummaryIncome() {
        if (!sharedCashBox.getString("CashBoxList", "").isNullOrEmpty()) {
            val gson = Gson()
            val type: Type = object : TypeToken<ArrayList<CashBoxViewModel?>?>() {}.type
            var cashBoxList: ArrayList<CashBoxViewModel?> = gson.fromJson(sharedCashBox.getString("CashBoxList", ""), type)
            txtValueSummaryIncome.text = convertValueToFormatMoney(cashBoxList.sumOf { it?.incomeValue ?: 0.00 }.toString())
        }
    }

    private fun setupClickListeners() {
        //region Setup click toolbar
        binding.customToolbar.ivAccount.setOnClickListener{
            Toast.makeText(this, "Account clicked", Toast.LENGTH_SHORT).show()
        }
        //endregion

        cardCashBox.tag = valueCashBox.text
        //region Setup click cash box
        cardCashBox.setOnLongClickListener { v ->
            v.tag = valueCashBox.text
            val item = ClipData.Item(v.tag as? CharSequence)
            val dragData = ClipData(
                v.tag as? CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item)

            val myShadow = MyDragShadowBuilder(cardCashBox)
            v.startDragAndDrop(dragData,
                myShadow,
                null,
                0
            )
            true
        }

        recyclerviewPlanMoney.setOnDragListener { v, e ->
            when (e.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    if (e.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        v.invalidate()
                        true
                    } else {
                        false
                    }
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    v.invalidate()
                    true
                }

                DragEvent.ACTION_DRAG_LOCATION -> {
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    v.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    val point = Point(e.x.toInt(), e.y.toInt())
                    val position = findPositionAtPoint(point, recyclerviewPlanMoney)
                    if (position !== -1) {
                        val item: ClipData.Item = e.clipData.getItemAt(0)
                        var planMoneyListForTransfer = ""
                        val bundle = Bundle()
                        if (!sharedPlanMoney.getString("PlanMoneys", "").isNullOrEmpty())
                            planMoneyListForTransfer = sharedPlanMoney.getString("PlanMoneys", "") ?: ""
                        bundle.putInt("IndexTransferPlanMoney", position)
                        bundle.putString("ValueCashBox", item.text.toString())
                        bundle.putString("PlanMoneyList", planMoneyListForTransfer)
                        dialogTransferCashBox.arguments = bundle
                        openDialogTransferCashBox()
                    }
                    v.invalidate()
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    v.invalidate()
                    when(e.result) {
                        true ->
                            Log.e("Success", "The drop was handled.")
                        else ->
                            Log.e("Fail", "The drop didn't work.")
                    }
                    true
                }
                else -> {
                    Log.e("DragDrop Example", "Unknown action type received by View.OnDragListener.")
                    false
                }
            }
        }

        btnAddCashBox.setOnClickListener {
            val intent = Intent(this, CashBoxActivity::class.java)
            startActivityForResult(intent, RequestCode.MAIN_INCOME_TYPE)
        }
        //endregion

        //region Setup click plan money
        cardAddPlanMoney.setOnClickListener {
            val intent = Intent(this, AddPlanMoney::class.java)
            intent.putExtra("PlanMoneyType", PlanMoneyType.EXPENSES_TYPE)
            startActivityForResult(intent, RequestCode.Add_PLAN_MONEY)
        }
        //endregion
    }

    //region Period date
    private fun openDialogTransferCashBox() {
        dialogTransferCashBox.show(supportFragmentManager, "dialogTransferCashBox")
    }
    //endregion

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.MAIN_INCOME_TYPE) {
            if (resultCode == Activity.RESULT_OK) {
                val editShareCashBox = sharedCashBox.edit()
                val incomeDay = data?.getStringExtra("IncomeDay") ?: "0"
                val incomeMonth = data?.getStringExtra("IncomeMonth") ?: "0"
                val incomeYear = data?.getStringExtra("IncomeYear") ?: "0"
                val incomeType = data?.getStringExtra("IncomeType") ?: ""
                val categoryType = data?.getStringExtra("CategoryType") ?: ""
                val incomeValue = data?.getStringExtra("IncomeValue") ?: "0.00"
                val incomeDesc = data?.getStringExtra("IncomeDesc") ?: ""
                var cashBoxViewModel = CashBoxViewModel(incomeDay.toInt(), incomeMonth.toInt(), incomeYear.toInt(), incomeType,
                    categoryType, replaceFormatMoney(incomeValue).toDouble(), incomeDesc)
                var cashBoxSummary = sharedCashBox.getString("CashBoxSummary", "")
                if (!sharedCashBox.getString("CashBoxList", "").isNullOrEmpty()) {
                    val gson = Gson()
                    val type: Type = object : TypeToken<ArrayList<CashBoxViewModel?>?>() {}.type
                    var cashBoxList: ArrayList<CashBoxViewModel?> = gson.fromJson(sharedCashBox.getString("CashBoxList", ""), type)
                    cashBoxList.add(cashBoxViewModel)
                    val jsonCashBoxList = gson.toJson(cashBoxList)
                    cashBoxSummary = (replaceFormatMoney(cashBoxSummary.toString()).toDouble() + cashBoxViewModel.incomeValue).toString()
                    editShareCashBox.putString("CashBoxList", jsonCashBoxList)
                    editShareCashBox.putString("CashBoxSummary", cashBoxSummary)
                    valueCashBox.text = convertValueToFormatMoney(cashBoxSummary)
                }
                else {
                    val gson = Gson()
                    var cashBoxList: ArrayList<CashBoxViewModel?> = ArrayList()
                    cashBoxList.add(cashBoxViewModel)
                    val jsonCashBoxList = gson.toJson(cashBoxList)
                    cashBoxSummary = cashBoxViewModel.incomeValue.toString()
                    editShareCashBox.putString("CashBoxList", jsonCashBoxList)
                    editShareCashBox.putString("CashBoxSummary", cashBoxSummary)
                    valueCashBox.text = convertValueToFormatMoney(cashBoxSummary)
                }
                editShareCashBox.apply()
                setValueSummaryIncome()
            }
        }
        else if (requestCode == RequestCode.Add_PLAN_MONEY) {
            if (resultCode == Activity.RESULT_OK) {
                val planMoneyModel = data?.getSerializableExtra("PlanMoneyModel") as PlanMoneyViewModel
                planMoneyList.add(planMoneyModel)
                val editPlanMoney = sharedPlanMoney.edit()
                val gson = Gson()
                val jsonPlanMoneyList = gson.toJson(planMoneyList)
                editPlanMoney.putString("PlanMoneys", jsonPlanMoneyList);
                editPlanMoney.apply()
                setPlanMoneyRecyclerview()
            }
        }
    }

    private fun setPlanMoneyRecyclerview() {
        val adapterPlanMoney = PlanMoneyAdapter(planMoneyList, this)
        recyclerviewPlanMoney.adapter = adapterPlanMoney
    }

    override fun onItemClick(position: Int) {

    }

    override fun transferCashBoxToPlanMoney(indexPlanMoney: Int, valueTransfer: String) {
        val getPlanMoneys = sharedPlanMoney.getString("PlanMoneys", "")
        val getCashBoxSummary = sharedCashBox.getString("CashBoxSummary", "")
        if (!getPlanMoneys.isNullOrEmpty() && !getCashBoxSummary.isNullOrEmpty()) {
            val gson = Gson()
            val type: Type = object : TypeToken<ArrayList<PlanMoneyViewModel?>?>() {}.type
            planMoneyList = gson.fromJson(getPlanMoneys, type)
            if (planMoneyList.isNotEmpty() && indexPlanMoney != -1) {
                planMoneyList[indexPlanMoney].amount = planMoneyList[indexPlanMoney].amount + replaceFormatMoney(valueTransfer).toDouble()
                valueCashBox.text = convertValueToFormatMoney((replaceFormatMoney(getCashBoxSummary).toDouble()
                        - replaceFormatMoney(valueTransfer).toDouble()).toString())

                val editPlanMoney = sharedPlanMoney.edit()
                val gson = Gson()
                val jsonPlanMoneyList = gson.toJson(planMoneyList)
                editPlanMoney.putString("PlanMoneys", jsonPlanMoneyList);
                editPlanMoney.apply()

                val editCashBox = sharedCashBox.edit()
                editCashBox.putString("CashBoxSummary", replaceFormatMoney(valueCashBox.text.toString()));
                editCashBox.apply()

                setPlanMoneyRecyclerview()
            }
        }
    }
}