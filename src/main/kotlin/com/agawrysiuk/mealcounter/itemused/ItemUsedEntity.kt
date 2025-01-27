package com.agawrysiuk.mealcounter.itemused

import com.agawrysiuk.mealcounter.common.jpa.BaseEntity
import com.agawrysiuk.mealcounter.common.jpa.BasePropertiesPer100HundredGram
import jakarta.persistence.*
import java.math.BigInteger
import java.util.UUID

@Entity
@Table(name = "item_used")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, columnDefinition = "VARCHAR(10)")
open class ItemUsedEntity(
    var name: String,
    var grams: BigInteger,
    @Embedded
    val properties: BasePropertiesPer100HundredGram,
) : BaseEntity()

@Entity
@DiscriminatorValue("MEAL")
class MealItemUsedEntity(
    name: String,
    grams: BigInteger,
    properties: BasePropertiesPer100HundredGram,
    var mealId: UUID,
) : ItemUsedEntity(name, grams, properties)

@Entity
@DiscriminatorValue("RECIPE")
class RecipeItemUsedEntity(
    name: String,
    grams: BigInteger,
    properties: BasePropertiesPer100HundredGram,
    val recipeId: UUID,
) : ItemUsedEntity(name, grams, properties)

enum class ItemUsedType {
    MEAL,
    RECIPE,
}
