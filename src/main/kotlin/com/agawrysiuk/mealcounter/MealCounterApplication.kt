package com.agawrysiuk.mealcounter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class MealCounterApplication

fun main(args: Array<String>) {
	runApplication<MealCounterApplication>(*args)
}
