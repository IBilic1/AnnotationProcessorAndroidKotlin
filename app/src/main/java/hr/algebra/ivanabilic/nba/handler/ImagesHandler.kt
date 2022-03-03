package hr.algebra.ivanabilic.nba.handler

import android.content.Context
import android.util.Log
import hr.algebra.ivanabilic.nba.factory.createGetHttpUrlConnection
import java.io.File
import java.lang.Exception
import java.net.HttpURLConnection
import java.nio.file.Files
import java.nio.file.Paths


fun downloadImageAndStore(context: Context,url:String,filename:String):String?{
    val file: File = createFile(context,filename,".png")

    try {
        val con:HttpURLConnection=createGetHttpUrlConnection(url)
        Files.copy(con.inputStream, Paths.get(file.toURI()))
        return file.absolutePath
    }catch (e: Exception) {
        Log.e("DOWNLOAD IMAGE", e.message, e)
    }

    return null
}

fun createFile(context: Context, filename: String, ext: String): File {
    val dir = context.applicationContext.getExternalFilesDir(null)
    val file = File(dir, File.separator + filename + ext)
    if (file.exists()) {
        file.delete()
    }
    return file
}
