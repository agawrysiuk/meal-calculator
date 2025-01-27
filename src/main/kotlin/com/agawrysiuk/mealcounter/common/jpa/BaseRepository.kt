package com.agawrysiuk.mealcounter.common.jpa

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BaseRepository<T : BaseEntity> : JpaRepository<T, UUID>