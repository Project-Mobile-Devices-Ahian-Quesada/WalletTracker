package com.example.smartwallet

import Controller.WalletController
import Entity.Expense
import Util.PrefsUtil
import Util.Util.Companion.openActivity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartwallet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var controller: WalletController
    private lateinit var adapter: ExpenseAdapter
    private var expenses = mutableListOf<Expense>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = WalletApp.getController(this)
        controller.setCurrency(PrefsUtil.getCurrency(this))

        // Carga saldo inicial
        if (controller.getBalance() == 0.0 && PrefsUtil.getInitialBalance(this) == 0.0) {
            showInitialBalanceDialog()
        } else if (PrefsUtil.getInitialBalance(this) != 0.0) {
            controller.setInitialBalance(PrefsUtil.getInitialBalance(this))
        }

        setupUI()
        loadData()
    }

    private fun setupUI() {
        updateBalanceText()

        // RecyclerView
        adapter = ExpenseAdapter(expenses) { id ->
            controller.deleteExpenseById(id)
            loadData()
        }
        binding.recyclerExpenses.layoutManager = LinearLayoutManager(this)
        binding.recyclerExpenses.adapter = adapter

        // Botón flotante
        binding.fabAddExpense.setOnClickListener {
            openActivity(this, AgregarGasto::class.java)
        }

        // Botón config
        binding.btnConfig.setOnClickListener {
            openActivity(this, Configuration::class.java)
        }

        // BÚSQUEDA:
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // No hacemos nada en submit (Enter)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterExpenses(newText ?: "")  // Usa "" si null
                return true
            }
        })
    }

    private fun loadData() {
        expenses.clear()
        expenses.addAll(controller.getExpenses())
        adapter.notifyDataSetChanged()
        updateBalanceText()
    }

    private fun updateBalanceText() {
        binding.txtSaldoMain.text = controller.getFormattedBalance()
    }

    private fun filterExpenses(query: String) {
        val filtered = controller.getExpenses().filter {
            it.Description.contains(query, ignoreCase = true) ||
                    it.Amount.toString().contains(query)
        }
        expenses.clear()
        expenses.addAll(filtered)
        adapter.notifyDataSetChanged()
    }

    private fun showInitialBalanceDialog() {
        val input = android.widget.EditText(this).apply {
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            hint = "Ej: 10000"
        }
        AlertDialog.Builder(this)
            .setTitle("Configura tu saldo inicial")
            .setMessage("¿Cuánto dinero tienes ahora?")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val amount = input.text.toString().toDoubleOrNull() ?: 0.0
                if (amount > 0) {
                    controller.setInitialBalance(amount)
                    PrefsUtil.saveInitialBalance(this, amount)
                    loadData()
                } else {
                    showInitialBalanceDialog()
                }
            }
            .setCancelable(false)
            .show()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}