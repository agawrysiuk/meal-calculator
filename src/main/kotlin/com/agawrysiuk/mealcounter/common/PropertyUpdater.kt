package com.agawrysiuk.mealcounter.common

import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

/**
 * Helper class to easily update the properties of beans or DTOs
 *
 * The helper checks if the values are equal and only updated the target property if the values are different
 */
class PropertyUpdater {
    var updated: Boolean = false
        private set

    infix fun <V> KMutableProperty0<V>.with(source: KProperty0<V>): Unit = with(source.get())

    infix fun <V> KMutableProperty0<V>.withNotNull(source: KProperty0<V?>) {
        val value = source.get()
        if (value != null) {
            with(value)
        }
    }

    infix fun <V> KMutableProperty0<V>.with(value: V) {
        if (isDifferent(get(), value)) {
            this.set(value)
            updated = true
        }
    }

    @Suppress("UNCHECKED_CAST") // Type erasure
    private fun <V> isDifferent(existingValue: V, newValue: V): Boolean =
        if (isComparable(existingValue, newValue)) {
            (existingValue as Comparable<Any>).compareTo(newValue as Any) != 0
        } else {
            existingValue != newValue
        }

    private fun <V> isComparable(existingValue: V, newValue: V) = existingValue != null &&
            newValue != null &&
            existingValue is Comparable<*> &&
            newValue is Comparable<*>

    companion object {
        fun update(block: PropertyUpdater.() -> Unit): Boolean {
            val updater = PropertyUpdater()
            block.invoke(updater)
            return updater.updated
        }
    }
}