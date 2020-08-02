package com.example.testassignment.DB

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Members {
    @TypeConverter
    fun fromCountryLangList(countryLang: ArrayList<Member?>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type = object :
            TypeToken<ArrayList<Member?>?>() {}.type
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toCountryLangList(countryLangString: String?): ArrayList<Member>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type = object :
            TypeToken<ArrayList<Member?>?>() {}.type
        return gson.fromJson<ArrayList<Member>>(
            countryLangString,
            type
        )
    }
}