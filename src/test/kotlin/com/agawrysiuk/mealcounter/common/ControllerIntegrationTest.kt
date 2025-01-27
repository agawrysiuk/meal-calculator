package com.agawrysiuk.mealcounter.common

import com.agawrysiuk.mealcounter.dto.BasePropertiesPer100HundredGramDto
import com.agawrysiuk.mealcounter.dto.ItemUsedDto
import com.agawrysiuk.mealcounter.dto.MoveUsedItemDto
import com.agawrysiuk.mealcounter.helper.CommonIntegrationTest
import com.agawrysiuk.mealcounter.helper.DtoCreator.createBaseBasePropertiesPer100HundredGramDto
import com.agawrysiuk.mealcounter.helper.DtoCreator.createItemDto
import com.agawrysiuk.mealcounter.helper.DtoCreator.createMealDto
import com.agawrysiuk.mealcounter.helper.DtoCreator.createRecipeDto
import com.agawrysiuk.mealcounter.helper.EntityCreator.createItemEntity
import com.agawrysiuk.mealcounter.helper.EntityCreator.createMealEntity
import com.agawrysiuk.mealcounter.helper.EntityCreator.createMealItemUsedEntity
import com.agawrysiuk.mealcounter.helper.EntityCreator.createRecipeEntity
import com.agawrysiuk.mealcounter.helper.EntityCreator.createRecipeItemUsedEntity
import com.agawrysiuk.mealcounter.itemused.MealItemUsedEntity
import com.agawrysiuk.mealcounter.itemused.RecipeItemUsedEntity
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
class ControllerIntegrationTest : CommonIntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `GET item should get an item`() {
        val item = itemRepository.save(createItemEntity())
        mockMvc.perform(
            get("/api/item")
        )
            .andExpectAll(
                status().isOk,
                jsonPath("$[0].id").value(item.id.toString()),
                jsonPath("$[1]").doesNotHaveJsonPath(),
            )
    }

    @Test
    fun `POST item should create an item`() {
        val itemName = "New Item"
        val itemDto = createItemDto(name = itemName)
        mockMvc.perform(
            post("/api/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto))
        )
            .andExpect(status().isOk)

        val item = itemRepository.findAll().single()
        assertEquals(itemName, item.name)
    }

    @Test
    fun `GET recipe should get a recipe`() {
        val recipe = recipeRepository.save(createRecipeEntity())
        val itemUsed = itemUsedRepository.save(
            createRecipeItemUsedEntity(
                name = "Special Item",
                recipeId = recipe.id,
            )
        )
        mockMvc.perform(
            get("/api/recipe")
        )
            .andExpectAll(
                status().isOk,
                jsonPath("$[0].id").value(recipe.id.toString()),
                jsonPath("$[0].name").value(recipe.name),
                jsonPath("$[0].servings").value(recipe.servings),
                jsonPath("$[0].itemsUsed[0].id").value(itemUsed.id.toString()),
                jsonPath("$[0].itemsUsed[0].name").value(itemUsed.name),
                jsonPath("$[0].itemsUsed[0].grams").value(itemUsed.grams),
                jsonPath("$[0].itemsUsed[0].properties.fat").value(itemUsed.properties.fat.setScale(2)),
                jsonPath("$[0].itemsUsed[0].properties.carbohydrates").value(
                    itemUsed.properties.carbohydrates.setScale(
                        2
                    )
                ),
                jsonPath("$[0].itemsUsed[0].properties.protein").value(itemUsed.properties.protein.setScale(2)),
                jsonPath("$[0].itemsUsed[0].properties.calories").value(itemUsed.properties.calories.setScale(2)),
                jsonPath("$[0].itemsUsed[1].id").doesNotHaveJsonPath(),
                jsonPath("$[1].id").doesNotHaveJsonPath(),
            )
    }

    @Test
    fun `POST recipe should create a recipe`() {
        val recipeDto = createRecipeDto()
        mockMvc.perform(
            post("/api/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDto))
        )
            .andExpect(status().isOk)

        val recipe = recipeRepository.findAll().single()
        assertEquals(recipeDto.name, recipe.name)
        assertEquals(recipeDto.servings, recipe.servings)
        val itemUsed = itemUsedRepository.findAll().single() as RecipeItemUsedEntity
        assertEquals(recipe.id, itemUsed.recipeId)
        assertEquals(recipeDto.itemsUsed.single().name, itemUsed.name)
        assertEquals(recipeDto.itemsUsed.single().name, itemUsed.name)
        assertEquals(recipeDto.itemsUsed.single().grams, itemUsed.grams)
        assertEquals(recipeDto.itemsUsed.single().properties.fat, itemUsed.properties.fat.setScale(0))
        assertEquals(
            recipeDto.itemsUsed.single().properties.carbohydrates,
            itemUsed.properties.carbohydrates.setScale(0)
        )
        assertEquals(recipeDto.itemsUsed.single().properties.calories, itemUsed.properties.calories.setScale(0))
        assertEquals(recipeDto.itemsUsed.single().properties.protein, itemUsed.properties.protein.setScale(0))
    }

    @Test
    fun `GET meal should get a meal`() {
        val meal = mealRepository.save(createMealEntity())
        val itemUsed = itemUsedRepository.save(
            createMealItemUsedEntity(
                name = "Special Item",
                mealId = meal.id,
            )
        )
        mockMvc.perform(
            get("/api/meal")
        )
            .andExpectAll(
                status().isOk,
                jsonPath("$[0].id").value(meal.id.toString()),
                jsonPath("$[0].name").value(meal.name),
                jsonPath("$[0].mealDay").value(meal.mealDay.toString()),
                jsonPath("$[0].itemsUsed[0].id").value(itemUsed.id.toString()),
                jsonPath("$[0].itemsUsed[0].name").value(itemUsed.name),
                jsonPath("$[0].itemsUsed[0].grams").value(itemUsed.grams),
                jsonPath("$[0].itemsUsed[0].properties.fat").value(itemUsed.properties.fat.setScale(2)),
                jsonPath("$[0].itemsUsed[0].properties.carbohydrates").value(
                    itemUsed.properties.carbohydrates.setScale(
                        2
                    )
                ),
                jsonPath("$[0].itemsUsed[0].properties.protein").value(itemUsed.properties.protein.setScale(2)),
                jsonPath("$[0].itemsUsed[0].properties.calories").value(itemUsed.properties.calories.setScale(2)),
                jsonPath("$[0].itemsUsed[1].id").doesNotHaveJsonPath(),
                jsonPath("$[1].id").doesNotHaveJsonPath(),
            )
    }

    @Test
    fun `POST meal should create a meal`() {
        val mealDto = createMealDto()
        mockMvc.perform(
            post("/api/meal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().registerModule(JavaTimeModule()).writeValueAsString(mealDto))
        )
            .andExpect(status().isOk)

        val meal = mealRepository.findAll().single()
        assertEquals(mealDto.name, meal.name)
        assertEquals(mealDto.mealDay, meal.mealDay)
        val itemUsed = itemUsedRepository.findAll().single() as MealItemUsedEntity
        assertEquals(meal.id, itemUsed.mealId)
        assertEquals(mealDto.itemsUsed.single().name, itemUsed.name)
        assertEquals(mealDto.itemsUsed.single().grams, itemUsed.grams)
        assertEquals(mealDto.itemsUsed.single().properties.fat.setScale(0), itemUsed.properties.fat.setScale(0))
        assertEquals(
            mealDto.itemsUsed.single().properties.carbohydrates.setScale(0),
            itemUsed.properties.carbohydrates.setScale(0)
        )
        assertEquals(
            mealDto.itemsUsed.single().properties.calories.setScale(0),
            itemUsed.properties.calories.setScale(0)
        )
        assertEquals(mealDto.itemsUsed.single().properties.protein.setScale(0), itemUsed.properties.protein.setScale(0))
    }

    @Test
    fun `GET meals from day should get all meals for a given day`() {
        val meal = mealRepository.save(createMealEntity(mealDay = LocalDate.now()))
            .also {
                itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item",
                        mealId = it.id,
                    )
                )
            }
        val meal2 = mealRepository.save(createMealEntity(mealDay = LocalDate.now()))
            .also {
                itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item2",
                        mealId = it.id,
                    )
                )
            }
        val meal3 = mealRepository.save(createMealEntity(mealDay = LocalDate.now().minusDays(1)))
            .also {
                itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item3",
                        mealId = it.id,
                    )
                )
            }
        mockMvc.perform(
            get("/api/day?date=${LocalDate.now()}")
        )
            .andExpectAll(
                status().isOk,
                jsonPath("$.meals[*].id", hasSize<Any>(2)),
                jsonPath("$.meals[*].id", containsInAnyOrder(meal.id.toString(), meal2.id.toString())),
            )
    }

    @Test
    fun `DELETE meal with id should delete a given meal`() {
        val meal = mealRepository.save(createMealEntity(mealDay = LocalDate.now()))
            .also {
                itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item",
                        mealId = it.id,
                    )
                )
            }
        val meal2 = mealRepository.save(createMealEntity(mealDay = LocalDate.now()))
            .also {
                itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item2",
                        mealId = it.id,
                    )
                )
            }
        mockMvc.perform(
            delete("/api/meal/${meal.id}")
        )
            .andExpectAll(
                status().isOk,
            )

        val meals = mealRepository.findAll()
        assertEquals(1, meals.size)
        assertEquals(meal2.id, meals.first().id)
    }

    @Test
    fun `PUT copy to today should get all meals from a given day and copy it to today`() {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val meal = mealRepository.save(createMealEntity(mealDay = today))
            .also {
                itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item",
                        mealId = it.id,
                    )
                )
            }
        val meal2 = mealRepository.save(createMealEntity(mealDay = today))
            .also {
                itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item2",
                        mealId = it.id,
                    )
                )
            }
        val meal3 = mealRepository.save(createMealEntity(mealDay = yesterday))
            .let {
                val itemUsed = itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item3",
                        mealId = it.id,
                    )
                )
                Pair(it, itemUsed)
            }
        mockMvc.perform(
            post("/api/day?fromDate=${yesterday}&toDate=${today}")
        )
            .andExpectAll(
                status().isOk,
            )

        val meals = mealRepository.findAll()
        assertEquals(4, meals.size)
        val newMeal = meals.single { it.mealDay == today && it.id != meal.id && it.id != meal2.id }
        assertEquals(today, newMeal.mealDay)
        val itemInNewMeal = itemUsedRepository.findAllByMealId(newMeal.id).single()
        assertEquals(meal3.second.name, itemInNewMeal.name)
        assertEquals(meal3.second.grams, itemInNewMeal.grams)
        assertEquals(meal3.second.properties.fat.setScale(0), itemInNewMeal.properties.fat.setScale(0))
        assertEquals(meal3.second.properties.protein.setScale(0), itemInNewMeal.properties.protein.setScale(0))
        assertEquals(meal3.second.properties.calories.setScale(0), itemInNewMeal.properties.calories.setScale(0))
        assertEquals(
            meal3.second.properties.carbohydrates.setScale(0),
            itemInNewMeal.properties.carbohydrates.setScale(0)
        )
    }

    @Test
    fun `PUT item used from meal to meal should move item and not delete old meal`() {
        val today = LocalDate.now()
        val meal = mealRepository.save(createMealEntity(mealDay = today))
            .let {
                val itemUsed = itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item",
                        mealId = it.id,
                    )
                )
                itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Other Special Item",
                        mealId = it.id,
                    )
                )
                Pair(it, itemUsed)
            }
        val savedForFutureReference = UUID.fromString(meal.second.id.toString())
        val meal2 = mealRepository.save(createMealEntity(mealDay = today))
            .let {
                val itemUsed = itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item2",
                        mealId = it.id,
                    )
                )
                Pair(it, itemUsed)
            }

        val request = MoveUsedItemDto(
            usedItemId = meal.second.id,
            fromMealId = meal.first.id,
            toMealId = meal2.first.id,
        )

        mockMvc.perform(
            patch("/api/item-used/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpectAll(
                status().isOk,
            )

        val meals = mealRepository.findAll()
        assertEquals(2, meals.size)
        assertEquals(1, itemUsedRepository.findAllByMealId(meal.first.id).size)
        val itemsInMeal2 = itemUsedRepository.findAllByMealId(meal2.first.id)
        assertEquals(2, itemsInMeal2.size)
        assertTrue(itemsInMeal2.singleOrNull { it.id == savedForFutureReference } != null)
    }

    @Test
    fun `PUT item used from meal to meal should move item and delete old meal because is empty`() {
        val today = LocalDate.now()
        val meal = mealRepository.save(createMealEntity(mealDay = today))
            .let {
                val itemUsed = itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item",
                        mealId = it.id,
                    )
                )
                Pair(it, itemUsed)
            }
        val savedForFutureReference = UUID.fromString(meal.second.id.toString())
        val meal2 = mealRepository.save(createMealEntity(mealDay = today))
            .let {
                val itemUsed = itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item2",
                        mealId = it.id,
                    )
                )
                Pair(it, itemUsed)
            }

        val request = MoveUsedItemDto(
            usedItemId = meal.second.id,
            fromMealId = meal.first.id,
            toMealId = meal2.first.id,
        )

        mockMvc.perform(
            patch("/api/item-used/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpectAll(
                status().isOk,
            )

        val meals = mealRepository.findAll()
        assertEquals(1, meals.size)
        assertEquals(0, itemUsedRepository.findAllByMealId(meal.first.id).size)
        val itemsInMeal2 = itemUsedRepository.findAllByMealId(meal2.first.id)
        assertEquals(2, itemsInMeal2.size)
        assertTrue(itemsInMeal2.singleOrNull { it.id == savedForFutureReference } != null)
    }

    @Test
    fun `DELETE item used from meal and leave out the meal if it still has other items used`() {
        val today = LocalDate.now()
        val meal = mealRepository.save(createMealEntity(mealDay = today))
            .let {
                val itemUsed = itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item",
                        mealId = it.id,
                    )
                )
                itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Other Special Item",
                        mealId = it.id,
                    )
                )
                Pair(it, itemUsed)
            }

        mockMvc.perform(
            delete("/api/item-used/${meal.second.id}")
        )
            .andExpectAll(
                status().isOk,
            )

        val meals = mealRepository.findAll()
        assertEquals(1, meals.size)
        val itemsInMeal = itemUsedRepository.findAllByMealId(meal.first.id)
        assertEquals(1, itemsInMeal.size)
        assertEquals("Other Special Item", itemsInMeal.single().name)
    }

    @Test
    fun `DELETE item used from meal and delete meal because it is empty`() {
        val today = LocalDate.now()
        val meal = mealRepository.save(createMealEntity(mealDay = today))
            .let {
                val itemUsed = itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item",
                        mealId = it.id,
                    )
                )
                Pair(it, itemUsed)
            }

        mockMvc.perform(
            delete("/api/item-used/${meal.second.id}")
        )
            .andExpectAll(
                status().isOk,
            )

        val meals = mealRepository.findAll()
        assertEquals(0, meals.size)
        val allItemsUsed = itemUsedRepository.findAll()
        assertEquals(0, allItemsUsed.size)
    }

    @Test
    fun `PUT update item should update the item`() {
        val today = LocalDate.now()
        val meal = mealRepository.save(createMealEntity(mealDay = today))
            .let {
                val itemUsed = itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item",
                        mealId = it.id,
                    )
                )
                Pair(it, itemUsed)
            }

        val request = ItemUsedDto(
            name = "new name",
            grams = BigInteger("500"),
            properties = BasePropertiesPer100HundredGramDto(
                calories = BigDecimal("11"),
                protein = BigDecimal("22"),
                fat = BigDecimal("33"),
                carbohydrates = BigDecimal("44")
            )
        )

        mockMvc.perform(
            put("/api/item-used/${meal.second.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpectAll(
                status().isOk,
            )

        val meals = mealRepository.findAll()
        assertEquals(1, meals.size)
        val allItemsUsed = itemUsedRepository.findAll()
        assertEquals(1, allItemsUsed.size)
        val itemUsed = allItemsUsed.single()
        assertEquals(request.name, itemUsed.name)
        assertEquals(request.grams, itemUsed.grams)
        assertEquals(request.properties.protein.setScale(2), itemUsed.properties.protein.setScale(2))
        assertEquals(request.properties.fat.setScale(2), itemUsed.properties.fat.setScale(2))
        assertEquals(request.properties.carbohydrates.setScale(2), itemUsed.properties.carbohydrates.setScale(2))
        assertEquals(request.properties.calories.setScale(2), itemUsed.properties.calories.setScale(2))
    }

    @Test
    fun `POST add item to meal should add item to meal`() {
        val today = LocalDate.now()
        val meal = mealRepository.save(createMealEntity(mealDay = today))
            .let {
                val itemUsed = itemUsedRepository.save(
                    createMealItemUsedEntity(
                        name = "Special Item",
                        mealId = it.id,
                    )
                )
                Pair(it, itemUsed)
            }

        val request = ItemUsedDto(
            name = "New Item",
            grams = BigInteger("100"),
            properties = createBaseBasePropertiesPer100HundredGramDto()
        )

        mockMvc.perform(
            post("/api/meal/${meal.second.id}/items-used")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpectAll(
                status().isOk,
            )

        val meals = mealRepository.findAll()
        assertEquals(1, meals.size)
        val allItemsUsed = itemUsedRepository.findAll()
        assertEquals(2, allItemsUsed.size)
        assertEquals(1, allItemsUsed.filter { it.name == "New Item" }.size)
        assertEquals(1, allItemsUsed.filter { it.name == "Special Item" }.size)
    }
}