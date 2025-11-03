package com.example.smartwallet

import Entity.Expense
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwallet.databinding.ItemExpenseBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ExpenseAdapter(
    private val expenses: List<Expense>,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemExpenseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expense = expenses[position]
        with(holder.binding) {
            txtDescription.text = expense.Description
            txtAmount.text = "-${expense.Amount}"
            txtDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(expense.Date)
            btnDelete.setOnClickListener { onDelete(expense.Id) }
        }
    }

    override fun getItemCount() = expenses.size
}