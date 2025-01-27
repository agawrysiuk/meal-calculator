package com.agawrysiuk.mealcounter.item

import com.agawrysiuk.mealcounter.common.jpa.BaseEntity
import com.agawrysiuk.mealcounter.common.jpa.BasePropertiesPer100HundredGram
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "item")
class ItemEntity(
    val name: String,
    @Embedded
    val properties: BasePropertiesPer100HundredGram,
) : BaseEntity()
