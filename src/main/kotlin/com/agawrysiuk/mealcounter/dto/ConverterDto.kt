package com.agawrysiuk.mealcounter.dto

import com.agawrysiuk.mealcounter.common.jpa.BasePropertiesPer100HundredGram
import com.agawrysiuk.mealcounter.item.ItemEntity
import com.agawrysiuk.mealcounter.itemused.ItemUsedEntity
import com.agawrysiuk.mealcounter.itemused.ItemUsedType
import com.agawrysiuk.mealcounter.itemused.MealItemUsedEntity
import com.agawrysiuk.mealcounter.itemused.RecipeItemUsedEntity
import com.agawrysiuk.mealcounter.meal.MealEntity
import com.agawrysiuk.mealcounter.recipe.RecipeEntity
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.util.*

object ConverterDto {

    // recipe
    fun mapToEntity(dto: RecipeDto): RecipeEntity =
        RecipeEntity(
            name = dto.name,
            servings = dto.servings,
            link = dto.link,
            description = dto.description,
        )

    fun mapToDto(entity: RecipeEntity, itemsUsed: List<ItemUsedDto>): RecipeDto = RecipeDto(
        id = entity.id,
        name = entity.name,
        itemsUsed = itemsUsed.toSet(),
        servings = entity.servings,
        link = entity.link,
        description = entity.description,
        sumCalories = itemsUsed.sumOf { it.properties.calories }.sumToBigInteger(),
        sumProtein = itemsUsed.sumOf { it.properties.protein }.sumToBigInteger(),
        sumFat = itemsUsed.sumOf { it.properties.fat }.sumToBigInteger(),
        sumCarbohydrates = itemsUsed.sumOf { it.properties.carbohydrates }.sumToBigInteger(),
    )

    // meal
    fun mapToEntity(dto: MealDto): MealEntity =
        MealEntity(
            mealDay = dto.mealDay,
            name = dto.name,
        )

    fun mapToDto(entity: MealEntity, itemsUsed: List<ItemUsedDto>): MealDto {
        return MealDto(
            id = entity.id,
            name = entity.name,
            itemsUsed = itemsUsed.toSet(),
            mealDay = entity.mealDay,
            sumCalories = itemsUsed.sumOf { it.properties.calories }.sumToBigInteger(),
            sumProtein = itemsUsed.sumOf { it.properties.protein }.sumToBigInteger(),
            sumFat = itemsUsed.sumOf { it.properties.fat }.sumToBigInteger(),
            sumCarbohydrates = itemsUsed.sumOf { it.properties.carbohydrates }.sumToBigInteger(),
        )
    }

    // item
    fun mapToEntity(dto: ItemDto): ItemEntity =
        ItemEntity(
            name = dto.name,
            properties = mapToEntity(dto.properties)
        )


    fun mapToDto(entity: ItemEntity): ItemDto =
        ItemDto(
            id = entity.id,
            name = entity.name,
            properties = mapToDto(entity.properties)
        )

    // item_used
    fun mapToEntity(itemUsed: ItemUsedDto, type: ItemUsedType, foreignId: UUID): ItemUsedEntity = when (type) {
        ItemUsedType.MEAL -> MealItemUsedEntity(
            name = itemUsed.name,
            grams = itemUsed.grams,
            properties = mapToEntity(itemUsed.properties),
            mealId = foreignId,
        )

        ItemUsedType.RECIPE -> RecipeItemUsedEntity(
            name = itemUsed.name,
            grams = itemUsed.grams,
            properties = mapToEntity(itemUsed.properties),
            recipeId = foreignId,
        )
    }

    fun mapToDto(entity: ItemUsedEntity): ItemUsedDto = ItemUsedDto(
        id = entity.id,
        name = entity.name,
        grams = entity.grams,
        properties = mapToDto(entity.properties)
    )

    // base properties
    fun mapToEntity(dto: BasePropertiesPer100HundredGramDto): BasePropertiesPer100HundredGram =
        BasePropertiesPer100HundredGram(
            calories = dto.calories,
            protein = dto.protein,
            fat = dto.fat,
            carbohydrates = dto.carbohydrates,
        )

    fun mapToDto(entity: BasePropertiesPer100HundredGram): BasePropertiesPer100HundredGramDto =
        BasePropertiesPer100HundredGramDto(
            calories = entity.calories,
            protein = entity.protein,
            fat = entity.fat,
            carbohydrates = entity.carbohydrates,
        )

    private fun BigDecimal.sumToBigInteger(): BigInteger = this.setScale(0, RoundingMode.HALF_UP).toBigInteger()
}