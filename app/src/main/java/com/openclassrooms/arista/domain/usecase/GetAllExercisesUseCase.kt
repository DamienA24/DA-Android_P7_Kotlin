package com.openclassrooms.arista.domain.usecase

import com.openclassrooms.arista.data.repository.DataResult
import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.domain.model.Exercise
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllExercisesUseCase @Inject constructor(private val exerciseRepository: ExerciseRepository) {
    suspend fun execute(): Flow<DataResult<List<Exercise>>> {
        return exerciseRepository.allExercises()
    }
}