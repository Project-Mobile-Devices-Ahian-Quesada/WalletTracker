package Controller

import android.content.Context
import android.graphics.Bitmap
import Data.IDataManager
import Data.MemoryDataManager
import Entity.Expense
import com.example.smartwallet.R
import java.util.*

class WalletController(private val context: Context) {

    private val dataManager: IDataManager = MemoryDataManager

    fun setInitialBalance(amount: Double) {
        dataManager.setInitialBalance(amount)
    }

    fun getBalance(): Double {
        return dataManager.getBalance()
    }

    fun addExpense(description: String, amount: Double, photoBitmap: Bitmap? = null) {
        val expense = Expense(
            id = UUID.randomUUID().toString(),
            description = description,
            amount = amount,
            date = Date(),
            photoBitmap = photoBitmap
        )
        dataManager.addExpense(expense)
    }

    fun getExpenses(): List<Expense> {
        return dataManager.getAllExpenses()
    }

    fun deleteExpenseById(id: String) {
        dataManager.deleteExpenseById(id)
    }

    fun resetAllData() {
        dataManager.resetData()
    }
}