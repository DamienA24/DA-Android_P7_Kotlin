package com.openclassrooms.arista.domain.model

import com.openclassrooms.arista.data.entity.UserDto

data class User(var id: Long = 0, var name: String, var email: String, var password: String? = null){
    companion object {
        fun fromDto(dto: UserDto): User {
            return User(
                id = dto.id,
                name = dto.name,
                email = dto.email,
            )
        }
    }
}