package com.openclassrooms.arista.domain.usecase

import com.openclassrooms.arista.data.repository.DataResult
import com.openclassrooms.arista.data.repository.UserRepository
import com.openclassrooms.arista.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun execute(): Flow<DataResult<User?>> {
        return userRepository.user()
    }
}