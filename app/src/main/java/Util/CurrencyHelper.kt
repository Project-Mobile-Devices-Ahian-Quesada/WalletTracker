package Util

import java.text.NumberFormat
import java.util.*

object CurrencyHelper {
    var currentCurrency: Currency = Currency.CRC // ₡ por defecto

    fun getSymbol(): String = when (currentCurrency) {
        Currency.CRC -> "₡"
        Currency.USD -> "$"
        Currency.EUR -> "€"
    }

    fun format(amount: Double): String {
        return when (currentCurrency) {
            Currency.CRC -> "₡%,.2f".format(amount)
            Currency.USD -> "$%,.2f".format(amount)
            Currency.EUR -> "€%,.2f".format(amount)
        }
    }
}

enum class Currency {
    CRC, USD, EUR
}