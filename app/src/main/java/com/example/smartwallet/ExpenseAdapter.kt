package com.example.smartwallet

import Entity.Expense
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwallet.databinding.ItemExpenseBinding
import Util.CurrencyHelper
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter(
    private val expenses: MutableList<Expense>,
    private val onEdit: (Expense) -> Unit,
    private val onDelete: (Expense) -> Unit
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseVH>() {

    class ExpenseVH(val binding: ItemExpenseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseVH {
        val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseVH(binding)
    }

    override fun onBindViewHolder(holder: ExpenseVH, position: Int) {
        val expense = expenses[position]
        with(holder.binding) {
            tvDescription.text = expense.Description
            tvAmount.text = "-${CurrencyHelper.format(expense.Amount)}"
            tvDate.text = SimpleDateFormat("dd MMM yyyy", Locale("es")).format(expense.Date)

            if (expense.PhotoBitmap != null) {
                ivExpensePhoto.setImageBitmap(expense.PhotoBitmap)
            } else {
                ivExpensePhoto.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            root.setOnClickListener { onEdit(expense) }
            btnDelete.setOnClickListener { onDelete(expense) }
        }
    }

    override fun getItemCount() = expenses.size

    fun updateList(newList: List<Expense>) {
        expenses.clear()
        expenses.addAll(newList)
        notifyDataSetChanged()
    }

    // NUEVA FUNCIÓN PARA SWIPE
    fun getExpenseAt(position: Int): Expense {
        return expenses[position]
    }

    fun removeItem(position: Int) {
        expenses.removeAt(position)
        notifyItemRemoved(position)
    }
}