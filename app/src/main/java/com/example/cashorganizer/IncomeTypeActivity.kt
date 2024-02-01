package com.example.cashorganizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashorganizer.adapter.IncomeTypeAdapter
import com.example.cashorganizer.databinding.ActivityCashBoxBinding
import com.example.cashorganizer.databinding.ActivityIncomeTypeBinding
import com.example.cashorganizer.model.IncomeTypeViewModel

class IncomeTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIncomeTypeBinding
    private lateinit var icIncomeTypeBack: AppCompatImageView
    private lateinit var recyclerview: RecyclerView
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
        recyclerview = findViewById<RecyclerView>(R.id.recyclerviewIncomeType)
        recyclerview.layoutManager = LinearLayoutManager(this)
    }

    private fun setupValue() {
        val data = ArrayList<IncomeTypeViewModel>()
        for (i in 1..20) {
            data.add(IncomeTypeViewModel(R.drawable.ic_income_statement, "Item " + i))
        }
        val adapter = IncomeTypeAdapter(data)
        recyclerview.adapter = adapter
    }

    private fun setupClickListeners() {
        icIncomeTypeBack.setOnClickListener {
            finish()
        }
    }
}