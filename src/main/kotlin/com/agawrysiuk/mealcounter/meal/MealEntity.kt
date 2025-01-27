package com.agawrysiuk.mealcounter.meal

import com.agawrysiuk.mealcounter.common.jpa.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "meal")
class MealEntity(
    val name: String,
    val mealDay: LocalDate,
) : BaseEntity()
