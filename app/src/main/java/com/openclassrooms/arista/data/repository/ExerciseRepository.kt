package com.openclassrooms.arista.data.repository

import android.database.sqlite.SQLiteException
import android.util.Log
import com.openclassrooms.arista.data.dao.ExerciseDtoDao
import com.openclassrooms.arista.data.entity.ExerciseDto
import com.openclassrooms.arista.domain.model.Exercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ExerciseRepository(private val exerciseDao: ExerciseDtoDao) {

    /**
     * Retrieves all exercises from the database.
     * Returns a Flow of DataResult<List<Exercise>>.
     */
    suspend fun allExercises(): Flow<DataResult<List<Exercise>>> {
        return exerciseDao.getAllExercises()
            .map<List<ExerciseDto>, DataResult<List<Exercise>>> { dtoList ->
                DataResult.Success(dtoList.map { Exercise.fromDto(it) })
            }
            .catch { e ->
                emit(DataResult.Error(Exception("Failed to fetch exercises from database", e)))
            }
    }

    /**
     * Adds a new exercise to the data source.
     *
     * @param exercise The Exercise object to add.
     * @throws IllegalArgumentException if the exercise conversion to DTO fails.
     */
    suspend fun addExercise(exercise: Exercise): DataResult<Long> {
        return try {
            val newId = exerciseDao.insertExercise(exercise.toDto())
            DataResult.Success(newId)
        } catch (e: SQLiteException) {
            DataResult.Error(e)
        } catch (e: IllegalArgumentException) { // Si toDto() peut lancer cette exception
            DataResult.Error(e)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }


    /**
     * Deletes an exercise from the data source.
     * @param exercise The Exercise object to delete.
     */
    suspend fun deleteExercise(exercise: Exercise): DataResult<Unit> {
        return try {
            exercise.id?.let { exerciseId ->
                exerciseDao.deleteExerciseById(id = exerciseId)
                DataResult.Success(Unit)
            } ?: run {
                DataResult.Error(IllegalArgumentException("Exercise ID cannot be null for deletion."))
            }
        } catch (e: SQLiteException) {
            DataResult.Error(e)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }}