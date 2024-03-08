package com.example.cashorganizer

import android.content.Intent
import android.os.Bundle
import android.view.View.OnTouchListener
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashorganizer.adapter.IncomeTypeAdapter
import com.example.cashorganizer.databinding.ActivityIncomeTypeBinding
import com.example.cashorganizer.model.IncomeTypeViewModel


class IncomeTypeActivity : AppCompatActivity(), IncomeTypeAdapter.ItemClickListener {
    private lateinit var binding: ActivityIncomeTypeBinding
    private lateinit var icIncomeTypeBack: AppCompatImageView
    private lateinit var searchIncomeType: EditText
    private lateinit var recyclerview: RecyclerView
    private val incomeTypeList: List<String> = listOf("เงินสด", "เงินโอน", "ทรุมันนี่ วอลเล็ท", "เป๋าตัง")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIncomeTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupValue()
        setupClickListeners()
    }

    private fun setupView() {
        icIncomeTypeBack = findViewById(R.id.icIncomeTypeBack)
        searchIncomeType = findViewById(R.id.searchIncomeType)
        recyclerview = findViewById(R.id.recyclerviewIncomeType)
        recyclerview.layoutManager = LinearLayoutManager(this)
    }

    private fun setupValue() {
        val data = ArrayList<IncomeTypeViewModel>()
        for (incomeType in incomeTypeList) {
            data.add(IncomeTypeViewModel(incomeType))
        }
        val adapter = IncomeTypeAdapter(data, this)
        recyclerview.adapter = adapter
    }

    private fun setupClickListeners() {
        icIncomeTypeBack.setOnClickListener {
            finish()
        }

        searchIncomeType.setOnTouchListener(OnTouchListener { v, event ->
            v.isFocusable = true
            v.isFocusableInTouchMode = true
            false
        })
    }

    override fun onItemClick(position: Int) {
        val intent = Intent()
        intent.putExtra("incomeType", incomeTypeList[position])
        setResult(RESULT_OK, intent);
        finish()
    }
}