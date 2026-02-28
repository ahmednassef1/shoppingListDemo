package com.nassef.shoppinglistdemo.di

import android.content.Context
import androidx.room.Room
import com.nassef.data.local.ShoppingListDao
import com.nassef.data.local.ShoppingListDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideShoppingListDao(shoppingListDatabase: ShoppingListDatabase): ShoppingListDao =
        shoppingListDatabase.getShoppingListDao()

    @Provides
    @Singleton
    fun provideShoppingListDatabase(@ApplicationContext context: Context): ShoppingListDatabase =
        Room.databaseBuilder(
            context = context,
            klass = ShoppingListDatabase::class.java, name = "shoppingList_database"
        ).fallbackToDestructiveMigration(true).build()

}