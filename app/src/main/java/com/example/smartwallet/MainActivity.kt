package com.example.smartwallet

import Controller.WalletController
import Entity.Expense
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwallet.databinding.ActivityMainBinding
import Util.CurrencyHelper
import Util.FirstTimeHelper
import kotlinx.coroutines.launch

/**
 * Pantalla principal de WalletTracker
 * Muestra saldo, lista de movimientos y permite añadir ingresos/gastos
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var controller: WalletController
    private lateinit var adapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = WalletController(this)
        setupRecyclerView()
        setupFabButtons()
        setupSearch()
        setupToolbarMenu()

        // Primera vez: mostrar onboarding
        if (FirstTimeHelper.isFirstTime(this)) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
            return
        }

        // Carga inicial
        loadExpenses()
        updateBalance()
    }

    override fun onResume() {
        super.onResume()
        loadExpenses()
        updateBalance()
    }

    /** Configura los FABs (añadir gasto y recargar) */
    private fun setupFabButtons() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddEditExpenseActivity::class.java))
        }

        binding.fabRefresh.setOnClickListener {
            loadExpenses()
            updateBalance()
            Toast.makeText(this, R.string.refresh_success, Toast.LENGTH_SHORT).show()
        }
    }

    /** Configura el campo de búsqueda */
    private fun setupSearch() {
        binding.searchView.addTextChangedListener { text ->
            filterExpenses(text.toString())
        }
    }

    /** Configura el menú de la toolbar (Settings) */
    private fun setupToolbarMenu() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    /** Configura la lista de movimientos con swipe y edición */
    private fun setupRecyclerView() {
        binding.rvExpenses.layoutManager = LinearLayoutManager(this)

        adapter = ExpenseAdapter(
            mutableListOf(),
            onEdit = { expense ->
                // Abre el formulario correcto según sea ingreso o gasto
                val intent = if (expense.isIncome) {
                    Intent(this, AddIncomeActivity::class.java)
                } else {
                    Intent(this, AddEditExpenseActivity::class.java)
                }
                intent.putExtra("expense", expense)
                startActivity(intent)
            },
            onDelete = { expense ->
                lifecycleScope.launch {
                    controller.deleteExpenseById(expense.id)
                    loadExpenses()
                    updateBalance()
                }
            }
        )

        binding.rvExpenses.adapter = adapter

        // Swipe para borrar
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val expense = adapter.getExpenseAt(viewHolder.bindingAdapterPosition)
                lifecycleScope.launch {
                    controller.deleteExpenseById(expense.id)
                    loadExpenses()
                    updateBalance()
                }
            }
        }).attachToRecyclerView(binding.rvExpenses)
    }

    /** Carga todos los movimientos desde la API */
    private fun loadExpenses() {
        lifecycleScope.launch {
            try {
                val list = controller.getExpenses().sortedByDescending { it.date }
                adapter.updateList(list)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /** Filtra los movimientos por descripción o monto */
    private fun filterExpenses(query: String) {
        lifecycleScope.launch {
            try {
                val all = controller.getExpenses()
                val filtered = all.filter {
                    it.description.contains(query, ignoreCase = true) ||
                            it.amount.toString().contains(query)
                }.sortedByDescending { it.date }
                adapter.updateList(filtered)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /** Actualiza el saldo en la tarjeta principal */
    private fun updateBalance() {
        lifecycleScope.launch {
            try {
                val balance = controller.getBalance()
                binding.tvBalance.text = CurrencyHelper.format(balance)
            } catch (e: Exception) {
                binding.tvBalance.text = CurrencyHelper.format(0.0)
            }
        }
    }
}