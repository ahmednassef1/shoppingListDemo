package com.nassef.shoppinglistdemo.screens.shoppingList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassef.core.data.model.Resource
import com.nassef.domain.entities.Category
import com.nassef.domain.entities.Item
import com.nassef.domain.interactor.DeleteItemUC
import com.nassef.domain.interactor.GetShoppingListUC
import com.nassef.domain.interactor.UpsertItemUC
import com.nassef.shoppinglistdemo.util.UiManager
import com.nassef.shoppinglistdemo.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val shoppingList: List<Item> = emptyList()
)

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    val uiManager: UiManager,
    val upsertItemUC: UpsertItemUC,
    val getShoppingListUC: GetShoppingListUC,
    val deleteItemUC: DeleteItemUC
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _isSuccess = MutableStateFlow(false)
    private val _shoppingList = MutableStateFlow(emptyList<Item>())
    private val _filteredShoppingList = MutableStateFlow(_shoppingList.value)
    private val _isFiltering = MutableStateFlow(false)

    val shoppingList = getShoppingListUC.invoke().map {
        when (it) {
            is Resource.Failure -> {
                sendMsg(it.exception.message ?: "something went wrong")

            }

            is Resource.Progress<*> -> {
                updateLoadingState(it.loading)
//                _isLoading.value = it.loading
            }

            is Resource.Success<List<Item>> -> {
//                _shoppingList.value = it.model
                _shoppingList.update { _ ->
                    it.model
                }
            }
        }
    }

    val uiState =
        combine(
            _isLoading,
            _isSuccess,
            _shoppingList,
            _filteredShoppingList
        ) { isLoading, isSuccess, _shoppingList, filteredShoppingList ->

            var shoppingList = _shoppingList
            if (_isFiltering.value)
                shoppingList = filteredShoppingList

            UiState(
                isLoading = isLoading,
                isSuccess = isSuccess,
                shoppingList = shoppingList
            )

        }.stateIn(viewModelScope, WhileUiSubscribed, UiState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingList.collect()
        }
    }


    fun addShoppingItem(name: String, category: Category, status: Boolean) {
        val shoppingItem = Item(name = name, category = category, completionStatus = status)
        upsertShoppingItem(shoppingItem)
    }

    fun updateLoadingState(status: Boolean) {
        _isLoading.update { _ ->
            status
        }
    }

    fun upsertShoppingItem(item: Item) {
        upsertItemUC.invoke(scope = viewModelScope, item) {
            when (it) {
                is Resource.Failure -> sendMsg(it.exception.message ?: "something went wrong")
                is Resource.Progress<*> -> {
                    updateLoadingState(it.loading)
                }

                is Resource.Success<Boolean> -> {
                    _isSuccess.update { _ ->
                        it.model
                    }

                }
            }
        }
    }

    fun deleteItem(item: Item) {
        deleteItemUC.invoke(scope = viewModelScope, item) {
            when (it) {
                is Resource.Failure -> sendMsg(it.exception.message ?: "something went wrong")
                is Resource.Progress<*> -> {
                    updateLoadingState(it.loading)
                }

                is Resource.Success<Boolean> -> _isSuccess.update { _ ->
                    it.model
                }
            }
        }

    }

    fun resetSuccess() {
        _isSuccess.value = false
    }

    fun filterShoppingList(category: Category) {
        _isFiltering.value = true
        _filteredShoppingList.value = _shoppingList.value.filter { it.category == category }
    }

    fun orderShoppingListAlphabetically() {
        _isFiltering.value = true
        _filteredShoppingList.value = _shoppingList.value.sortedBy {
            it.name
        }
    }

    fun resetFiltering() {
        _isFiltering.value = false
        _filteredShoppingList.value = _shoppingList.value
    }

    fun sendMsg(msg: String) {
        viewModelScope.launch {
            uiManager.sendMessage(msg)
        }
    }

}