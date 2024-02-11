package com.example.cashorganizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashorganizer.adapter.CategoryAdapter
import com.example.cashorganizer.databinding.ActivityCategoryBinding
import com.example.cashorganizer.model.CategoryViewModel
import com.example.cashorganizer.utilities.RecyclerviewCode

class CategoryActivity : AppCompatActivity(), CategoryAdapter.ItemClickListener {
    private lateinit var icCategoryBack: AppCompatImageView
    private lateinit var binding: ActivityCategoryBinding
    private lateinit var recyclerviewIncomeWork: RecyclerView
    private lateinit var recyclerviewIncomeAsset: RecyclerView
    private lateinit var recyclerviewIncomeOther: RecyclerView
    private val incomeWorkList: List<String> = listOf("เงินเดือน", "ค่าจ้าง", "ค่าล่วงเวลา", "โบนัส", "อื่นๆ")
    private val incomeAssetList: List<String> = listOf("ดอกเบี้ย", "เงินปันผล", "ค่าเช่า", "ค่าขายสินทรัพย์")
    private val incomeOtherList: List<String> = listOf("เงินรางวัล", "เงินของขวัญ", "ขายของ TikTok", "ค่าขายสินทรัพย์")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupValue()
        setupClickListeners()
    }

    private fun setupView() {
        icCategoryBack = findViewById(R.id.icCategoryBack)
        recyclerviewIncomeWork = findViewById(R.id.recyclerviewIncomeWork)
        recyclerviewIncomeAsset = findViewById(R.id.recyclerviewIncomeAsset)
        recyclerviewIncomeOther = findViewById(R.id.recyclerviewIncomeOther)
        recyclerviewIncomeWork.layoutManager = LinearLayoutManager(this)
        recyclerviewIncomeAsset.layoutManager = LinearLayoutManager(this)
        recyclerviewIncomeOther.layoutManager = LinearLayoutManager(this)
    }

    private fun setupValue() {
        val dataIncomeWork = ArrayList<CategoryViewModel>()
        for (incomeWork in incomeWorkList) {
            dataIncomeWork.add(CategoryViewModel(incomeWork))
        }
        val adapterIncomeWork = CategoryAdapter(dataIncomeWork, this, RecyclerviewCode.INCOME_WORK)
        recyclerviewIncomeWork.adapter = adapterIncomeWork

        val dataIncomeAsset = ArrayList<CategoryViewModel>()
        for (incomeAsset in incomeAssetList) {
            dataIncomeAsset.add(CategoryViewModel(incomeAsset))
        }
        val adapterIncomeAsset = CategoryAdapter(
            dataIncomeAsset,
            this,
            RecyclerviewCode.INCOME_ASSET
        )
        recyclerviewIncomeAsset.adapter = adapterIncomeAsset

        val dataIncomeOther = ArrayList<CategoryViewModel>()
        for (incomeOther in incomeOtherList) {
            dataIncomeOther.add(CategoryViewModel(incomeOther))
        }
        val adapterIncomeOther = CategoryAdapter(
            dataIncomeOther,
            this,
            RecyclerviewCode.INCOME_OTHER
        )
        recyclerviewIncomeOther.adapter = adapterIncomeOther
    }

    private fun setupClickListeners() {
        icCategoryBack.setOnClickListener {
            finish()
        }
    }

    override fun onItemClick(position: Int, incomeCode: Int) {
        val intent = Intent()
        var incomeName = ""
        when (incomeCode) {
            RecyclerviewCode.INCOME_WORK -> incomeName = incomeWorkList[position]
            RecyclerviewCode.INCOME_ASSET -> incomeName = incomeAssetList[position]
            RecyclerviewCode.INCOME_OTHER -> incomeName = incomeOtherList[position]
        }
        intent.putExtra("categoryType", incomeName)
        setResult(RESULT_OK, intent);
        finish()
    }
}