package com.agawrysiuk.mealcounter.recipe

import com.agawrysiuk.mealcounter.dto.ConverterDto.mapToDto
import com.agawrysiuk.mealcounter.dto.ConverterDto.mapToEntity
import com.agawrysiuk.mealcounter.dto.RecipeDto
import com.agawrysiuk.mealcounter.itemused.ItemUsedService
import com.agawrysiuk.mealcounter.itemused.ItemUsedType
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val itemUsedService: ItemUsedService,
) {

    @Transactional
    fun createRecipe(recipeDto: RecipeDto): RecipeDto {
        val recipeEntity = recipeRepository.save(mapToEntity(recipeDto))
        val itemsUsed = recipeDto.itemsUsed.map { itemUsedService.save(it, ItemUsedType.RECIPE, recipeEntity.id) }
        return mapToDto(recipeEntity, itemsUsed)
    }

    fun getAllRecipes(): List<RecipeDto> {
        return recipeRepository.findAll()
            .associateWith { itemUsedService.findAllByForeignIdAndType(it.id, ItemUsedType.RECIPE) }
            .map { (recipe, itemsUsed) -> mapToDto(recipe, itemsUsed) }
    }
}