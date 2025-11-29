package Util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.*

object Util {
    fun openActivity(context: Context, target: Class<*>) {
        val intent = Intent(context, target)
        context.startActivity(intent)
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
        val file = File(context.getExternalFilesDir(null), "expense_${UUID.randomUUID()}.jpg")
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
        }
        return Uri.fromFile(file)
    }
}