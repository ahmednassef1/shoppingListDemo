package com.nassef.data.mappers

import com.nassef.core.data.mapper.Mapper
import com.nassef.data.entities.ItemEntity
import com.nassef.domain.entities.Item

internal object ItemMapper : Mapper<ItemEntity, Item, Unit>() {
    override fun dtoToDomain(model: ItemEntity): Item {
        return Item(
            model.id,
            model.name,
            CategoryMapper.dtoToDomain(model.category),
            model.completionStatus
        )
    }

    override fun domainToDto(model: Item): ItemEntity {
        return ItemEntity(
            model.id,
            model.name,
            CategoryMapper.domainToDto(model.category),
            model.completionStatus
        )
    }
}