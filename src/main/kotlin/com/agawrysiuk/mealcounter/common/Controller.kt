package com.agawrysiuk.mealcounter.common

import com.agawrysiuk.mealcounter.common.events.ItemUsedDeletedEvent
import com.agawrysiuk.mealcounter.dto.*
import com.agawrysiuk.mealcounter.item.ItemService
import com.agawrysiuk.mealcounter.itemused.ItemUsedService
import com.agawrysiuk.mealcounter.itemused.ItemUsedType
import com.agawrysiuk.mealcounter.meal.MealService
import com.agawrysiuk.mealcounter.recipe.RecipeService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
class Controller(
    private val itemService: ItemService,
    private val mealService: MealService,
    private val recipeService: RecipeService,
    private val itemUsedService: ItemUsedService,
    private val eventPublisher: ApplicationEventPublisher,
) {

    @PostMapping("/item")
    fun createItem(@RequestBody itemDTO: ItemDto): ItemDto {
        return itemService.createItem(itemDTO)
    }

    @GetMapping("/item")
    fun getAllItems(): List<ItemDto> {
        return itemService.getAllItems()
    }
    
    @DeleteMapping("/item-used/{id}")
    fun deleteItem(@PathVariable id: UUID) {
        val modifiedMealId = itemUsedService.deleteById(id)
        eventPublisher.publishEvent(ItemUsedDeletedEvent(modifiedMealId))
    }

    @PutMapping("/item-used/{id}")
    fun updateItem(@PathVariable id: UUID, @RequestBody itemUsedDto: ItemUsedDto) {
        itemUsedService.update(id, itemUsedDto)
    }

    @PatchMapping("/item-used/move")
    fun moveFromOneMealToAnother(@RequestBody request: MoveUsedItemDto) {
        mealService.moveFromOneMealToAnother(request)
        eventPublisher.publishEvent(ItemUsedDeletedEvent(request.fromMealId))
    }

    @PostMapping("/recipe")
    fun createRecipe(@RequestBody recipeDto: RecipeDto): RecipeDto {
        return recipeService.createRecipe(recipeDto)
    }

    @GetMapping("/recipe")
    fun getAllRecipes(): List<RecipeDto> {
        return recipeService.getAllRecipes()
    }

    @PostMapping("/meal")
    fun createMeal(@RequestBody mealDTO: MealDto): MealDto {
        return mealService.createMeal(mealDTO)
    }

    @GetMapping("/meal")
    fun getAllMeals(): List<MealDto> {
        return mealService.getAllMeals()
    }

    @DeleteMapping("/meal/{id}")
    fun deleteMeal(@PathVariable id: UUID) {
        mealService.deleteMeal(id)
    }

    @PostMapping("/meal/{id}/items-used")
    fun addItemToMeal(@PathVariable id: UUID, @RequestBody itemUsed: ItemUsedDto) {
        itemUsedService.save(itemUsed, ItemUsedType.MEAL, id)
    }

    @GetMapping("/day")
    fun getAllMealsPerDay(@RequestParam date: LocalDate): DayDto {
        return mealService.findAllByDate(date)
    }

    @PostMapping("/day")
    fun copyToToday(@RequestParam fromDate: LocalDate, @RequestParam toDate: LocalDate): DayDto {
        return mealService.copyAllMealsFromDateToDate(fromDate, toDate)
    }
}