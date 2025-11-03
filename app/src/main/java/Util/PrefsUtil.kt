package Util

import android.content.Context

object PrefsUtil {
    private const val PREFS_NAME = "SmartWalletPrefs"
    private const val KEY_INITIAL_BALANCE = "initial_balance"
    private const val KEY_CURRENCY = "currency"

    fun saveInitialBalance(context: Context, amount: Double) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putFloat(KEY_INITIAL_BALANCE, amount.toFloat()).apply()
    }

    fun getInitialBalance(context: Context): Double {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getFloat(KEY_INITIAL_BALANCE, 0.0f).toDouble()
    }

    fun saveCurrency(context: Context, symbol: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CURRENCY, symbol).apply()
    }

    fun getCurrency(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_CURRENCY, "₡") ?: "₡"
    }
}