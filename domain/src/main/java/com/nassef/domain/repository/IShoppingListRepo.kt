package com.nassef.domain.repository

import com.nassef.domain.entities.Item
import kotlinx.coroutines.flow.Flow

interface IShoppingListRepo {
    suspend fun addItem(item: Item)
    suspend fun deleteItem(item: Item)
    suspend fun deleteItemById(id: Int)
    fun getShoppingList(): Flow<List<Item>>
}