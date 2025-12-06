package Entity

import android.graphics.Bitmap
import java.util.*
import java.io.Serializable

class Expense(
    id: String = UUID.randomUUID().toString(),
    description: String = "",
    amount: Double = 0.0,
    date: Date = Date(),
    photoBitmap: Bitmap? = null
) : Serializable {

    var Id: String = id
    var Description: String = description
    var Amount: Double = amount
    var Date: Date = date
    var PhotoBitmap: Bitmap? = photoBitmap  // ← Ahora guardamos Bitmap
}