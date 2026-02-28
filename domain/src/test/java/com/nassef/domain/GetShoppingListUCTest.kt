package com.nassef.domain

import com.nassef.core.data.error.GeneralErrorHandlerImpl
import com.nassef.core.data.model.Resource
import com.nassef.domain.entities.Category
import com.nassef.domain.entities.Item
import com.nassef.domain.interactor.GetShoppingListUC
import com.nassef.domain.repository.IShoppingListRepo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetShoppingListUCTest {

    private lateinit var repo: IShoppingListRepo
    private lateinit var useCase: GetShoppingListUC

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testCategory = Category("Milk", 0, 0)
    private val testItems = listOf(
        Item(1, "Milk", testCategory, false),
        Item(2, "Bread", testCategory, true)
    )

    @Before
    fun setUp() {
        repo = mockk()
        useCase = GetShoppingListUC(repo, GeneralErrorHandlerImpl(), )
    }

    @Test
    fun `invoke emits loading true first`() = runTest(testDispatcher) {
        every { repo.getShoppingList() } returns flowOf(testItems)

        val results = useCase.invoke().toList()

        assertTrue((results.first() as Resource.Progress).loading)
    }

    @Test
    fun `invoke emits Success with list after loading`() = runTest(testDispatcher) {
        every { repo.getShoppingList() } returns flowOf(testItems)

        val results = useCase.invoke().toList()

        val success = results.filterIsInstance<Resource.Success<List<Item>>>()
        assertEquals(1, success.size)
        assertEquals(testItems, success.first().model)
    }

    @Test
    fun `invoke emits loading false as last emission`() = runTest(testDispatcher) {
        every { repo.getShoppingList() } returns flowOf(testItems)

        val results = useCase.invoke().toList()

        assertFalse((results.last() as Resource.Progress).loading)
    }

    @Test
    fun `invoke emits exactly 3 items on success`() = runTest(testDispatcher) {
        every { repo.getShoppingList() } returns flowOf(testItems)

        val results = useCase.invoke().toList()

        // Progress(true) → Success → Progress(false)
        assertEquals(3, results.size)
    }

    @Test
    fun `invoke emits Failure when repo flow throws`() = runTest(testDispatcher) {
        every { repo.getShoppingList() } returns flow { throw RuntimeException("DB read error") }

        val results = useCase.invoke().toList()

        assertTrue(results.any { it is Resource.Failure })
    }

    @Test
    fun `invoke emits loading false after failure`() = runTest(testDispatcher) {
        every { repo.getShoppingList() } returns flow { throw RuntimeException("DB read error") }

        val results = useCase.invoke().toList()

        // Last emission must always be Progress(false) so UI never stays stuck loading
        assertFalse((results.last() as Resource.Progress).loading)
    }
}
