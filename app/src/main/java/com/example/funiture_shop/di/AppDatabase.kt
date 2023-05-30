package com.example.funiture_shop.di

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.funiture_shop.common.Const
import com.example.funiture_shop.data.dao.ProductDao
import com.example.funiture_shop.data.dao.UserDao
import com.example.funiture_shop.data.model.entity.Product
import com.example.funiture_shop.data.model.entity.User

@Database(entities = [User::class, Product::class], version = Const.DB_VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
}