package com.example.smartwallet

import Controller.WalletController
import Util.PrefsUtil
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwallet.databinding.ActivityConfigurationBinding

class Configuration : AppCompatActivity() {

    private lateinit var binding: ActivityConfigurationBinding
    private lateinit var controller: WalletController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = WalletApp.getController(this)  // SINGLETON

        // Carga moneda actual
        binding.spinner.setSelection(getIndex(controller.getCurrency()))

        // Listener para cambiar moneda
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val symbol = parent.getItemAtPosition(pos).toString()
                controller.setCurrency(symbol)
                PrefsUtil.saveCurrency(this@Configuration, symbol)  // CONTEXTO OK
                Toast.makeText(this@Configuration, "Moneda cambiada a $symbol", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Botón cambiar saldo
        binding.txtnuevosaldoConf.text.clear()  // Limpia campo
        binding.btnCambiarSaldo.setOnClickListener {
            val nuevo = binding.txtnuevosaldoConf.text.toString().toDoubleOrNull() ?: 0.0
            if (nuevo > 0) {
                controller.setInitialBalance(nuevo)
                PrefsUtil.saveInitialBalance(this, nuevo)
                Toast.makeText(this, "Saldo actualizado a $nuevo", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Ingresa un saldo válido", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón borrar datos
        binding.btnborradatosConf.setOnClickListener {
            controller.resetAllData()
            PrefsUtil.saveInitialBalance(this, 0.0)
            PrefsUtil.saveCurrency(this, "₡")  // Reset moneda
            Toast.makeText(this, "Datos borrados", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getIndex(symbol: String): Int = when (symbol) {
        "₡" -> 0
        "$" -> 1
        "€" -> 2
        else -> 0
    }
}