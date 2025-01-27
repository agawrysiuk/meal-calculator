package com.agawrysiuk.mealcounter.meal

import com.agawrysiuk.mealcounter.common.jpa.BaseRepository
import java.time.LocalDate

interface MealRepository : BaseRepository<MealEntity> {

    fun findAllByMealDay(day: LocalDate): List<MealEntity>
}
