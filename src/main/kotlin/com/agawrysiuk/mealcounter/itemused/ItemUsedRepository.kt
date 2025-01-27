package com.agawrysiuk.mealcounter.itemused

import com.agawrysiuk.mealcounter.common.jpa.BaseRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ItemUsedRepository : BaseRepository<ItemUsedEntity> {

    @Query("SELECT i FROM RecipeItemUsedEntity i WHERE i.recipeId = ?1")
    fun findAllByRecipeId(recipeId: UUID): List<RecipeItemUsedEntity>
    @Query("SELECT i FROM MealItemUsedEntity i WHERE i.mealId = ?1")
    fun findAllByMealId(mealId: UUID): List<MealItemUsedEntity>
    @Modifying
    @Query("DELETE FROM MealItemUsedEntity i WHERE i.mealId = ?1")
    fun deleteAllByMealId(mealId: UUID)
}