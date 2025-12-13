package Util

import java.text.NumberFormat
import java.util.*

/**
 * Utilidad para formatear montos según la moneda seleccionada
 * Soporta: CRC (₡), USD ($), EUR (€)
 */
object CurrencyHelper {

    // Moneda actual de la app (por defecto Colones)
    var currentCurrency: Currency = Currency.CRC

    /**
     * Devuelve el símbolo de la moneda actual
     * Ej: ₡, $, €
     */
    fun getSymbol(): String = when (currentCurrency) {
        Currency.CRC -> "₡"
        Currency.USD -> "$"
        Currency.EUR -> "€"
    }

    /**
     * Formatea un monto con el símbolo y separadores de miles
     * Ej: ₡12,500.00  |  $12,500.00  |  €12,500.00
     */
    fun format(amount: Double): String {
        val cleanAmount = if (amount < 0) kotlin.math.abs(amount) else amount
        return when (currentCurrency) {
            Currency.CRC -> "₡%,.2f".format(cleanAmount)
            Currency.USD -> "$%,.2f".format(cleanAmount)
            Currency.EUR -> "€%,.2f".format(cleanAmount)
        }
    }

    /**
     * Formatea un monto con signo + o - según sea ingreso o gasto
     * Usado en la lista principal
     */
    fun formatWithSign(amount: Double): String {
        val absAmount = kotlin.math.abs(amount)
        val formatted = when (currentCurrency) {
            Currency.CRC -> "₡%,.2f".format(absAmount)
            Currency.USD -> "$%,.2f".format(absAmount)
            Currency.EUR -> "€%,.2f".format(absAmount)
        }
        return if (amount < 0) "+$formatted" else "-$formatted"
    }
}

/** Monedas soportadas por la app */
enum class Currency {
    CRC, USD, EUR
}