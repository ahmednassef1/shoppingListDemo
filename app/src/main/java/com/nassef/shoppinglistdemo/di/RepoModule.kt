package com.nassef.shoppinglistdemo.di

import com.nassef.data.repository.ShoppingListRepoImp
import com.nassef.data.local.ShoppingListDao
import com.nassef.domain.repository.IShoppingListRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    fun provideShoppingListRepo(shoppingListDao: ShoppingListDao): IShoppingListRepo =
        ShoppingListRepoImp(shoppingListDao)
}