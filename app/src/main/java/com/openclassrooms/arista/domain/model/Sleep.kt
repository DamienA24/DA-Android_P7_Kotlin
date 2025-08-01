package com.openclassrooms.arista.domain.model

import com.openclassrooms.arista.data.entity.SleepDto
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

data class Sleep(var startTime: LocalDateTime, var duration: Int, var quality: Int) {
    companion object {
        fun fromDto(dto: SleepDto): Sleep {
            return Sleep(
                startTime = Instant.ofEpochMilli(dto.startTime)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime(),
                duration = dto.duration,
                quality = dto.quality
            )
        }
    }
}
