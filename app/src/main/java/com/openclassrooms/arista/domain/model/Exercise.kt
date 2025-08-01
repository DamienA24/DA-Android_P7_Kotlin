package com.openclassrooms.arista.domain.model

import com.openclassrooms.arista.data.entity.ExerciseDto
import java.time.LocalDateTime
import java.time.ZoneOffset

data class Exercise(
    val id: Long? = null,
    var startTime: LocalDateTime,
    var duration: Int,
    var category: ExerciseCategory,
    var intensity: Int
) {
    fun toDto(): ExerciseDto {
        return ExerciseDto(
            id = this.id ?: 0L,
            startTime = this.startTime.toEpochSecond(ZoneOffset.UTC),
            duration = this.duration,
            category = this.category.name,
            intensity = this.intensity
        )
    }
    companion object {
        fun fromDto(dto: ExerciseDto): Exercise {
            return Exercise(
                id = dto.id,
                startTime = LocalDateTime.ofEpochSecond(dto.startTime, 0, ZoneOffset.UTC),
                duration = dto.duration,
                category = ExerciseCategory.valueOf(dto.category),
                intensity = dto.intensity
            )
        }
    }
}