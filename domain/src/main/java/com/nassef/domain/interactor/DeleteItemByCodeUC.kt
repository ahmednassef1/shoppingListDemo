package com.nassef.domain.interactor

import com.nassef.core.domain.error.ErrorHandler
import com.nassef.core.domain.interactor.BaseUseCase
import com.nassef.domain.repository.IShoppingListRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteItemByCodeUC(private val repo: IShoppingListRepo, errorHandler: ErrorHandler) :
    BaseUseCase<Boolean, Int>(errorHandler) {
    override fun executeDS(body: Int?): Flow<Boolean> = flow {
        repo.deleteItemById(body!!)
        emit(true)
    }
}