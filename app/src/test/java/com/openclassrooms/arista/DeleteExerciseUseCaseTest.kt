package com.openclassrooms.arista

import com.openclassrooms.arista.data.repository.DataResult
import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.domain.model.Exercise
import com.openclassrooms.arista.domain.model.ExerciseCategory
import com.openclassrooms.arista.domain.usecase.DeleteExerciseUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class DeleteExerciseUseCaseTest {

    private lateinit var exerciseRepository: ExerciseRepository
    private lateinit var deleteExerciseUseCase: DeleteExerciseUseCase

    private val fakeExerciseToDelete = Exercise(
        id = 1L,
        startTime = LocalDateTime.now(),
        duration = 60,
        category = ExerciseCategory.Swimming,
        intensity = 4
    )

    private val fakeExerciseWithNullId = Exercise(
        id = null,
        startTime = LocalDateTime.now(),
        duration = 30,
        category = ExerciseCategory.Walking,
        intensity = 2
    )

    @Before
    fun setUp() {
        exerciseRepository = mock()
        deleteExerciseUseCase = DeleteExerciseUseCase(exerciseRepository)
    }

    @Test
    fun `execute should return Success when repository deletes exercise successfully`() = runTest {
        // Arrange
        val successResult = DataResult.Success(Unit) // La suppression réussie retourne DataResult<Unit>

        whenever(exerciseRepository.deleteExercise(fakeExerciseToDelete)).thenReturn(successResult)

        // Act
        val actualResult = deleteExerciseUseCase.execute(fakeExerciseToDelete)

        // Assert
        assertTrue("Result should be Success", actualResult is DataResult.Success)
        assertEquals(Unit, (actualResult as DataResult.Success).data)

        verify(exerciseRepository).deleteExercise(fakeExerciseToDelete)
    }

    @Test
    fun `execute should return Error when repository fails to delete exercise`() = runTest {
        // Arrange
        val expectedException = Exception("Database deletion failed")
        val errorResult: DataResult<Unit> = DataResult.Error(expectedException)

        whenever(exerciseRepository.deleteExercise(fakeExerciseToDelete)).thenReturn(errorResult)

        // Act
        val actualResult = deleteExerciseUseCase.execute(fakeExerciseToDelete)

        // Assert
        assertTrue("Result should be Error", actualResult is DataResult.Error)
        assertEquals(expectedException, (actualResult as DataResult.Error).exception)
        verify(exerciseRepository).deleteExercise(fakeExerciseToDelete)
    }

    @Test
    fun `execute should return Error when exercise ID is null and repository handles it`() = runTest {
        // Arrange
        val idNullException = IllegalArgumentException("Exercise ID cannot be null for deletion.")
        val errorResultWithNullId: DataResult<Unit> = DataResult.Error(idNullException)

        whenever(exerciseRepository.deleteExercise(fakeExerciseWithNullId)).thenReturn(errorResultWithNullId)

        // Act
        val actualResult = deleteExerciseUseCase.execute(fakeExerciseWithNullId)

        // Assert
        assertTrue("Result should be Error for null ID", actualResult is DataResult.Error)
        assertEquals(idNullException.message, (actualResult as DataResult.Error).exception.message)

        verify(exerciseRepository).deleteExercise(fakeExerciseWithNullId)
    }
}
