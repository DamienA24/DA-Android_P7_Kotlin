package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.SleepDtoDao
import com.openclassrooms.arista.domain.model.Exercise
import com.openclassrooms.arista.domain.model.Sleep
import kotlinx.coroutines.flow.first

class SleepRepository(private val sleepDao: SleepDtoDao) {

    // Get all sleep records
    suspend fun allSleeps(): List<Sleep> {
        return sleepDao.getAllSleeps()
            .first() // Collect the first emission of the Flow
            .map { Sleep.fromDto(it) } // Convert every DTO in Exercise
    }
}