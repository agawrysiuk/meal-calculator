package com.agawrysiuk.mealcounter.common.jpa

import jakarta.persistence.Embeddable
import java.math.BigDecimal

@Embeddable
class BasePropertiesPer100HundredGram(
    var calories: BigDecimal,
    var protein: BigDecimal,
    var fat: BigDecimal,
    var carbohydrates: BigDecimal,
)
