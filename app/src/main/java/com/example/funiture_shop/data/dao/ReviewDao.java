package com.example.funiture_shop.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.funiture_shop.data.model.entity.InvoiceLine;
import com.example.funiture_shop.data.model.entity.Review;

import java.util.List;

@Dao
public interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntities(List<Review> reviews);

    @Query("SELECT * FROM reviews")
    LiveData<List<Review>> getReviews();

    @Query("DELETE FROM reviews")
    void deleteAll();
}
