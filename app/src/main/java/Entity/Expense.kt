package Entity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * Modelo que representa un movimiento financiero (gasto o ingreso)
 *
 * - amount > 0  → Gasto
 * - amount < 0  → Ingreso (usamos negativo para que el saldo suba)
 * - isIncome = true → se muestra en verde con +
 * - photoBase64 → solo para gastos
 */
data class Expense(
    var id: String = "",
    var description: String = "",
    var amount: Double = 0.0,
    var date: String = Date().toInstant().toString(),
    var photoBase64: String? = null,
    var isIncome: Boolean = false
) : java.io.Serializable {

    /**
     * Convierte un Bitmap a Base64 y lo guarda en photoBase64
     * Usa calidad 70 para reducir tamaño sin perder mucha calidad
     */
    fun setPhotoFromBitmap(bitmap: Bitmap?) {
        photoBase64 = bitmap?.let {
            val baos = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
        }
    }

    /**
     * Convierte el photoBase64 de vuelta a Bitmap
     * Devuelve null si hay error o no hay foto
     */
    fun getPhotoBitmap(): Bitmap? {
        return try {
            photoBase64?.let {
                val bytes = Base64.decode(it, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            }
        } catch (e: Exception) {
            null
        }
    }
}