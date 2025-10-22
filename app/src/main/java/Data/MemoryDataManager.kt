package Data

import Entity.Expense
import Entity.Wallet

object MemoryDataManager : IDataManager {
    private val wallet = Wallet(0.0)

    override fun setInitialBalance(amount: Double) {
        wallet.InitialBalance = amount
    }

    override fun getBalance(): Double = wallet.getCurrentBalance()

    override fun addExpense(expense: Expense) {
        wallet.addExpense(expense)
    }

    override fun deleteExpenseById(id: String) {
        val exp = wallet.getExpenses().find { it.Id == id }
        if (exp != null) wallet.removeExpense(exp)
    }

    override fun getAllExpenses(): List<Expense> = wallet.getExpenses()

    override fun getExpenseById(id: String): Expense? {
        return wallet.getExpenses().find { it.Id == id }
    }

    override fun resetData() {
        wallet.reset()
    }

    override fun getWallet(): Wallet = wallet
}
