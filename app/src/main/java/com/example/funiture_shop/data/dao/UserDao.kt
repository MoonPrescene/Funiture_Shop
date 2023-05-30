package com.example.funiture_shop.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.example.funiture_shop.data.model.entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}