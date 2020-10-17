package williamlopes.project.rtcontrol.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import williamlopes.project.rtcontrol.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


fun showImageUri(context: Context, fileName: String?): Uri? {
    return try {
        val fOut: OutputStream?
        val imageUri: Uri?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues()
            contentValues.apply {
                this.put(
                    MediaStore.MediaColumns.DISPLAY_NAME, getNameFile(
                        fileName, ".jpeg")
                )
                this.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                this.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fOut = imageUri?.let { resolver.openOutputStream(it) }
        } else {
            val path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ).absolutePath
            val file = fileName?.let {
                File(path, getNameFile(it, ".jpeg"))
            }
            fOut = FileOutputStream(file)
            imageUri = file?.let {
                FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".provider", it
                )
            }
        }
        fOut?.flush()
        fOut?.close()
        imageUri
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

private fun getNameFile(name: String?, extension: String): String {
    var nameFile: String?
    nameFile = if (name == null || name.isEmpty()) {
        "Ternium - ${getDateHourWithMinusSignal(Date()).toString()}.$extension"
    } else name + extension
    if (nameFile.contains("/")) {
        nameFile = nameFile.replace("/", "-")
    }
    return nameFile
}



