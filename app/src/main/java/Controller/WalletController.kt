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
    private var currencySymbol: String = "₡" // Default

    fun setCurrency(symbol: String) {
        currencySymbol = symbol
    }

    fun getCurrency(): String = currencySymbol

    fun setInitialBalance(amount: Double) {
        try {
            dataManager.setInitialBalance(amount)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_setting_balance))
        }
    }

    fun getBalance(): Double = dataManager.getBalance()

    fun getFormattedBalance(): String = "$currencySymbol ${getBalance()}"

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

    fun getExpenses(): List<Expense> = dataManager.getAllExpenses()

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
            setInitialBalance(0.0)
            setCurrency("₡")
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.error_reset_data))
        }
    }
}