package com.nassef.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nassef.domain.entities.Category

@Entity(tableName = "shoppingList")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val category: CategoryEntity,
    val completionStatus: Boolean
)