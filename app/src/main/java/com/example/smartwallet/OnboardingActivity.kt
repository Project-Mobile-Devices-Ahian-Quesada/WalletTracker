package com.example.smartwallet

import Controller.WalletController
import Util.Util
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwallet.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var walletController: WalletController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        walletController = WalletController(this)

        // Si ya tiene saldo, va directo a Main
        if (walletController.getBalance() > 0) {
            Util.openActivity(this, MainActivity::class.java)
            finish()
            return
        }

        binding.btnContinue.setOnClickListener {
            val text = binding.etInitialAmount.text.toString()
            if (text.isNotEmpty()) {
                val amount = text.toDoubleOrNull()
                if (amount != null && amount > 0) {
                    walletController.setInitialBalance(amount)
                    Util.openActivity(this, MainActivity::class.java)
                    finish()
                }
            }
        }
    }
}