package com.example.cashorganizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.cashorganizer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initUI()
    }

    private fun initUI() {
        binding.customToolbar.ivAccount.setOnClickListener{
            Toast.makeText(this, "Account clicked", Toast.LENGTH_SHORT).show()
        }
    }
}