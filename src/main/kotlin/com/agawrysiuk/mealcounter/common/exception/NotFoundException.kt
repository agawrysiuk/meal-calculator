package com.agawrysiuk.mealcounter.common.exception

import java.util.UUID
import kotlin.reflect.KClass

class NotFoundException(
    id: UUID,
    clazz: KClass<*>
) : Exception("Entity of ${clazz.simpleName} with id of $id was not found.")