package com.agawrysiuk.mealcounter.helper

import com.agawrysiuk.mealcounter.item.ItemRepository
import com.agawrysiuk.mealcounter.itemused.ItemUsedRepository
import com.agawrysiuk.mealcounter.meal.MealRepository
import com.agawrysiuk.mealcounter.recipe.RecipeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
abstract class CommonIntegrationTest {

    @Autowired
    protected lateinit var itemRepository: ItemRepository

    @Autowired
    protected lateinit var mealRepository: MealRepository

    @Autowired
    protected lateinit var recipeRepository: RecipeRepository

    @Autowired
    protected lateinit var itemUsedRepository: ItemUsedRepository
}
