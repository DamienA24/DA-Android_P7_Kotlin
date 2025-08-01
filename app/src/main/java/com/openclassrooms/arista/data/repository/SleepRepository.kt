package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.SleepDtoDao
import com.openclassrooms.arista.data.entity.SleepDto
import com.openclassrooms.arista.domain.model.Sleep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
class SleepRepository(private val sleepDao: SleepDtoDao) {

    /**
     * Retrieves all sleeps from the database.
     * Returns a Flow of DataResult<List<Sleep>>.
     * If the operation is successful, it emits DataResult.Success with the list of sleeps.
     * If an error occurs, it emits DataResult.Error with the exception.
     */
    suspend fun allSleeps(): Flow<DataResult<List<Sleep>>> {
        return sleepDao.getAllSleeps()
            .map<List<SleepDto>, DataResult<List<Sleep>>> { dtoList ->
                DataResult.Success(dtoList.map { Sleep.fromDto(it) })
            }
            .catch { e ->
                emit(DataResult.Error(Exception("Failed to fetch sleeps from database", e)))
            }
    }
}