package williamlopes.project.rtcontrol.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.format.DateUtils
import androidx.core.content.FileProvider
import williamlopes.project.rtcontrol.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


fun downloadBitmapImage(context: Context, bitmap: Bitmap, fileName: String?): Uri? {
    return try {
        val fOut: OutputStream?
        val imageUri: Uri?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues()
            contentValues.apply {
                this.put(MediaStore.MediaColumns.DISPLAY_NAME, getNameFile(fileName,
                    null, ".jpeg"))
                this.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                this.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES
            )
            imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fOut = imageUri?.let { resolver.openOutputStream(it) }
        } else {
            val path = Objects.requireNonNull(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                )
            ).absolutePath
            val file = File(path, getNameFile(fileName, null, ".jpeg")
            )
            fOut = FileOutputStream(file)
            imageUri = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                file
            )
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
        assert(fOut != null)
        fOut!!.flush()
        fOut.close()
        imageUri
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

private fun getNameFile(name: String?, date: Date?, extension: String): String? {
    var nameFile: String
    nameFile = if (name == null || name.isEmpty()) {
        if (date != null) {
            "Ternium - ${getDateHourWithMinusSignal(date).toString()} $extension"
        } else {
            "Ternium - ${getDateHourWithMinusSignal(Date()).toString()} $extension"
        }
    } else name + extension
    if (nameFile.contains("/")) {
        nameFile = nameFile.replace("/", "-")
    }
    return nameFile
}



