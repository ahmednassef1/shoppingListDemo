package com.nassef.domain.interactor

import com.nassef.core.domain.error.ErrorHandler
import com.nassef.core.domain.interactor.BaseUseCase
import com.nassef.domain.entities.Item
import com.nassef.domain.repository.IShoppingListRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteItemUC(private val repo: IShoppingListRepo , errorHandler: ErrorHandler) : BaseUseCase<Boolean , Item>(errorHandler) {
    override fun executeDS(body: Item?): Flow<Boolean>  = flow {
        requireBody(body)
        repo.deleteItem(body!!)
        emit(true)
    }
}