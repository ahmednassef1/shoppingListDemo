package com.nassef.domain.features.shoppingList.interactor

import com.nassef.core.domain.error.ErrorHandler
import com.nassef.core.domain.interactor.BaseUseCase
import com.nassef.domain.entities.Item
import com.nassef.domain.features.shoppingList.repository.IShoppingListRepo
import kotlinx.coroutines.flow.Flow

class GetShoppingListUC(private val repo: IShoppingListRepo , errorHandler: ErrorHandler) : BaseUseCase<List<Item> , Unit>(errorHandler) {
    override fun executeDS(body: Unit?): Flow<List<Item>> {
        return repo.getShoppingList()
    }
}