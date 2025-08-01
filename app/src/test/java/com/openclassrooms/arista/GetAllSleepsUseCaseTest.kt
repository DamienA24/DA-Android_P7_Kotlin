package com.openclassrooms.arista

import com.openclassrooms.arista.data.repository.DataResult
import com.openclassrooms.arista.domain.usecase.GetAllSleepsUseCase
import com.openclassrooms.arista.data.repository.SleepRepository
import com.openclassrooms.arista.domain.model.Sleep
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
import java.time.LocalDateTime


@ExperimentalCoroutinesApi
class GetAllSleepsUseCaseTest {

    private lateinit var sleepRepository: SleepRepository

    private lateinit var getAllSleepsUseCase: GetAllSleepsUseCase

    @Before
    fun setUp() {
        sleepRepository = mock()
        getAllSleepsUseCase = GetAllSleepsUseCase(sleepRepository)
    }

    @Test
    fun `execute should return Success with list of Sleeps when repository returns sleeps`() = runTest {
        // Arrange
        val sleep1 = Sleep(
            startTime = LocalDateTime.of(2024, 1, 1, 22, 0),
            duration = 540,
            quality = 4
        )
        val sleep2 = Sleep(
            startTime = LocalDateTime.of(2024, 1, 2, 23, 30),
            duration = 510,
            quality = 5
        )
        val expectedSleeps = listOf(sleep1, sleep2)
        val successResult = DataResult.Success(expectedSleeps)
        val flowResult = flowOf(successResult)

        whenever(sleepRepository.allSleeps()).thenReturn(flowResult)

        // Act
        val resultFlow = getAllSleepsUseCase.execute()
        val actualDataResult = resultFlow.first()

        // Assert
        assertTrue("Result should be Success", actualDataResult is DataResult.Success)
        assertEquals(expectedSleeps, (actualDataResult as DataResult.Success).data)
        assertEquals(2, actualDataResult.data.size)
        assertEquals(sleep1, actualDataResult.data[0])
        assertEquals(sleep2, actualDataResult.data[1])
    }

    @Test
    fun `execute should return Success with empty list when repository returns empty list`() = runTest {
        // Arrange
        val emptyList = emptyList<Sleep>()
        val successResult = DataResult.Success(emptyList)
        val flowResult = flowOf(successResult)

        whenever(sleepRepository.allSleeps()).thenReturn(flowResult)

        // Act
        val resultFlow = getAllSleepsUseCase.execute()
        val actualDataResult = resultFlow.first()

        // Assert
        assertTrue("Result should be Success", actualDataResult is DataResult.Success)
        assertEquals(emptyList, (actualDataResult as DataResult.Success).data)
        assertTrue("List should be empty", actualDataResult.data.isEmpty())
    }

    @Test
    fun `execute should return Success with single Sleep when repository returns one sleep`() = runTest {
        // Arrange
        val singleSleep = Sleep(
            startTime = LocalDateTime.of(2024, 1, 1, 22, 0),
            duration = 480,
            quality = 3
        )
        val sleepList = listOf(singleSleep)
        val successResult = DataResult.Success(sleepList)
        val flowResult = flowOf(successResult)

        whenever(sleepRepository.allSleeps()).thenReturn(flowResult)

        // Act
        val resultFlow = getAllSleepsUseCase.execute()
        val actualDataResult = resultFlow.first()

        // Assert
        assertTrue("Result should be Success", actualDataResult is DataResult.Success)
        assertEquals(sleepList, (actualDataResult as DataResult.Success).data)
        assertEquals(1, actualDataResult.data.size)
        assertEquals(singleSleep, actualDataResult.data[0])
    }

    @Test
    fun `execute should return Error when repository returns Error`() = runTest {
        // Arrange
        val expectedException = Exception("Failed to fetch sleeps from database")
        val errorResult: DataResult<List<Sleep>> = DataResult.Error(expectedException)
        val flowResult = flowOf(errorResult)

        whenever(sleepRepository.allSleeps()).thenReturn(flowResult)

        // Act
        val resultFlow = getAllSleepsUseCase.execute()
        val actualDataResult = resultFlow.first()

        // Assert
        assertTrue("Result should be Error", actualDataResult is DataResult.Error)
        assertEquals(expectedException, (actualDataResult as DataResult.Error).exception)
    }

    @Test
    fun `execute should return Error when repository throws exception`() = runTest {
        // Arrange
        val expectedException = RuntimeException("Database connection failed")
        whenever(sleepRepository.allSleeps()).thenThrow(expectedException)

        // Act & Assert
        try {
            val resultFlow = getAllSleepsUseCase.execute()
            resultFlow.first()
            assertTrue("Should have thrown an exception", false)
        } catch (e: Exception) {
            assertEquals(expectedException, e)
        }
    }

    @Test
    fun `execute should return Success with large list when repository returns many sleeps`() = runTest {
        // Arrange
        val largeSleepList = (1..10).map { index ->
            Sleep(
                startTime = LocalDateTime.of(2024, 1, index, 22, 0),
                duration = 420 + (index * 30),
                quality = (index % 5) + 1
            )
        }
        val successResult = DataResult.Success(largeSleepList)
        val flowResult = flowOf(successResult)

        whenever(sleepRepository.allSleeps()).thenReturn(flowResult)

        // Act
        val resultFlow = getAllSleepsUseCase.execute()
        val actualDataResult = resultFlow.first()

        // Assert
        assertTrue("Result should be Success", actualDataResult is DataResult.Success)
        assertEquals(largeSleepList, (actualDataResult as DataResult.Success).data)
        assertEquals(10, actualDataResult.data.size)

        assertEquals(LocalDateTime.of(2024, 1, 1, 22, 0), actualDataResult.data.first().startTime)
        assertEquals(LocalDateTime.of(2024, 1, 10, 22, 0), actualDataResult.data.last().startTime)

        assertEquals(450, actualDataResult.data[0].duration)
        assertEquals(720, actualDataResult.data[9].duration)
    }
}
