package com.example.smartwallet

import Controller.WalletController
import Entity.Expense
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smartwallet.databinding.ActivityAddIncomeBinding
import kotlinx.coroutines.launch

/**
 * Activity para añadir o editar un INGRESO
 * - Permite descripción opcional
 * - Cambia entre ingreso y gasto desde la toolbar
 */
class AddIncomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddIncomeBinding
    private lateinit var controller: WalletController
    private var editingIncome: Expense? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = WalletController(this)

        editingIncome = intent.getSerializableExtra("expense") as? Expense

        setupToolbar()
        loadIncomeDataIfEditing()
        setupSaveButton()
    }

    /** Configura la toolbar con título y botón de volver */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (editingIncome != null)
            getString(R.string.edit_income_title)
        else
            getString(R.string.add_income_title)
    }

    /** Si estamos editando, carga descripción y monto */
    private fun loadIncomeDataIfEditing() {
        editingIncome?.let { income ->
            binding.etDescription.setText(income.description)
            binding.etAmount.setText(kotlin.math.abs(income.amount).toString())
            binding.btnSave.text = getString(R.string.save_changes)
        }
    }

    /** Valida y guarda el ingreso (nuevo o editado) */
    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val description = binding.etDescription.text.toString().trim().ifEmpty { "Ingreso manual" }
            val amountText = binding.etAmount.text.toString().trim()

            if (amountText.isEmpty()) {
                Toast.makeText(this, R.string.empty_amount, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                Toast.makeText(this, R.string.invalid_amount, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    if (editingIncome != null) {
                        // Editar ingreso existente
                        editingIncome!!.apply {
                            this.description = description
                            this.amount = -amount // negativo para que sume al saldo
                        }
                        controller.updateExpense(editingIncome!!)
                    } else {
                        // Nuevo ingreso
                        controller.addIncome(amount, description)
                    }
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@AddIncomeActivity, R.string.network_error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /** Infla el menú con los botones (Gasto) (Ingreso) */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_type, menu)
        return true
    }

    /** Navega entre ingreso y gasto */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { finish(); true }
            R.id.action_income -> true // ya estamos aquí
            R.id.action_expense -> {
                startActivity(Intent(this, AddEditExpenseActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}