package com.nassef.shoppinglistdemo

import com.nassef.core.data.error.GeneralErrorHandlerImpl
import com.nassef.domain.entities.Category
import com.nassef.domain.entities.Item
import com.nassef.domain.interactor.DeleteItemUC
import com.nassef.domain.interactor.GetShoppingListUC
import com.nassef.domain.interactor.UpsertItemUC
import com.nassef.domain.repository.IShoppingListRepo
import com.nassef.shoppinglistdemo.screens.shoppingList.ShoppingListViewModel
import com.nassef.shoppinglistdemo.util.UiManager
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class FakeShoppingListRepo : IShoppingListRepo {
    private val _items = MutableStateFlow<List<Item>>(emptyList())

    fun emitItems(items: List<Item>) {
        _items.value = items
    }

    override fun getShoppingList(): Flow<List<Item>> = _items

    override suspend fun addItem(item: Item) {
        _items.value += item
    }

    override suspend fun deleteItem(item: Item) {
        _items.value = _items.value.filter { it.id != item.id }
    }

    override suspend fun deleteItemById(id: Int) {
        _items.value = _items.value.filter { it.id != id }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ShoppingListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val errorHandler = GeneralErrorHandlerImpl()

    private val milkCategory = Category("Milk", 0, 0)
    private val fruitCategory = Category("Fruits", 0, 0)

    private val allItems = listOf(
        Item(1, "Milk", milkCategory, false),
        Item(2, "Apple", fruitCategory, false),
        Item(3, "Cheese", milkCategory, true)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(repo: FakeShoppingListRepo): ShoppingListViewModel =
        ShoppingListViewModel(
            uiManager = mockk(relaxed = true),
            upsertItemUC = UpsertItemUC(repo, errorHandler),
            getShoppingListUC = GetShoppingListUC(repo, errorHandler),
            deleteItemUC = DeleteItemUC(repo, errorHandler)
        )

    /**
     * Activates the uiState combine by subscribing to it in the background.
     *
     * uiState uses WhileSubscribed — the combine upstream only runs while
     * there is at least one active collector. Without this, state changes to
     * _shoppingList, _isSuccess, etc. never propagate to uiState.value.
     */
    private fun TestScope.activateUiState(viewModel: ShoppingListViewModel) {
        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }
    }

    /**
     * Yields to the IO thread pool and back.
     *
     * The ViewModel's init block runs shoppingList.collect() on Dispatchers.IO.
     * After repo.emitItems(), the StateFlow notification is processed on that
     * IO thread. This call suspends briefly on IO, giving the IO coroutine time
     * to run and update _shoppingList before the test asserts.
     */
    private suspend fun yieldToIo() {
        withContext(Dispatchers.IO) {}
    }

    // ── Initial state ────────────────────────────────────────────────────────

    @Test
    fun `initial uiState has empty list, no loading, no success`() = runTest(testDispatcher) {
        val repo = FakeShoppingListRepo()
        val viewModel = createViewModel(repo)

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertFalse(state.isSuccess)
        assertTrue(state.shoppingList.isEmpty())
    }

    // ── List population ──────────────────────────────────────────────────────

    @Test
    fun `uiState shoppingList reflects items emitted by repo`() = runTest(testDispatcher) {
        val repo = FakeShoppingListRepo()
        val viewModel = createViewModel(repo)
        activateUiState(viewModel)

        repo.emitItems(allItems)
        yieldToIo()
        advanceUntilIdle()

        assertEquals(allItems, viewModel.uiState.value.shoppingList)
    }

    @Test
    fun `uiState updates when repo emits a new list`() = runTest(testDispatcher) {
        val repo = FakeShoppingListRepo()
        val viewModel = createViewModel(repo)
        activateUiState(viewModel)

        repo.emitItems(allItems)
        yieldToIo()
        advanceUntilIdle()

        val extra = Item(4, "Butter", milkCategory, false)
        repo.emitItems(allItems + extra)
        yieldToIo()
        advanceUntilIdle()

        assertEquals(4, viewModel.uiState.value.shoppingList.size)
    }

    // ── Filtering ────────────────────────────────────────────────────────────

    @Test
    fun `filterShoppingList shows only items matching the given category`() =
        runTest(testDispatcher) {
            val repo = FakeShoppingListRepo()
            val viewModel = createViewModel(repo)
            activateUiState(viewModel)
            repo.emitItems(allItems)
            yieldToIo()
            advanceUntilIdle()

            viewModel.filterShoppingList(milkCategory)
            yieldToIo()

            val filtered = viewModel.uiState.value.shoppingList
            assertEquals(2, filtered.size)
            assertTrue(filtered.all { it.category == milkCategory })
        }

    @Test
    fun `filterShoppingList with category that has no matches returns empty list`() =
        runTest(testDispatcher) {
            val repo = FakeShoppingListRepo()
            val viewModel = createViewModel(repo)
            activateUiState(viewModel)
            repo.emitItems(allItems)
            yieldToIo()
            advanceUntilIdle()

            val emptyCategory = Category("Empty", 0, 0)
            viewModel.filterShoppingList(emptyCategory)
            yieldToIo()

            assertTrue(viewModel.uiState.value.shoppingList.isEmpty())
        }

    @Test
    fun `resetFiltering restores the full unfiltered list`() = runTest(testDispatcher) {
        val repo = FakeShoppingListRepo()
        val viewModel = createViewModel(repo)
        activateUiState(viewModel)
        repo.emitItems(allItems)
        yieldToIo()
        advanceUntilIdle()

        viewModel.filterShoppingList(milkCategory)
        viewModel.resetFiltering()

        assertEquals(allItems, viewModel.uiState.value.shoppingList)
    }

    // ── Success flag ─────────────────────────────────────────────────────────

    @Test
    fun `resetSuccess sets isSuccess to false`() = runTest(testDispatcher) {
        val repo = FakeShoppingListRepo()
        val viewModel = createViewModel(repo)

        viewModel.resetSuccess()

        assertFalse(viewModel.uiState.value.isSuccess)
    }

    @Test
    fun `addShoppingItem triggers isSuccess true then resetSuccess sets it false`() =
        runTest(testDispatcher) {
            val repo = FakeShoppingListRepo()
            val viewModel = createViewModel(repo)
            activateUiState(viewModel)
            advanceUntilIdle()

            viewModel.addShoppingItem("Bread", milkCategory, false)
            yieldToIo()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isSuccess)

            viewModel.resetSuccess()
            yieldToIo()
            assertFalse(viewModel.uiState.value.isSuccess)
        }

    // ── Delete ───────────────────────────────────────────────────────────────

    @Test
    fun `deleteItem removes the item from the list`() = runTest(testDispatcher) {
        val repo = FakeShoppingListRepo()
        val viewModel = createViewModel(repo)
        activateUiState(viewModel)
        repo.emitItems(allItems)
        yieldToIo()
        advanceUntilIdle()

        val itemToDelete = allItems[0]
        viewModel.deleteItem(itemToDelete)
        yieldToIo()
        advanceUntilIdle()

        val remaining = viewModel.uiState.value.shoppingList
        assertFalse(remaining.any { it.id == itemToDelete.id })
        assertEquals(2, remaining.size)
    }
}
