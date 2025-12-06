package com.example.smartwallet

import Controller.WalletController
import Util.Currency
import Util.CurrencyHelper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwallet.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var controller: WalletController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = WalletController(this)
        loadCurrentSettings()

        binding.radioGroupCurrency.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbCRC -> CurrencyHelper.currentCurrency = Currency.CRC
                R.id.rbUSD -> CurrencyHelper.currentCurrency = Currency.USD
                R.id.rbEUR -> CurrencyHelper.currentCurrency = Currency.EUR
            }
        }

        binding.btnUpdateBalance.setOnClickListener {
            val text = binding.etNewBalance.text.toString()
            if (text.isNotEmpty()) {
                val newBalance = text.toDoubleOrNull()
                if (newBalance != null && newBalance >= 0) {
                    controller.setInitialBalance(newBalance)
                    finish()
                }
            }
        }

        binding.btnDeleteAll.setOnClickListener {
            controller.resetAllData()
            finish()
        }
    }

    private fun loadCurrentSettings() {
        when (CurrencyHelper.currentCurrency) {
            Currency.CRC -> binding.rbCRC.isChecked = true
            Currency.USD -> binding.rbUSD.isChecked = true
            Currency.EUR -> binding.rbEUR.isChecked = true
        }
    }
}