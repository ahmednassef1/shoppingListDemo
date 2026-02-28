package com.nassef.domain

import com.nassef.core.data.error.GeneralErrorHandlerImpl
import com.nassef.core.data.model.Resource
import com.nassef.domain.entities.Category
import com.nassef.domain.entities.Item
import com.nassef.domain.interactor.DeleteItemUC
import com.nassef.domain.repository.IShoppingListRepo
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteItemUCTest {

    private lateinit var repo: IShoppingListRepo
    private lateinit var useCase: DeleteItemUC

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testItem = Item(5, "Bread", Category("Bread", 0, 0), false)

    @Before
    fun setUp() {
        repo = mockk()
        useCase = DeleteItemUC(repo, GeneralErrorHandlerImpl())
    }

    @Test
    fun `invoke emits loading true, Success true, loading false on success`() = runTest(testDispatcher) {
        coJustRun { repo.deleteItem(testItem) }

        val results = useCase.invoke(body = testItem).toList()

        // Progress(true) → Success(true) → Progress(false)
        assertEquals(3, results.size)
        assertTrue((results[0] as Resource.Progress).loading)
        assertTrue((results[1] as Resource.Success<*>).model as Boolean)
        assertFalse((results[2] as Resource.Progress).loading)
    }

    @Test
    fun `invoke calls repo deleteItem with correct item`() = runTest(testDispatcher) {
        coJustRun { repo.deleteItem(testItem) }

        useCase.invoke(body = testItem).toList()

        coVerify(exactly = 1) { repo.deleteItem(testItem) }
    }

    @Test
    fun `invoke with null body emits Failure without calling repo`() = runTest(testDispatcher) {
        val results = useCase.invoke(body = null).toList()

        assertTrue(results.any { it is Resource.Failure })
        coVerify(exactly = 0) { repo.deleteItem(any()) }
    }

    @Test
    fun `invoke emits Failure and loading false when repo throws`() = runTest(testDispatcher) {
        coJustRun { repo.deleteItem(any()) }
        io.mockk.coEvery { repo.deleteItem(testItem) } throws RuntimeException("Delete failed")

        val results = useCase.invoke(body = testItem).toList()

        assertTrue(results.any { it is Resource.Failure })
        assertFalse((results.last() as Resource.Progress).loading)
    }
}
