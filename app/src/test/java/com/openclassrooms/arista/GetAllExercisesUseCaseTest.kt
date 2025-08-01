package com.openclassrooms.arista.domain.usecase

import com.openclassrooms.arista.data.repository.DataResult
import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.domain.model.Exercise
import com.openclassrooms.arista.domain.model.ExerciseCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.Instant.now
import java.time.LocalDateTime
import java.time.ZoneOffset

@ExperimentalCoroutinesApi
class GetAllExercisesUseCaseTest {

    private lateinit var exerciseRepository: ExerciseRepository
    private lateinit var getAllExercisesUseCase: GetAllExercisesUseCase

    @Before
    fun setUp() {
        exerciseRepository = mock()
        getAllExercisesUseCase = GetAllExercisesUseCase(exerciseRepository)
    }

    @Test
    fun `execute should return Success with list of exercises when repository returns success`() = runTest {
        // Arrange
        val fakeExercises = listOf(
            Exercise(id = 1, category = ExerciseCategory.Swimming, duration = 30, startTime = LocalDateTime.ofInstant(
                now().minusSeconds(100000), ZoneOffset.UTC), intensity = 3),
            Exercise(id = 2, category = ExerciseCategory.Running, duration = 60, startTime = LocalDateTime.ofInstant(
                now().minusSeconds(100000), ZoneOffset.UTC), intensity = 4)
        )
        val successResult = DataResult.Success(fakeExercises)
        val flowResult = flowOf(successResult)

        whenever(exerciseRepository.allExercises()).thenReturn(flowResult)

        // Act
        val resultFlow = getAllExercisesUseCase.execute()
        val actualDataResult = resultFlow.first()

        // Assert
        assertTrue("Result should be Success", actualDataResult is DataResult.Success)
        assertEquals(fakeExercises, (actualDataResult as DataResult.Success).data)
    }

    @Test
    fun `execute should return Success with empty list when repository returns success with empty list`() = runTest {
        // Arrange
        val emptyExercisesList = emptyList<Exercise>()
        val successResult = DataResult.Success(emptyExercisesList)
        val flowResult = flowOf(successResult)

        whenever(exerciseRepository.allExercises()).thenReturn(flowResult)

        // Act
        val resultFlow = getAllExercisesUseCase.execute()
        val actualDataResult = resultFlow.first()

        // Assert
        assertTrue("Result should be Success", actualDataResult is DataResult.Success)
        assertTrue("Exercise list should be empty", (actualDataResult as DataResult.Success).data.isEmpty())
    }

    @Test
    fun `execute should return Error when repository returns error`() = runTest {
        // Arrange
        val expectedException = Exception("Database connection failed")
        val errorResult: DataResult<List<Exercise>> = DataResult.Error(expectedException)
        val flowResult = flowOf(errorResult)

        whenever(exerciseRepository.allExercises()).thenReturn(flowResult)

        // Act
        val resultFlow = getAllExercisesUseCase.execute()
        val actualDataResult = resultFlow.first()

        // Assert
        assertTrue("Result should be Error", actualDataResult is DataResult.Error)
        assertEquals(expectedException, (actualDataResult as DataResult.Error).exception)
    }
}
