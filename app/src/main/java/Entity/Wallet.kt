package Entity

class Wallet(
    private var initialBalance: Double = 0.0
) {
    private val expenses = mutableListOf<Expense>()

    var InitialBalance: Double
        get() = initialBalance
        set(value) { initialBalance = value }

    fun addExpense(expense: Expense) {
        expenses.add(expense)
    }

    fun removeExpense(expense: Expense) {
        expenses.remove(expense)
    }

    fun getExpenses(): List<Expense> = expenses

    fun getCurrentBalance(): Double {
        val totalSpent = expenses.sumOf { it.Amount }
        return initialBalance - totalSpent
    }

    fun reset() {
        expenses.clear()
        initialBalance = 0.0
    }
}
