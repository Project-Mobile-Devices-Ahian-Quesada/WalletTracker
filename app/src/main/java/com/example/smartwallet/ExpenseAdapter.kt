package com.example.smartwallet

import Entity.Expense
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwallet.databinding.ItemExpenseBinding
import Util.CurrencyHelper
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adaptador para mostrar gastos e ingresos en la lista principal
 * - Ingresos: verde con +
 * - Gastos: rojo con -
 * - Fotos solo en gastos
 */
class ExpenseAdapter(
    private val expenses: MutableList<Expense>,
    private val onEdit: (Expense) -> Unit,
    private val onDelete: (Expense) -> Unit
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseVH>() {

    class ExpenseVH(val binding: ItemExpenseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseVH {
        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseVH(binding)
    }

    override fun onBindViewHolder(holder: ExpenseVH, position: Int) {
        val expense = expenses[position]

        with(holder.binding) {
            // Descripción
            tvDescription.text = expense.description

            // Monto: verde si es ingreso, rojo si es gasto
            if (expense.isIncome || expense.amount < 0) {
                tvAmount.text = "+${CurrencyHelper.format(kotlin.math.abs(expense.amount))}"
                tvAmount.setTextColor(android.graphics.Color.parseColor("#4ADE80")) // Verde
            } else {
                tvAmount.text = "-${CurrencyHelper.format(expense.amount)}"
                tvAmount.setTextColor(android.graphics.Color.parseColor("#EF4444")) // Rojo
            }

            // Fecha formateada (ej: 10 dic 2025)
            tvDate.text = try {
                val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                input.timeZone = TimeZone.getTimeZone("UTC")
                val date = input.parse(expense.date)
                val output = SimpleDateFormat("dd MMM yyyy", Locale("es"))
                output.timeZone = TimeZone.getDefault()
                output.format(date ?: Date())
            } catch (e: Exception) {
                expense.date.take(10) // fallback seguro
            }

            // Foto: solo visible en gastos con imagen
            if (!expense.isIncome && expense.photoBase64 != null) {
                expense.getPhotoBitmap()?.let { bitmap ->
                    ivExpensePhoto.setImageBitmap(bitmap)
                    ivExpensePhoto.visibility = View.VISIBLE
                } ?: run {
                    ivExpensePhoto.visibility = View.GONE
                }
            } else {
                ivExpensePhoto.visibility = View.GONE
            }

            // Acciones
            root.setOnClickListener { onEdit(expense) }
            btnDelete.setOnClickListener { onDelete(expense) }
        }
    }

    override fun getItemCount() = expenses.size

    /** Actualiza la lista completa y notifica cambios */
    fun updateList(newList: List<Expense>) {
        expenses.clear()
        expenses.addAll(newList)
        notifyDataSetChanged()
    }

    /** Obtiene el elemento en la posición dada */
    fun getExpenseAt(position: Int) = expenses[position]
}