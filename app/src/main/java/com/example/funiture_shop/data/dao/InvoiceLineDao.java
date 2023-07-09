package com.example.funiture_shop.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.funiture_shop.data.model.entity.InvoiceLine;


import java.util.List;

@Dao
public interface InvoiceLineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntities(List<InvoiceLine> invoiceLines);

    @Query("SELECT * FROM invoiceLines")
    LiveData<List<InvoiceLine>> getInvoiceLines();

    @Query("DELETE FROM invoiceLines")
    void deleteAll();
}
