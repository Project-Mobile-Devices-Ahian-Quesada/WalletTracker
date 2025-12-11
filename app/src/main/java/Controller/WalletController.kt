package Controller

import Entity.Expense
import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.Date

/**
 * Controlador principal que maneja toda la comunicación con la API en Render
 * Todas las funciones son suspend y corren en hilo IO
 */
class WalletController(private val context: Context) {

    private val client = OkHttpClient()
    private val BASE_URL = "https://wallet-api-m312.onrender.com"
    private val JSON = "application/json; charset=utf-8".toMediaType()

    /** Guarda el saldo inicial del usuario (solo la primera vez) */
    suspend fun saveInitialBalance(amount: Double) = withContext(Dispatchers.IO) {
        try {
            val json = JSONObject().apply { put("amount", amount) }
            val body = json.toString().toRequestBody(JSON)
            val request = Request.Builder()
                .url("$BASE_URL/initial-balance")
                .post(body)
                .build()
            client.newCall(request).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Obtiene todos los movimientos (gastos e ingresos) desde la API */
    suspend fun getExpenses(): List<Expense> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url("$BASE_URL/expenses").build()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) return@withContext emptyList()

            val json = response.body?.string() ?: "[]"
            val array = JSONArray(json)
            val list = mutableListOf<Expense>()

            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    Expense(
                        id = obj.getString("id"),
                        description = obj.getString("description"),
                        amount = obj.getDouble("amount"),
                        date = obj.getString("date"),
                        photoBase64 = if (obj.isNull("photoBase64")) null else obj.getString("photoBase64"),
                        isIncome = obj.optBoolean("isIncome", false)
                    )
                )
            }
            list.sortedByDescending { it.date }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /** Añade un nuevo gasto (con o sin foto) */
    suspend fun addExpense(description: String, amount: Double, photoBitmap: Bitmap?) = withContext(Dispatchers.IO) {
        try {
            val json = JSONObject().apply {
                put("description", description)
                put("amount", amount)
                photoBitmap?.let {
                    val baos = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.JPEG, 70, baos)
                    val base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
                    put("photoBase64", base64)
                } ?: put("photoBase64", JSONObject.NULL)
            }
            val body = json.toString().toRequestBody(JSON)
            val request = Request.Builder()
                .url("$BASE_URL/expenses")
            .post(body)
                .build()
            client.newCall(request).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Actualiza un gasto existente */
    suspend fun updateExpense(expense: Expense) = withContext(Dispatchers.IO) {
        try {
            val json = JSONObject().apply {
                put("description", expense.description)
                put("amount", expense.amount)
                expense.photoBase64?.let { put("photoBase64", it) } ?: put("photoBase64", JSONObject.NULL)
            }
            val body = json.toString().toRequestBody(JSON)
            val request = Request.Builder()
                .url("$BASE_URL/expenses/${expense.id}")
                .put(body)
                .build()
            client.newCall(request).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Elimina un movimiento por su ID */
    suspend fun deleteExpenseById(id: String) = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$BASE_URL/expenses/$id")
                .delete()
                .build()
            client.newCall(request).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Borra TODOS los datos (saldo + movimientos) */
    suspend fun resetAllData() = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$BASE_URL/reset")
                .delete()
                .build()
            client.newCall(request).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Borra solo los movimientos, mantiene el saldo actual */
    suspend fun clearExpensesOnly() = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$BASE_URL/reset-expenses-only")
                .delete()
                .build()
            client.newCall(request).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Añade un ingreso (se guarda en la lista como amount negativo) */
    suspend fun addIncome(amount: Double, description: String = "Ingreso manual") = withContext(Dispatchers.IO) {
        try {
            val json = JSONObject().apply {
                put("amount", amount)
                put("description", description)
            }
            val body = json.toString().toRequestBody(JSON)
            val request = Request.Builder()
                .url("$BASE_URL/add-income")
                .post(body)
                .build()
            client.newCall(request).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Obtiene el saldo actual desde la API */
    suspend fun getBalance(): Double = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url("$BASE_URL/balance").build()
            val response = client.newCall(request).execute()
            val json = JSONObject(response.body?.string() ?: "{\"balance\":0}")
            json.getDouble("balance")
        } catch (e: Exception) {
            0.0
        }
    }
}