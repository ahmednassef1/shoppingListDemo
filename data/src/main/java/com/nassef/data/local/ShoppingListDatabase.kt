package com.nassef.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nassef.data.entities.ItemEntity
import com.nassef.data.utils.DbTypeConverters

@Database(entities = [ItemEntity::class], version = 1, exportSchema = false)
@TypeConverters(DbTypeConverters::class)
abstract class ShoppingListDatabase : RoomDatabase() {
    abstract fun getShoppingListDao(): ShoppingListDao

}