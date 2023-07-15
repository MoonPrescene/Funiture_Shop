package com.example.funiture_shop.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.funiture_shop.data.model.entity.Product
import com.example.funiture_shop.data.model.entity.SearchQuery

@Dao
interface SearchQueryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntities(entities: List<SearchQuery>)

    @Query("SELECT * FROM searchQuery")
    fun getAllEntities(): LiveData<List<SearchQuery>>
}