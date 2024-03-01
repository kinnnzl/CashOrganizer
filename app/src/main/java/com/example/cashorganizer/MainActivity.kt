package com.example.cashorganizer

import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Point
import android.os.Bundle
import android.text.format.DateFormat
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
import com.example.cashorganizer.Fragment.AddPlanMoneyFragment
import com.example.cashorganizer.Fragment.CurrentDatePeriodFragment
import com.example.cashorganizer.Fragment.CustomDatePeriodFragment
import com.example.cashorganizer.Fragment.DatePeriodFragment
import com.example.cashorganizer.adapter.PlanMoneyAdapter
import com.example.cashorganizer.databinding.ActivityMainBinding
import com.example.cashorganizer.model.CashBoxViewModel
import com.example.cashorganizer.model.PlanMoneyViewModel
import com.example.cashorganizer.share.AddPlanMoneyInterface
import com.example.cashorganizer.share.PeriodDateInterface
import com.example.cashorganizer.utilities.MyDragShadowBuilder
import com.example.cashorganizer.utilities.PlanMoneyType
import com.example.cashorganizer.utilities.RequestCode
import com.example.cashorganizer.utilities.UtilFunction.Function.convertValueToFormatMoney
import com.example.cashorganizer.utilities.UtilFunction.Function.findPositionAtPoint
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : AppCompatActivity(), PeriodDateInterface, AddPlanMoneyInterface,
    PlanMoneyAdapter.ItemClickListener {
    private lateinit var binding:ActivityMainBinding
    private lateinit var dialogConfirmCurrentDatePeriod: CurrentDatePeriodFragment
    private lateinit var dialogConfirmCustomDatePeriod: CustomDatePeriodFragment
    private lateinit var dialogAddPlanMoney: AddPlanMoneyFragment
    private lateinit var dialogDatePeriod: DatePeriodFragment
    private lateinit var btnAddCashBox: AppCompatImageView
    private lateinit var txtPeriod: TextView
    private lateinit var valueCashBox: TextView
    private lateinit var btnDatePicker: CardView
    private lateinit var cardCashBox: MaterialCardView
    private lateinit var cardAddPlanMoney: CardView
    private lateinit var recyclerviewPlanMoney: RecyclerView
    private lateinit var sharedPeriodDate : SharedPreferences
    private lateinit var sharedCashBox : SharedPreferences
    private lateinit var sharedPlanMoney : SharedPreferences
    private lateinit var startDate: Date
    private lateinit var endDate: Date
    private var planMoneyList: ArrayList<PlanMoneyViewModel> = ArrayList()
    private val fullMonth: List<String> = listOf("มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม")
    private val shortMonth: List<String> = listOf("ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.", "พ.ค.", "มิ.ย.", "ก.ค.", "ส.ค.", "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค.")
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
        //region Setup view period date
        txtPeriod = findViewById(R.id.txtPeriod)
        btnDatePicker = findViewById(R.id.cardPeriod)
        dialogDatePeriod = DatePeriodFragment()
        dialogConfirmCurrentDatePeriod = CurrentDatePeriodFragment()
        dialogConfirmCustomDatePeriod = CustomDatePeriodFragment()
        sharedPeriodDate = getSharedPreferences("PeriodDate" , Context.MODE_PRIVATE)
        //endregion

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
        dialogAddPlanMoney = AddPlanMoneyFragment()
        sharedPlanMoney = getSharedPreferences("PlanMoneys" , Context.MODE_PRIVATE)
        //endregion
    }

    private fun setupValue() {
        //region Setup value period date
        if (sharedPeriodDate.getString("StartDate", "") != "") {
            startDate = SimpleDateFormat("dd-MM-yyyy").parse(sharedPeriodDate.getString("StartDate", ""))
            if (sharedPeriodDate.getString("StartDate", "") != "" && sharedPeriodDate.getString("EndDate", "") != "") {
                endDate = SimpleDateFormat("dd-MM-yyyy").parse(sharedPeriodDate.getString("EndDate", ""))
                txtPeriod.text =
                    "รอบวันที่ ${DateFormat.format("dd", startDate)} " +
                            "${shortMonth[DateFormat.format("MM", startDate).toString().toInt() - 1]} " +
                            "${DateFormat.format("yyyy", startDate)} - " +
                            "${DateFormat.format("dd", endDate)} " +
                            "${shortMonth[DateFormat.format("MM", endDate).toString().toInt() - 1]} " +
                            "${DateFormat.format("yyyy", endDate)}"
            }
            else {
                txtPeriod.text = "รอบวันที่ ${DateFormat.format("dd", startDate)} " +
                        "${shortMonth[DateFormat.format("MM", startDate).toString().toInt() - 1]} " +
                        "${DateFormat.format("yyyy", startDate)}"
            }
        }
        //endregion

        //region Setup value cash box
        if (sharedCashBox.getString("CashBoxList", "") != "") {
            val gson = Gson()
            val type: Type = object : TypeToken<ArrayList<CashBoxViewModel?>?>() {}.type
            var cashBoxList: ArrayList<CashBoxViewModel?> = gson.fromJson(sharedCashBox.getString("CashBoxList", ""), type)
            valueCashBox.text = convertValueToFormatMoney(cashBoxList.sumOf { it!!.incomeValue }.toString())
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
    }

    private fun setupClickListeners() {
        //region Setup click toolbar
        binding.customToolbar.ivAccount.setOnClickListener{
            Toast.makeText(this, "Account clicked", Toast.LENGTH_SHORT).show()
        }
        //endregion

        //region Setup click period date
        btnDatePicker.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("IsSelectStartDate", false)
            bundle.putBoolean("IsSelectEndDate", false)
            dialogDatePeriod.arguments = bundle
            openDialogDatePicker()
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
                        val dragData = item.text
                    }
                    Toast.makeText(this, "Dragged data is $position", Toast.LENGTH_LONG).show()
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
            dialogAddPlanMoney.show(supportFragmentManager, "dialogAddPlanMoney")
        }
        //endregion
    }

    //region Period date
    private fun openDialogDatePicker() {
        dialogDatePeriod.show(supportFragmentManager, "dialogDatePeriod")
    }

    private fun openConfirmCurrentDate() {
        dialogConfirmCurrentDatePeriod.show(supportFragmentManager, "dialogConfirmCurrentDatePeriod")
    }

    private fun openConfirmCustomDate() {
        dialogConfirmCustomDatePeriod.show(supportFragmentManager, "dialogConfirmCustomDatePeriod")
    }

    override fun transferCurrentDate(currentDay: String, currentMonth: Int, currentYear: String) {
        val bundle = Bundle()
        bundle.putString("CurrentDay", currentDay)
        bundle.putInt("CurrentMonth", currentMonth)
        bundle.putString("CurrentYear", currentYear)
        dialogConfirmCurrentDatePeriod.arguments = bundle
        openConfirmCurrentDate()
    }

    override fun transferCustomDate() {
        val bundle = Bundle()
        bundle.putBoolean("IsFirstOpenCustomDate", true)
        dialogConfirmCustomDatePeriod.arguments = bundle
        openConfirmCustomDate()
    }

    override fun transferConfirmDate(
        currentDay: String,
        currentMonth: Int,
        currentYear: String,
        endDay: String,
        endMonth: Int,
        endYear: String
    ) {
        val editSharePeriodDate = sharedPeriodDate.edit()
        editSharePeriodDate.putString("StartDate", "$currentDay-${currentMonth + 1}-$currentYear")
        if (endDay == "" && endMonth == -1 && endYear == "") {
            txtPeriod.text = "รอบวันที่ $currentDay ${fullMonth[currentMonth!!]} $currentYear"
            editSharePeriodDate.putString("EndDate", "")
        }
        else {
            txtPeriod.text = "รอบวันที่ $currentDay ${shortMonth[currentMonth!!]} $currentYear - $endDay ${shortMonth[endMonth!!]} $endYear"
            editSharePeriodDate.putString("EndDate", "$endDay-${endMonth + 1}-$endYear")
        }
        editSharePeriodDate.apply()
    }

    override fun transferCustomDate(
        startDay: String,
        startMonth: Int,
        startYear: String,
        endDay: String,
        endMonth: Int,
        endYear: String,
        isStartDate: Boolean
    ) {
        val bundle = Bundle()
        bundle.putBoolean("IsSelectStartDate", isStartDate)
        bundle.putBoolean("IsSelectEndDate", !isStartDate)
        bundle.putString("StartDay", startDay)
        bundle.putInt("StartMonth", startMonth)
        bundle.putString("StartYear", startYear)
        bundle.putString("EndDay", endDay)
        bundle.putInt("EndMonth", endMonth)
        bundle.putString("EndYear", endYear)
        dialogDatePeriod.arguments = bundle
        openDialogDatePicker()
    }

    override fun transferEnterCustomDate(
        startDay: String,
        startMonth: Int,
        startYear: String,
        endDay: String,
        endMonth: Int,
        endYear: String
    ) {
        val bundle = Bundle()
        bundle.putString("StartDay", startDay)
        bundle.putInt("StartMonth", startMonth)
        bundle.putString("StartYear", startYear)
        bundle.putString("EndDay", endDay)
        bundle.putInt("EndMonth", endMonth)
        bundle.putString("EndYear", endYear)
        bundle.putBoolean("IsFirstOpenCustomDate", false)
        dialogConfirmCustomDatePeriod.arguments = bundle
        openConfirmCustomDate()
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
                    categoryType, incomeValue.replace(",", "").toDouble(), incomeDesc)
                if (!sharedCashBox.getString("CashBoxList", "").isNullOrEmpty()) {
                    val gson = Gson()
                    val type: Type = object : TypeToken<ArrayList<CashBoxViewModel?>?>() {}.type
                    var cashBoxList: ArrayList<CashBoxViewModel?> = gson.fromJson(sharedCashBox.getString("CashBoxList", ""), type)
                    cashBoxList.add(cashBoxViewModel)
                    val jsonCashBoxList = gson.toJson(cashBoxList)
                    editShareCashBox.putString("CashBoxList", jsonCashBoxList)
                    valueCashBox.text = convertValueToFormatMoney(cashBoxList.sumOf { it!!.incomeValue }.toString())
                }
                else {
                    val gson = Gson()
                    var cashBoxList: ArrayList<CashBoxViewModel?> = ArrayList()
                    cashBoxList.add(cashBoxViewModel)
                    val jsonCashBoxList = gson.toJson(cashBoxList)
                    editShareCashBox.putString("CashBoxList", jsonCashBoxList)
                    valueCashBox.text = convertValueToFormatMoney(cashBoxViewModel.incomeValue.toString())
                }
                editShareCashBox.apply()
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

    override fun onClickAddIncome() {
        val intent = Intent(this, AddPlanMoney::class.java)
        intent.putExtra("PlanMoneyType", PlanMoneyType.INCOME_TYPE)
        startActivityForResult(intent, RequestCode.Add_PLAN_MONEY)
    }

    override fun onClickAddExpenses() {
        val intent = Intent(this, AddPlanMoney::class.java)
        intent.putExtra("PlanMoneyType", PlanMoneyType.EXPENSES_TYPE)
        startActivityForResult(intent, RequestCode.Add_PLAN_MONEY)
    }

    override fun onItemClick(position: Int) {

    }
}