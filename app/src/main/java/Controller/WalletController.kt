package Controller

import android.content.Context
import android.net.Uri
import Data.IDataManager
import Data.MemoryDataManager
import Entity.Expense
import com.example.smartwallet.R
import java.util.Date
import java.util.UUID

class WalletController(private val context: Context) {

    private val dataManager: IDataManager = MemoryDataManager

    fun setInitialBalance(amount: Double) {
        try {
            dataManager.setInitialBalance(amount)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_setting_balance))
        }
    }

    fun getBalance(): Double {
        return try {
            dataManager.getBalance()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_get_balance))
        }
    }

    fun addExpense(description: String, amount: Double, imageUri: Uri? = null) {
        try {
            val expense = Expense(
                UUID.randomUUID().toString(),
                description,
                amount,
                Date(),
                imageUri
            )
            dataManager.addExpense(expense)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_add_expense))
        }
    }

    fun getExpenses() : List<Expense> {
        try {
            return dataManager.getAllExpenses()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_get_expenses))
        }
    }

    fun deleteExpenseById(id: String) {
        try {
            dataManager.deleteExpenseById(id)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_delete_expense))
        }
    }

    fun resetAllData() {
        try {
            dataManager.resetData()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_reset_data))
        }
    }
}
