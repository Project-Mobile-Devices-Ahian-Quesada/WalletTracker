package Util

import android.content.Context

/**
 * Utilidad para detectar si es la primera vez que el usuario abre la app
 * Usa SharedPreferences para guardar el estado de forma persistente
 */
object FirstTimeHelper {

    private const val PREFS_NAME = "WalletTrackerPrefs"
    private const val KEY_FIRST_TIME = "isFirstTime"

    /**
     * Devuelve true si es la primera vez que se abre la app
     * Por defecto devuelve true (asumiendo que aún no ha entrado)
     */
    fun isFirstTime(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_FIRST_TIME, true)
    }

    /**
     * Marca que el usuario ya entró a la app
     * Después de llamar este método, isFirstTime() siempre devolverá false
     */
    fun setNotFirstTime(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_FIRST_TIME, false)
            .apply() // apply() es asíncrono y más eficiente que commit()
    }
}