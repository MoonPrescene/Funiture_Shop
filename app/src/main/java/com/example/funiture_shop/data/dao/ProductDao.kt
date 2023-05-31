package com.example.funiture_shop.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.funiture_shop.data.model.entity.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntities(entities: List<Product>)

    @Query("SELECT * FROM products")
    fun getAllEntities(): LiveData<List<Product>>
}