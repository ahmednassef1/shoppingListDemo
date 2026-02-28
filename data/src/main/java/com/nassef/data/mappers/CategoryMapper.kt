package com.nassef.data.mappers

import com.nassef.core.data.mapper.Mapper
import com.nassef.data.entities.CategoryEntity
import com.nassef.domain.entities.Category

internal object CategoryMapper : Mapper<CategoryEntity , Category , Unit>() {
    override fun dtoToDomain(model: CategoryEntity): Category {
        return Category(model.name , model.icon , model.bkColor)
    }

    override fun domainToDto(model: Category): CategoryEntity {
        return CategoryEntity(model.name , model.icon , model.bkColor)
    }
}