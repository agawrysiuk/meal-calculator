package com.agawrysiuk.mealcounter.common.events

import com.agawrysiuk.mealcounter.meal.MealService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ApplicationEventListener(
    private val mealService: MealService,
) {

    @EventListener
    fun onItemUsedDeletedEvent(event: ItemUsedDeletedEvent) {
        mealService.deleteMealIfNoItemsUsedArePresent(event.mealId)
    }
}