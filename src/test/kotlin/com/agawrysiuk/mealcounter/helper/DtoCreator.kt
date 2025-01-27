package com.agawrysiuk.mealcounter.helper

import com.agawrysiuk.mealcounter.dto.*
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.util.*

object DtoCreator {
    fun createMealDto(
        name: String = "Default Meal",
        mealDay: LocalDate = LocalDate.now(),
        itemsUsed: Set<ItemUsedDto> = setOf(createItemUsedDto()),
    ): MealDto {
        return MealDto(
            name = name,
            mealDay = mealDay,
            itemsUsed = itemsUsed,
        )
    }

    fun createRecipeDto(
        name: String = "Default Recipe",
        servings: Int = 1,
        itemsUsed: Set<ItemUsedDto> = setOf(createItemUsedDto()),
    ): RecipeDto {
        return RecipeDto(
            name = name,
            servings = servings,
            itemsUsed = itemsUsed,
        )
    }

    fun createItemDto(
        name: String = "Default Item",
        properties: BasePropertiesPer100HundredGramDto = createBaseBasePropertiesPer100HundredGramDto()
    ): ItemDto {
        return ItemDto(
            name = name,
            properties = properties
        )
    }

    fun createItemUsedDto(
        id: UUID = UUID.randomUUID(),
        name: String = "Default Item",
        grams: BigInteger = BigInteger("100"),
        properties: BasePropertiesPer100HundredGramDto = createBaseBasePropertiesPer100HundredGramDto()
    ): ItemUsedDto {
        return ItemUsedDto(
            id = id,
            name = name,
            grams = grams,
            properties = properties
        )
    }

    fun createBaseBasePropertiesPer100HundredGramDto(
        calories: BigDecimal = BigDecimal.TEN,
        protein: BigDecimal = BigDecimal.TEN,
        fat: BigDecimal = BigDecimal.TEN,
        carbohydrates: BigDecimal = BigDecimal.TEN,
    ): BasePropertiesPer100HundredGramDto = BasePropertiesPer100HundredGramDto(
        calories = calories,
        protein = protein,
        fat = fat,
        carbohydrates = carbohydrates,
    )
}