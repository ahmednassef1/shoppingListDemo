package com.nassef.data.mappers

import com.nassef.data.entities.CategoryEntity
import com.nassef.data.entities.ItemEntity
import com.nassef.domain.entities.Category
import com.nassef.domain.entities.Item
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ItemMapperTest {

    private val testCategory = Category(name = "Milk", icon = 0, bkColor = 0xFF0000)
    private val testCategoryEntity = CategoryEntity(name = "Milk", icon = 0, bkColor = 0xFF0000)

    // region dtoToDomain (ItemEntity → Item)

    @Test
    fun `dtoToDomain maps id correctly`() {
        val entity = ItemEntity(id = 42, name = "Bread", category = testCategoryEntity, completionStatus = false)
        assertEquals(42, ItemMapper.dtoToDomain(entity).id)
    }

    @Test
    fun `dtoToDomain maps name correctly`() {
        val entity = ItemEntity(id = 1, name = "Olive Oil", category = testCategoryEntity, completionStatus = false)
        assertEquals("Olive Oil", ItemMapper.dtoToDomain(entity).name)
    }

    @Test
    fun `dtoToDomain maps category correctly`() {
        val entity = ItemEntity(id = 1, name = "Milk", category = testCategoryEntity, completionStatus = false)
        assertEquals(testCategory, ItemMapper.dtoToDomain(entity).category)
    }

    @Test
    fun `dtoToDomain maps completionStatus true`() {
        val entity = ItemEntity(id = 1, name = "Eggs", category = testCategoryEntity, completionStatus = true)
        assertTrue(ItemMapper.dtoToDomain(entity).completionStatus)
    }

    @Test
    fun `dtoToDomain maps completionStatus false`() {
        val entity = ItemEntity(id = 1, name = "Eggs", category = testCategoryEntity, completionStatus = false)
        assertFalse(ItemMapper.dtoToDomain(entity).completionStatus)
    }

    // endregion

    // region domainToDto (Item → ItemEntity)

    @Test
    fun `domainToDto maps id correctly`() {
        val item = Item(id = 99, name = "Butter", category = testCategory, completionStatus = false)
        assertEquals(99, ItemMapper.domainToDto(item).id)
    }

    @Test
    fun `domainToDto maps name correctly`() {
        val item = Item(id = 1, name = "Yogurt", category = testCategory, completionStatus = false)
        assertEquals("Yogurt", ItemMapper.domainToDto(item).name)
    }

    @Test
    fun `domainToDto maps category correctly`() {
        val item = Item(id = 1, name = "Milk", category = testCategory, completionStatus = false)
        assertEquals(testCategoryEntity, ItemMapper.domainToDto(item).category)
    }

    @Test
    fun `domainToDto maps completionStatus correctly`() {
        val purchased = Item(id = 1, name = "Milk", category = testCategory, completionStatus = true)
        val pending = Item(id = 2, name = "Eggs", category = testCategory, completionStatus = false)
        assertTrue(ItemMapper.domainToDto(purchased).completionStatus)
        assertFalse(ItemMapper.domainToDto(pending).completionStatus)
    }

    // endregion

    // region List mappings

    @Test
    fun `dtoToDomain with empty list returns empty list`() {
        val result = ItemMapper.dtoToDomain(emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `dtoToDomain maps list preserving order and count`() {
        val entities = listOf(
            ItemEntity(id = 1, name = "Milk", category = testCategoryEntity, completionStatus = false),
            ItemEntity(id = 2, name = "Bread", category = testCategoryEntity, completionStatus = true),
            ItemEntity(id = 3, name = "Butter", category = testCategoryEntity, completionStatus = false)
        )
        val result = ItemMapper.dtoToDomain(entities)
        assertEquals(3, result.size)
        assertEquals(1, result[0].id)
        assertEquals(2, result[1].id)
        assertEquals(3, result[2].id)
    }

    @Test
    fun `domainToDto maps list preserving order and count`() {
        val items = listOf(
            Item(id = 10, name = "Apple", category = testCategory, completionStatus = false),
            Item(id = 11, name = "Orange", category = testCategory, completionStatus = true)
        )
        val result = ItemMapper.domainToDto(items)
        assertEquals(2, result.size)
        assertEquals(10, result[0].id)
        assertEquals(11, result[1].id)
    }

    // endregion

    // region Round-trip

    @Test
    fun `round trip entity to domain and back preserves all data`() {
        val original = ItemEntity(id = 7, name = "Cheese", category = testCategoryEntity, completionStatus = true)
        val roundTripped = ItemMapper.domainToDto(ItemMapper.dtoToDomain(original))
        assertEquals(original, roundTripped)
    }

    @Test
    fun `round trip domain to entity and back preserves all data`() {
        val original = Item(id = 5, name = "Eggs", category = testCategory, completionStatus = false)
        val roundTripped = ItemMapper.dtoToDomain(ItemMapper.domainToDto(original))
        assertEquals(original, roundTripped)
    }

    // endregion
}
