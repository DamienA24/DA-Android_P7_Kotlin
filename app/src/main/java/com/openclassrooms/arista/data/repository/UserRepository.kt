package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.UserDtoDao
import com.openclassrooms.arista.data.entity.UserDto
import com.openclassrooms.arista.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(private val userDao: UserDtoDao) {

    suspend fun user(id: Long): Flow<User?> {
        return userDao.getUserById(id)
            .map { userDto ->
                userDto?.let { User.fromDto(it) }
            }
    }

    suspend fun insertUser(userDomain: User, passwordForDb: String): Long {
        val userDtoToInsert = UserDto(
            name = userDomain.name,
            email = userDomain.email,
            password = passwordForDb
        )
        return userDao.insertUser(userDtoToInsert)
    }
}