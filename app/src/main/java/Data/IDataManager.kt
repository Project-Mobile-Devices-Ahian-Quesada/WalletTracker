package Data

import Entity.Expense
import Entity.Wallet

interface IDataManager {
    fun setInitialBalance(amount: Double)
    fun getBalance(): Double

    fun addExpense(expense: Expense)
    fun deleteExpenseById(id: String)
    fun getAllExpenses(): List<Expense>
    fun getExpenseById(id: String): Expense?

    fun resetData()
    fun getWallet(): Wallet
}
