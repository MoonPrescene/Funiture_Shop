package com.example.funiture_shop.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.funiture_shop.data.model.entity.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntities(List<Order> order);

    @Query("SELECT * FROM orders")
    LiveData<List<Order>> getOrder();

    @Query("DELETE FROM orders")
    void deleteCart();
}
