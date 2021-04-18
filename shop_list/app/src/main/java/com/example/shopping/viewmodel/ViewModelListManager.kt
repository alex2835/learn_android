package com.example.shopping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.shopping.data.Note
import java.io.File
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.lang.Exception


typealias ItemNameWithMetrics = Pair<String, MutableList<String>>

/**
 * Control all data
 *
 * listOfLists    - список списков покупок (Насвание саисков)
 * notes          - список покупок, определяется именем списка списков
 * availableItems - список доступных измен и списоок доступних для них метрик
 * */
class ViewModelListManager(application: Application): AndroidViewModel(application)
{
    // Data
    private val file           = File(application.applicationContext.filesDir, "stored_data.txt")
    private var listOfLists    = MutableLiveData(mutableListOf<String>())
    private var notes          = MutableLiveData(mutableMapOf<String, MutableList<Note>>())
    private var availableItems = MutableLiveData(mutableListOf<ItemNameWithMetrics>())

    // Logic
    var activeListName = MutableLiveData("")

    init {
        deserialize()
    }

    fun getListOfLists() : MutableList<String> {
        return listOfLists.value!!
    }

    fun getAvailableItemList() : MutableList< Pair<String, MutableList<String>> > {
        return availableItems.value!!
    }

    fun getNoteListByName(list_name: String) : MutableList<Note> {
        if (!notes.value!!.contains(list_name))
            notes.value!!.put(list_name, mutableListOf())
        return notes.value!!.get(list_name)!!
    }

    fun removeNoteListByName(list_name: String) {
        if (notes.value!!.contains(list_name))
            notes.value!!.remove(list_name)
    }

    fun serialize() {
        var output = buildJsonObject {
            put("listOfLists", Json.encodeToJsonElement(listOfLists.value!!))
            put("notes", Json.encodeToJsonElement(notes.value!!))
            put("availableNames", Json.encodeToJsonElement(availableItems.value!!))
        }.toString()

        if (!file.exists())
            file.createNewFile()

        file.writeText(output)
    }

    fun deserialize() {
        if (file.exists()) {
            val json = Json.parseToJsonElement(file.readText())
            try {
                listOfLists.value = Json.decodeFromJsonElement(json.jsonObject["listOfLists"]!!)
            } catch (e: Exception) {}

            try {
                notes.value = Json.decodeFromJsonElement(json.jsonObject["notes"]!!)
            } catch (e: Exception) {}

            try {
                availableItems.value = Json.decodeFromJsonElement(json.jsonObject["availableNames"]!!)
            } catch (e: Exception) {}
        }
    }

}