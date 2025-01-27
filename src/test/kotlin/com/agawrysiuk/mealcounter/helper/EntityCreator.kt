package com.agawrysiuk.mealcounter.helper

import com.agawrysiuk.mealcounter.common.jpa.BasePropertiesPer100HundredGram
import com.agawrysiuk.mealcounter.item.ItemEntity
import com.agawrysiuk.mealcounter.itemused.ItemUsedEntity
import com.agawrysiuk.mealcounter.itemused.MealItemUsedEntity
import com.agawrysiuk.mealcounter.itemused.RecipeItemUsedEntity
import com.agawrysiuk.mealcounter.meal.MealEntity
import com.agawrysiuk.mealcounter.recipe.RecipeEntity
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.util.*

object EntityCreator {
    fun createMealEntity(name: String = "Default Meal", mealDay: LocalDate = LocalDate.now()): MealEntity {
        return MealEntity(
            name = name,
            mealDay = mealDay
        )
    }

    fun createRecipeEntity(name: String = "Default Recipe", servings: Int = 1): RecipeEntity {
        return RecipeEntity(
            name = name,
            servings = servings,
            link = null,
            description = null,
        )
    }

    fun createItemEntity(
        name: String = "Default Item",
        properties: BasePropertiesPer100HundredGram = createBaseBasePropertiesPer100HundredGram()
    ): ItemEntity {
        return ItemEntity(
            name = name,
            properties = properties
        )
    }

    fun createMealItemUsedEntity(
        name: String = "Default Item",
        grams: BigInteger = BigInteger("100"),
        properties: BasePropertiesPer100HundredGram = createBaseBasePropertiesPer100HundredGram(),
        mealId: UUID = UUID.randomUUID(),
    ): MealItemUsedEntity {
        return MealItemUsedEntity(
            name = name,
            grams = grams,
            properties = properties,
            mealId = mealId,
        )
    }

    fun createRecipeItemUsedEntity(
        name: String = "Default Item",
        grams: BigInteger = BigInteger("100"),
        properties: BasePropertiesPer100HundredGram = createBaseBasePropertiesPer100HundredGram(),
        recipeId: UUID = UUID.randomUUID(),
    ): ItemUsedEntity {
        return RecipeItemUsedEntity(
            name = name,
            grams = grams,
            properties = properties,
            recipeId = recipeId,
        )
    }

    fun createBaseBasePropertiesPer100HundredGram(
        calories: BigDecimal = BigDecimal.TEN,
        protein: BigDecimal = BigDecimal.TEN,
        fat: BigDecimal = BigDecimal.TEN,
        carbohydrates: BigDecimal = BigDecimal.TEN,
    ): BasePropertiesPer100HundredGram = BasePropertiesPer100HundredGram(
        calories = calories,
        protein = protein,
        fat = fat,
        carbohydrates = carbohydrates,
    )
}