package Data

import Entity.Expense

interface IDataManager {
    fun setInitialBalance(amount: Double)
    fun getBalance(): Double
    fun addExpense(expense: Expense)
    fun updateExpense(updatedExpense: Expense)
    fun deleteExpenseById(id: String)
    fun getAllExpenses(): List<Expense>
    fun getExpenseById(id: String): Expense?
    fun resetData()
}