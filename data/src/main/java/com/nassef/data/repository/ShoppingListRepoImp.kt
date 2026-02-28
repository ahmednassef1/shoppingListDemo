package com.nassef.data.repository

import com.nassef.data.local.ShoppingListDao
import com.nassef.data.mappers.ItemMapper
import com.nassef.domain.entities.Item
import com.nassef.domain.repository.IShoppingListRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShoppingListRepoImp(val shoppingListDao: ShoppingListDao) : IShoppingListRepo {
    override suspend fun addItem(item: Item) {
        shoppingListDao.upsertShoppingListItem(ItemMapper.domainToDto(item))
    }

    override suspend fun deleteItem(item: Item) {
        shoppingListDao.deleteItem(ItemMapper.domainToDto(item))
    }

    override suspend fun deleteItemById(id: Int) {
        shoppingListDao.deleteItemById(id)
    }

    override fun getShoppingList(): Flow<List<Item>> {
        return shoppingListDao.getAllShoppingList().map { ItemMapper.dtoToDomain(it) }
    }
}