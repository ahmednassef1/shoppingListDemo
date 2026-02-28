package com.nassef.shoppinglistdemo.di

import com.nassef.core.domain.error.ErrorHandler
import com.nassef.domain.interactor.DeleteItemByCodeUC
import com.nassef.domain.interactor.DeleteItemUC
import com.nassef.domain.interactor.GetShoppingListUC
import com.nassef.domain.interactor.UpsertItemUC
import com.nassef.domain.repository.IShoppingListRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {
    @Provides
    fun provideUpsertItemUC(repo: IShoppingListRepo, errorHandler: ErrorHandler): UpsertItemUC =
        UpsertItemUC(repo, errorHandler)

    @Provides
    fun provideDeleteItemUC(repo: IShoppingListRepo, errorHandler: ErrorHandler): DeleteItemUC =
        DeleteItemUC(repo, errorHandler)

    @Provides
    fun provideDeleteItemByUC(
        repo: IShoppingListRepo,
        errorHandler: ErrorHandler
    ): DeleteItemByCodeUC = DeleteItemByCodeUC(repo, errorHandler)

    @Provides
    fun provideGetShoppingListUC(
        repo: IShoppingListRepo,
        errorHandler: ErrorHandler
    ): GetShoppingListUC = GetShoppingListUC(repo, errorHandler)
}