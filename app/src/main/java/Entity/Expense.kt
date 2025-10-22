package Entity

import android.net.Uri
import java.util.Date

class Expense(
    private var id: String,
    private var description: String,
    private var amount: Double,
    private var date: Date,
    private var imageUri: Uri? = null  // Propiedad para el uso opcional de una imagen (Aún no se como implementar)
) {
    var Id: String
        get() = id
        set(value) { id = value }

    var Description: String
        get() = description
        set(value) { description = value }

    var Amount: Double
        get() = amount
        set(value) { amount = value }

    var Date: Date
        get() = date
        set(value) { date = value }

    var ImageUri: Uri?
        get() = imageUri
        set(value) { imageUri = value }
}
