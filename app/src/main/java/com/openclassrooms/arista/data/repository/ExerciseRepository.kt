package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.ExerciseDtoDao
import com.openclassrooms.arista.data.entity.ExerciseDto
import com.openclassrooms.arista.domain.model.Exercise
import kotlinx.coroutines.flow.first

class ExerciseRepository(private val exerciseDao: ExerciseDtoDao) {

    // Get all exercises
    suspend fun allExercises(): List<Exercise> {
        return exerciseDao.getAllExercises()
            .first() // Collect the first emission of the Flow
            .map { Exercise.fromDto(it) } // Convert every DTO in Exercise
    }

    /**
     * Adds a new exercise to the data source.
     *
     * @param exercise The Exercise object to add.
     * @throws IllegalArgumentException if the exercise conversion to DTO fails.
     */
    suspend fun addExercise(exercise: Exercise) {
        exerciseDao.insertExercise(exercise.toDto())
    }


    // Delete an exercise
    suspend fun deleteExercise(exercise: Exercise) {
        // If there is no id, you can raise an exception and catch it in the use case and viewmodel
        exercise.id?.let {
            exerciseDao.deleteExerciseById(
                id = exercise.id,
            )
        }
    }
}