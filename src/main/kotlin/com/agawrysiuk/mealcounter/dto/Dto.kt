package com.agawrysiuk.mealcounter.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.util.UUID

data class ItemUsedDto(
    val id: UUID? = null,
    val name: String,
    val grams: BigInteger,
    val properties: BasePropertiesPer100HundredGramDto
)

data class ItemDto(
    val id: UUID? = null,
    val name: String,
    val properties: BasePropertiesPer100HundredGramDto,
)

data class BasePropertiesPer100HundredGramDto(
    val calories: BigDecimal,
    val protein: BigDecimal,
    val fat: BigDecimal,
    val carbohydrates: BigDecimal
)

data class MealDto(
    val id: UUID? = null,
    var mealDay: LocalDate,
    val name: String,
    val itemsUsed: Set<ItemUsedDto>,
    val sumCalories: BigInteger? = null,
    val sumProtein: BigInteger? = null,
    val sumFat: BigInteger? = null,
    val sumCarbohydrates: BigInteger? = null,
)

data class RecipeDto(
    val id: UUID? = null,
    val name: String,
    val itemsUsed: Set<ItemUsedDto>,
    val servings: Int,
    val link: String? = null,
    val description: String? = null,
    val sumCalories: BigInteger? = null,
    val sumProtein: BigInteger? = null,
    val sumFat: BigInteger? = null,
    val sumCarbohydrates: BigInteger? = null,
)

data class DayDto(
    val date: LocalDate,
    val meals: List<MealDto>
)

data class MoveUsedItemDto(
    val usedItemId: UUID,
    val fromMealId: UUID,
    val toMealId: UUID,
)
