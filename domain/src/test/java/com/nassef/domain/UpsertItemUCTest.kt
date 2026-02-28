package com.nassef.domain

import com.nassef.core.data.error.GeneralErrorHandlerImpl
import com.nassef.core.data.model.Resource
import com.nassef.domain.entities.Category
import com.nassef.domain.entities.Item
import com.nassef.domain.interactor.UpsertItemUC
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

class UpsertItemUCTest {

    private lateinit var repo: IShoppingListRepo
    private lateinit var useCase: UpsertItemUC

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testItem = Item(0, "Milk", Category("Milk", 0, 0), false)

    @Before
    fun setUp() {
        repo = mockk()
        useCase = UpsertItemUC(repo, GeneralErrorHandlerImpl())
    }

    @Test
    fun `invoke with valid item emits loading true first`() = runTest(testDispatcher) {
        coJustRun { repo.addItem(testItem) }

        val results = useCase.invoke(body = testItem).toList()

        assertTrue((results.first() as Resource.Progress).loading)
    }

    @Test
    fun `invoke with valid item emits Success true`() = runTest(testDispatcher) {
        coJustRun { repo.addItem(testItem) }

        val results = useCase.invoke(body = testItem).toList()

        val success = results.filterIsInstance<Resource.Success<Boolean>>()
        assertEquals(1, success.size)
        assertTrue(success.first().model)
    }

    @Test
    fun `invoke with valid item emits loading false last`() = runTest(testDispatcher) {
        coJustRun { repo.addItem(testItem) }

        val results = useCase.invoke(body = testItem).toList()

        assertFalse((results.last() as Resource.Progress).loading)
    }

    @Test
    fun `invoke with valid item calls repo addItem`() = runTest(testDispatcher) {
        coJustRun { repo.addItem(testItem) }

        useCase.invoke(body = testItem).toList()

        coVerify(exactly = 1) { repo.addItem(testItem) }
    }

    @Test
    fun `invoke with null body emits Failure`() = runTest(testDispatcher) {
        val results = useCase.invoke(body = null).toList()

        assertTrue(results.any { it is Resource.Failure })
    }

    @Test
    fun `invoke with null body emits loading false after failure`() = runTest(testDispatcher) {
        val results = useCase.invoke(body = null).toList()

        assertFalse((results.last() as Resource.Progress).loading)
    }

    @Test
    fun `invoke with null body does not call repo`() = runTest(testDispatcher) {
        useCase.invoke(body = null).toList()

        coVerify(exactly = 0) { repo.addItem(any()) }
    }
}
