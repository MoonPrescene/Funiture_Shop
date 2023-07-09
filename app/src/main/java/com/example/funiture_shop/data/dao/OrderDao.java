package com.example.funiture_shop.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.funiture_shop.data.model.entity.Order;

@Dao
public interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntities(Order order);

    @Query("SELECT * FROM orders")
    LiveData<Order> getOrder();

    @Query("DELETE FROM orders")
    void deleteCart();
}
