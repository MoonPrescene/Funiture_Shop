package com.example.funiture_shop.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    var imageUrl: String = "",
    val permission: Int = 0 ,
    val address: String = ""
)