package com.example.funiture_shop.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searchQuery")
class SearchQuery(
    @PrimaryKey
    val content: String = ""
)