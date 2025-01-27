package com.agawrysiuk.mealcounter.item

import com.agawrysiuk.mealcounter.common.exception.NotFoundException
import com.agawrysiuk.mealcounter.common.jpa.BasePropertiesPer100HundredGram
import com.agawrysiuk.mealcounter.dto.BasePropertiesPer100HundredGramDto
import com.agawrysiuk.mealcounter.dto.ConverterDto.mapToDto
import com.agawrysiuk.mealcounter.dto.ConverterDto.mapToEntity
import com.agawrysiuk.mealcounter.dto.ItemDto
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class ItemService(
    private val itemRepository: ItemRepository
) {

    @Transactional
    fun createItem(itemDTO: ItemDto): ItemDto {
        val item = mapToEntity(itemDTO)
        val savedItem = itemRepository.save(item)
        return mapToDto(savedItem)
    }

    fun getAllItems(): List<ItemDto> {
        return itemRepository.findAll().map { mapToDto(it) }
    }

    fun getItemById(id: UUID): ItemEntity {
        return itemRepository.findByIdOrNull(id) ?: throw NotFoundException(id, ItemEntity::class)
    }
}