package com.example.smartwallet

import Controller.WalletController
import Util.FirstTimeHelper
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smartwallet.databinding.ActivityOnboardingBinding
import kotlinx.coroutines.launch

/**
 * Pantalla de bienvenida que aparece SOLO la primera vez
 * Permite al usuario definir su saldo inicial
 */
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var controller: WalletController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = WalletController(this)
        setupContinueButton()
    }

    /** Configura el botón Continuar con validación y guardado del saldo inicial */
    private fun setupContinueButton() {
        binding.btnContinue.setOnClickListener {
            val text = binding.etInitialAmount.text.toString().trim()

            when {
                text.isEmpty() -> {
                    binding.etInitialAmount.error = getString(R.string.onboarding_empty_amount)
                }
                text.toDoubleOrNull().let { it == null || it <= 0 } -> {
                    binding.etInitialAmount.error = getString(R.string.onboarding_invalid_amount)
                }
                else -> {
                    val amount = text.toDouble()
                    saveInitialBalanceAndContinue(amount)
                }
            }
        }
    }

    /** Guarda el saldo inicial en la API y pasa a MainActivity */
    private fun saveInitialBalanceAndContinue(amount: Double) {
        lifecycleScope.launch {
            try {
                controller.saveInitialBalance(amount)
                FirstTimeHelper.setNotFirstTime(this@OnboardingActivity)
                startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
                finish()
            } catch (e: Exception) {
                binding.etInitialAmount.error = getString(R.string.onboarding_network_error)
            }
        }
    }
}