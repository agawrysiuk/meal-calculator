package com.agawrysiuk.mealcounter.itemused

import com.agawrysiuk.mealcounter.common.PropertyUpdater.Companion.update
import com.agawrysiuk.mealcounter.common.exception.NotFoundException
import com.agawrysiuk.mealcounter.dto.ConverterDto.mapToDto
import com.agawrysiuk.mealcounter.dto.ConverterDto.mapToEntity
import com.agawrysiuk.mealcounter.dto.ItemUsedDto
import com.agawrysiuk.mealcounter.dto.MoveUsedItemDto
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ItemUsedService(
    private val itemUsedRepository: ItemUsedRepository,
) {

    fun save(itemUsed: ItemUsedDto, type: ItemUsedType, foreignId: UUID): ItemUsedDto {
        val entity = mapToEntity(itemUsed, type, foreignId)
        return mapToDto(itemUsedRepository.save(entity))
    }

    fun findAllByForeignIdAndType(foreignId: UUID, type: ItemUsedType): List<ItemUsedDto> {
        val entity = when (type) {
            ItemUsedType.MEAL -> itemUsedRepository.findAllByMealId(foreignId)
            ItemUsedType.RECIPE -> itemUsedRepository.findAllByRecipeId(foreignId)
        }
        return entity.map { mapToDto(it) }
    }

    fun deleteByMealId(mealId: UUID) {
        itemUsedRepository.deleteAllByMealId(mealId)
    }

    @Transactional
    fun moveFromOneMealToAnother(request: MoveUsedItemDto) {
        val item = getEntity(request.usedItemId) as MealItemUsedEntity
        item.mealId = request.toMealId
        itemUsedRepository.save(item)
    }

    private fun getEntity(id: UUID): ItemUsedEntity {
        return itemUsedRepository.findByIdOrNull(id) ?: throw NotFoundException(id, ItemUsedDto::class)
    }

    fun deleteById(id: UUID): UUID {
        val modifiedMealId = (getEntity(id) as MealItemUsedEntity).mealId
        itemUsedRepository.deleteById(id)
        return modifiedMealId
    }

    @Transactional
    fun update(id: UUID, itemUsedDto: ItemUsedDto) {
        val entity = getEntity(id) as MealItemUsedEntity
        val updated = update {
            entity::name with itemUsedDto::name
            entity::grams with itemUsedDto::grams
            entity.properties::fat with itemUsedDto.properties::fat
            entity.properties::calories with itemUsedDto.properties::calories
            entity.properties::carbohydrates with itemUsedDto.properties::carbohydrates
            entity.properties::protein with itemUsedDto.properties::protein
        }
        if (updated) {
            itemUsedRepository.save(entity)
        }
    }
}
