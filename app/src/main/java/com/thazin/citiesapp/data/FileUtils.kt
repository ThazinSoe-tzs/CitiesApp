package com.thazin.citiesapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FileUtils {

    companion object {
        fun getCities(context: Context): List<City> {
            val fileInString: String =
                context.assets.open("cities.json").bufferedReader().use {
                    it.readText()
                }
            val listCity = object : TypeToken<List<City>>() {}.type
            val cityList: ArrayList<City> = Gson().fromJson(fileInString, listCity)
            return cityList.sortedBy { it.name }
        }
    }

}