package com.agawrysiuk.mealcounter.recipe

import com.agawrysiuk.mealcounter.common.jpa.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "recipe")
class RecipeEntity(
    val name: String,
    val link: String?,
    val description: String?,
    val servings: Int,
) : BaseEntity()