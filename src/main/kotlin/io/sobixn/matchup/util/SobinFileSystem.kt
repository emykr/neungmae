package io.sobixn.matchup.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.sobixn.matchup.util.party.PartySystemImpl.Party
import java.io.File
import java.io.IOException

class SobinFileSystem {
    private var fileData: String? = null
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Party::class.java, PartyTypeAdapter())
        .create()

    fun saveFile(fileName: String, data: Any) {
        try {
            val file = File(fileName)
            file.parentFile?.mkdirs() // Create directories if they do not exist
            val jsonData = gson.toJson(data)
            file.writeText(jsonData)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun <T> loadFile(fileName: String, classOfT: Class<T>): T? {
        return try {
            val file = File(fileName)
            val jsonData = file.readText()
            gson.fromJson(jsonData, classOfT)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun getFile(fileName: String, data: Any): String? {
        saveFile(fileName, data)
        fileData = File(fileName).readText()
        return fileData
    }

    fun save(fileName: String, data: Any): String? {
        saveFile(fileName, data)
        return File(fileName).readText()
    }
}