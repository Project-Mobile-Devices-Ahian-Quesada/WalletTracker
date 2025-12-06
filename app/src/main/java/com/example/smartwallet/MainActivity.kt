package com.example.smartwallet

import Controller.WalletController
import Entity.Expense
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwallet.databinding.ActivityMainBinding
import Util.CurrencyHelper

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
        updateBalance()

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddEditExpenseActivity::class.java))
        }

        binding.searchView.addTextChangedListener { text ->
            filterExpenses(text.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        loadExpenses()
        updateBalance()
    }

    private fun setupRecyclerView() {
        adapter = ExpenseAdapter(
            mutableListOf(),
            onEdit = { expense ->
                val intent = Intent(this, AddEditExpenseActivity::class.java)
                intent.putExtra("expense", expense)
                startActivity(intent)
            },
            onDelete = { expense ->
                controller.deleteExpenseById(expense.Id)
                loadExpenses()
                updateBalance()
            }
        )

        binding.rvExpenses.adapter = adapter

        // Swipe para borrar
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val expense = adapter.getExpenseAt(viewHolder.adapterPosition)
                controller.deleteExpenseById(expense.Id)
                loadExpenses()
                updateBalance()
            }
        }).attachToRecyclerView(binding.rvExpenses)

        // Botón de configuración
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

    private fun loadExpenses() {
        val list = controller.getExpenses().sortedByDescending { it.Date }
        adapter.updateList(list)
    }

    private fun filterExpenses(query: String) {
        val all = controller.getExpenses()
        val filtered = all.filter {
            it.Description.contains(query, ignoreCase = true) ||
                    it.Amount.toString().contains(query)
        }.sortedByDescending { it.Date }
        adapter.updateList(filtered)
    }

    private fun updateBalance() {
        binding.tvBalance.text = CurrencyHelper.format(controller.getBalance())
    }
}