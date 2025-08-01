package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.UserDtoDao
import com.openclassrooms.arista.data.entity.UserDto
import com.openclassrooms.arista.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserRepository(private val userDao: UserDtoDao) {

    /**
     * Retrieves a user by their ID from the database.
     * Returns a Flow of DataResult<User?>.
     * If the operation is successful, it emits DataResult.Success with the user.
     * If an error occurs, it emits DataResult.Error with the exception.
     */
    suspend fun user(id: Long): Flow<DataResult<User?>> {
        return userDao.getUserById(id)
            .map<UserDto?, DataResult<User?>> { userDto -> // Explicit type for map
                DataResult.Success(userDto?.let { User.fromDto(it) })
            }.catch { e ->
                emit(DataResult.Error(Exception("User not found", e)))
            }
    }

}