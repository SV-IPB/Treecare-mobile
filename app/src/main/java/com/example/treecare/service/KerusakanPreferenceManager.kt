package com.example.treecare.service

import android.content.Context
import com.example.treecare.service.model.RiwayatKerusakanPohonModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class KerusakanPreferenceManager(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("ListKerusakanPohon", Context.MODE_PRIVATE)

    private val editor = sharedPreferences.edit()

    fun <T> setList(key: String?, list: List<T>?) {
        val gson = Gson()
        val json = gson.toJson(list)
        set(key, json)
    }

    operator fun set(key: String?, value: String?) {
        editor.putString(key, value)
        editor.commit()
    }

    fun getList(key: String?): List<RiwayatKerusakanPohonModel?>? {
        val arrayItems: List<RiwayatKerusakanPohonModel>
        val serializedObject = sharedPreferences.getString(key, null)
        if (serializedObject != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<RiwayatKerusakanPohonModel?>?>() {}.type
            arrayItems = gson.fromJson(serializedObject, type)
            return arrayItems
        }
        return null
    }

    fun getAllLists(): Map<String, List<RiwayatKerusakanPohonModel?>?> {
        val allLists: MutableMap<String, List<RiwayatKerusakanPohonModel?>?> = mutableMapOf()

        val allKeys = sharedPreferences.all.keys

        val filteredKeys = allKeys.filter { it.startsWith("kerusakan-") }

        for (key in filteredKeys) {
            val list = getList(key)
            if (list != null) {
                allLists[key] = list
            }
        }

        return allLists
    }

    fun removeData(){
        editor.clear()
        editor.commit()
    }
}