package com.agawrysiuk.mealcounter.meal

import com.agawrysiuk.mealcounter.dto.ConverterDto.mapToDto
import com.agawrysiuk.mealcounter.dto.ConverterDto.mapToEntity
import com.agawrysiuk.mealcounter.dto.DayDto
import com.agawrysiuk.mealcounter.dto.MealDto
import com.agawrysiuk.mealcounter.dto.MoveUsedItemDto
import com.agawrysiuk.mealcounter.itemused.ItemUsedService
import com.agawrysiuk.mealcounter.itemused.ItemUsedType
import jakarta.transaction.Transactional
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
class MealService(
    private val mealRepository: MealRepository,
    private val itemUsedService: ItemUsedService,
) {

    @Transactional
    fun createMeal(mealDTO: MealDto): MealDto {
        val mealEntity = mealRepository.save(mapToEntity(mealDTO))
        val itemsUsed = mealDTO.itemsUsed.map { itemUsedService.save(it, ItemUsedType.MEAL, mealEntity.id) }
        return mapToDto(mealEntity, itemsUsed)
    }

    fun getAllMeals(): List<MealDto> {
        return mealRepository.findAll().toMealDto()
    }

    fun findAllByDate(date: LocalDate): DayDto {
        return DayDto(
            date = date,
            meals = mealRepository.findAllByMealDay(date).toMealDto()
        )
    }

    @Transactional
    fun copyAllMealsFromDateToDate(fromDate: LocalDate, toDate: LocalDate): DayDto {
        val meals = mealRepository.findAllByMealDay(fromDate).toMealDto()
        return DayDto(
            date = toDate,
            meals = meals
                .onEach { it.mealDay = toDate }
                .map { meal ->
                    val mealEntity = mealRepository.save(mapToEntity(meal))
                    val itemsUsed = meal.itemsUsed.map { itemUsedService.save(it, ItemUsedType.MEAL, mealEntity.id) }
                    mapToDto(mealEntity, itemsUsed)
                }
        )
    }

    @Transactional
    fun deleteMeal(mealId: UUID) {
        itemUsedService.deleteByMealId(mealId)
        mealRepository.deleteById(mealId)
    }

    private fun List<MealEntity>.toMealDto(): List<MealDto> {
        return this.associateWith { itemUsedService.findAllByForeignIdAndType(it.id, ItemUsedType.MEAL) }
            .map { (meal, itemsUsed) -> mapToDto(meal, itemsUsed) }
    }

    @Transactional
    fun moveFromOneMealToAnother(request: MoveUsedItemDto) {
        verifyExists(request.fromMealId)
        verifyExists(request.toMealId)
        itemUsedService.moveFromOneMealToAnother(request)
    }

    fun verifyExists(mealId: UUID) {
        if (!mealRepository.existsById(mealId)) {
            throw BadRequestException("Meal $mealId does not exist")
        }
    }

    fun deleteMealIfNoItemsUsedArePresent(mealId: UUID) {
        if (itemUsedService.findAllByForeignIdAndType(mealId, ItemUsedType.MEAL).isEmpty()) {
            mealRepository.deleteById(mealId)
        }
    }

    fun renameMeal(mealId: UUID, newName: String): MealDto {
        val meal = mealRepository.findById(mealId).orElseThrow { BadRequestException("Meal $mealId does not exist") }
        meal.name = newName
        return mapToDto(mealRepository.save(meal), itemUsedService.findAllByForeignIdAndType(mealId, ItemUsedType.MEAL))
    }
}
