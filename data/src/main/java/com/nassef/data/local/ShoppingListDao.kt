package com.nassef.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.nassef.data.entities.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shoppingList")
    fun getAllShoppingList(): Flow<List<ItemEntity>>

    //insert or update items
    @Upsert
    suspend fun upsertShoppingListItem(item: ItemEntity)

    @Upsert
    suspend fun upsertAllShoppingItems(shoppingList: List<ItemEntity>)

    @Query("DELETE FROM shoppingList WHERE id = :itemId")
    suspend fun deleteItemById(itemId: Int)

    @Delete
    suspend fun deleteItem(item: ItemEntity)

    @Query("DELETE FROM shoppingList")
    suspend fun deleteShoppingList()
}