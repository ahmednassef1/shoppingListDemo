package com.nassef.data.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nassef.data.entities.CategoryEntity
import java.lang.reflect.Type

class DbTypeConverters {
    @TypeConverter
    fun fromSource(category: CategoryEntity): String = Gson().toJson(category)

    @TypeConverter
    fun toSource(categoryJson: String): CategoryEntity {
        val type: Type = object : TypeToken<CategoryEntity>() {}.type
        return Gson().fromJson(categoryJson, type)
    }
}