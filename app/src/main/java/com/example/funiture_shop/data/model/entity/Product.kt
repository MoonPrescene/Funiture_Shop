package com.example.funiture_shop.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.funiture_shop.common.formatPrice

@Entity(tableName = "products")
class Product(
    @PrimaryKey val productId: String = "",
    val name: String = "",
    val yearProduct: String = "",
    val type: String = "",
    var numberSold: Int = 0,
    var numberInventory: Int = 0,
    val price: Double = 0.0,
    val importPrice: Double = 0.0,
    var descriptions: String = "",
    var imgUrl: String = ""
){
    fun toPrice(): String{
        return price.formatPrice()
    }
}