package com.example.treecare.service

import android.content.Context
import com.google.gson.Gson

class ImagePreferenceManager(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("ListImageKerusakanPohon", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val editor = sharedPreferences.edit()

    operator fun set(key: String?, value: String?) {
        editor.putString(key, value)
        editor.apply()
    }

    fun getList(key: String?): List<String>? {
        val json = sharedPreferences.getString(key, null)
        return if (json != null) {
            gson.fromJson(json, Array<String>::class.java).toList()
        } else {
            null
        }
    }

    fun setList(key: String?, imageUrls: List<String>) {
        val json = gson.toJson(imageUrls)
        set(key, json)
    }

    fun addImageUriToList(key: String?, imageUrl: String) {
        val existingList = getList(key)?.toMutableList() ?: mutableListOf()
        existingList.add(imageUrl)
        setList(key, existingList)
    }

    fun removeImageUriFromList(key: String?, imageUrl: String) {
        val existingList = getList(key)?.toMutableList() ?: return
        existingList.remove(imageUrl)
        setList(key, existingList)
    }

    fun removeData() {
        editor.clear().apply()
    }
}
