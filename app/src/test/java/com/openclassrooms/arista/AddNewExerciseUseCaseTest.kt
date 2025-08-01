package com.openclassrooms.arista

import com.openclassrooms.arista.data.repository.DataResult
import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.domain.model.Exercise
import com.openclassrooms.arista.domain.model.ExerciseCategory
import com.openclassrooms.arista.domain.usecase.AddNewExerciseUseCase
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
class AddNewExerciseUseCaseTest {

    private lateinit var exerciseRepository: ExerciseRepository
    private lateinit var addNewExerciseUseCase: AddNewExerciseUseCase

    private val fakeExerciseToAdd = Exercise(
        startTime = LocalDateTime.now(),
        duration = 60,
        category = ExerciseCategory.Running,
        intensity = 3
    )

    @Before
    fun setUp() {
        exerciseRepository = mock()
        addNewExerciseUseCase = AddNewExerciseUseCase(exerciseRepository)
    }

    @Test
    fun `execute should return Success with new exercise ID when repository adds exercise successfully`() = runTest {
        // Arrange
        val expectedNewId = 123L
        val successResult = DataResult.Success(expectedNewId)

        whenever(exerciseRepository.addExercise(fakeExerciseToAdd)).thenReturn(successResult)

        // Act
        val actualResult = addNewExerciseUseCase.execute(fakeExerciseToAdd)

        // Assert
        assertTrue("Result should be Success", actualResult is DataResult.Success)
        assertEquals(expectedNewId, (actualResult as DataResult.Success).data)

        verify(exerciseRepository).addExercise(fakeExerciseToAdd)
    }

    @Test
    fun `execute should return Error when repository fails to add exercise`() = runTest {
        // Arrange
        val expectedException = Exception("Database insertion failed")
        val errorResult: DataResult<Long> = DataResult.Error(expectedException)

        whenever(exerciseRepository.addExercise(fakeExerciseToAdd)).thenReturn(errorResult)

        // Act
        val actualResult = addNewExerciseUseCase.execute(fakeExerciseToAdd)

        // Assert
        assertTrue("Result should be Error", actualResult is DataResult.Error)
        assertEquals(expectedException, (actualResult as DataResult.Error).exception)
        // Vérifier que la méthode du repository a bien été appelée
        verify(exerciseRepository).addExercise(fakeExerciseToAdd)
    }

    @Test
    fun `execute should return Error when repository throws specific exception like IllegalArgumentException`() = runTest {
        // Arrange
        val specificException = IllegalArgumentException("Invalid exercise data for DTO conversion")
        val errorResult: DataResult<Long> = DataResult.Error(specificException)

        whenever(exerciseRepository.addExercise(fakeExerciseToAdd)).thenReturn(errorResult)

        // Act
        val actualResult = addNewExerciseUseCase.execute(fakeExerciseToAdd)

        // Assert
        assertTrue("Result should be Error", actualResult is DataResult.Error)
        assertEquals(specificException, (actualResult as DataResult.Error).exception)
        verify(exerciseRepository).addExercise(fakeExerciseToAdd)
    }
}

