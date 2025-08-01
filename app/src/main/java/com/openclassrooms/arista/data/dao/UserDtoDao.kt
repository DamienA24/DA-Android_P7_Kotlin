package com.openclassrooms.arista.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.openclassrooms.arista.data.entity.UserDto
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDtoDao {
    @Upsert
    suspend fun insertUser(user: UserDto): Long


    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: Long): Flow<UserDto?>


    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteUserById(id: Long)
}