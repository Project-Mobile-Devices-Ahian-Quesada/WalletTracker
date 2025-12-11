package com.example.smartwallet

import Controller.WalletController
import Util.Currency
import Util.CurrencyHelper
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smartwallet.databinding.ActivitySettingsBinding
import kotlinx.coroutines.launch

/**
 * Pantalla de configuración de WalletTracker
 * - Cambiar moneda de la app
 * - Borrar todos los movimientos (el saldo se mantiene)
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var controller: WalletController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = WalletController(this)

        loadCurrentCurrency()
        setupCurrencyChange()
        setupDeleteAllButton()
    }

    /** Carga la moneda seleccionada actualmente */
    private fun loadCurrentCurrency() {
        when (CurrencyHelper.currentCurrency) {
            Currency.CRC -> binding.rbCRC.isChecked = true
            Currency.USD -> binding.rbUSD.isChecked = true
            Currency.EUR -> binding.rbEUR.isChecked = true
        }
    }

    /** Cambia la moneda cuando el usuario selecciona otra opción */
    private fun setupCurrencyChange() {
        binding.radioGroupCurrency.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbCRC -> CurrencyHelper.currentCurrency = Currency.CRC
                R.id.rbUSD -> CurrencyHelper.currentCurrency = Currency.USD
                R.id.rbEUR -> CurrencyHelper.currentCurrency = Currency.EUR
            }
        }
    }

    /** Borra todos los movimientos pero mantiene el saldo actual */
    private fun setupDeleteAllButton() {
        binding.btnDeleteAll.setOnClickListener {
            lifecycleScope.launch {
                try {
                    controller.clearExpensesOnly()
                    Toast.makeText(
                        this@SettingsActivity,
                        R.string.delete_expenses_success,
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(
                        this@SettingsActivity,
                        R.string.delete_expenses_error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}