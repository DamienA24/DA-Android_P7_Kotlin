package com.openclassrooms.arista

import com.openclassrooms.arista.data.repository.DataResult
import com.openclassrooms.arista.domain.usecase.GetUserUseCase
import com.openclassrooms.arista.data.repository.UserRepository
import com.openclassrooms.arista.domain.model.User
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

@ExperimentalCoroutinesApi
class GetUserUseCaseTest {

    // Déclarer le mock du repository
    private lateinit var userRepository: UserRepository

    // Déclarer l'instance du UseCase à tester
    private lateinit var getUserUseCase: GetUserUseCase

    @Before
    fun setUp() {
        // Initialiser le mock avant chaque test
        userRepository = mock()

        // Initialiser le UseCase avec le repository mocké
        getUserUseCase = GetUserUseCase(userRepository)
    }

    @Test
    fun `execute should return Success with User when repository returns User`() = runTest {
        // Arrange
        val expectedUser = User(id = 1L, name = "Test User", email = "test@example.com")
        val successResult = DataResult.Success(expectedUser)
        val flowResult = flowOf(successResult)

        // Mock avec l'ID spécifique utilisé par le UseCase (1L)
        whenever(userRepository.user()).thenReturn(flowResult)

        // Act
        val resultFlow = getUserUseCase.execute()
        val actualDataResult = resultFlow.first()

        // Assert
        assertTrue("Result should be Success", actualDataResult is DataResult.Success)
        assertEquals(expectedUser, (actualDataResult as DataResult.Success).data)
    }

    @Test
    fun `execute should return Success with null User when repository returns null User`() = runTest {
        // Arrange
        val successResultWithNullUser: DataResult<User?> = DataResult.Success(null)
        val flowResult = flowOf(successResultWithNullUser)

        whenever(userRepository.user()).thenReturn(flowResult)

        // Act
        val resultFlow = getUserUseCase.execute()
        val actualDataResult = resultFlow.first()

        // Assert
        assertTrue("Result should be Success", actualDataResult is DataResult.Success)
        assertEquals(null, (actualDataResult as DataResult.Success).data)
    }

    @Test
    fun `execute should return Error when repository returns Error`() = runTest {
        // Arrange
        val expectedException = Exception("Database error")
        val errorResult: DataResult<User?> = DataResult.Error(expectedException)
        val flowResult = flowOf(errorResult)

        whenever(userRepository.user()).thenReturn(flowResult)

        // Act
        val resultFlow = getUserUseCase.execute()
        val actualDataResult = resultFlow.first()

        // Assert
        assertTrue("Result should be Error", actualDataResult is DataResult.Error)
        assertEquals(expectedException, (actualDataResult as DataResult.Error).exception)
    }

    @Test
    fun `execute should return Error when repository throws exception`() = runTest {
        // Arrange - Simuler une exception lancée par le repository
        val expectedException = RuntimeException("Network error")
        whenever(userRepository.user()).thenThrow(expectedException)

        // Act & Assert
        try {
            val resultFlow = getUserUseCase.execute()
            resultFlow.first() // Ceci devrait lancer l'exception
            assertTrue("Should have thrown an exception", false)
        } catch (e: Exception) {
            assertEquals(expectedException, e)
        }
    }
}