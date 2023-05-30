package com.example.funiture_shop.repository

import androidx.lifecycle.MutableLiveData
import com.example.funiture_shop.data.dao.ProductDao
import com.example.funiture_shop.data.model.entity.Product
import com.example.funiture_shop.data.model.res.Res
import com.example.funiture_shop.di.runOnIoThread
import com.example.funiture_shop.helper.SharedPreferencesHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val storage: FirebaseStorage,
    private val db: FirebaseFirestore,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val productDao: ProductDao
) {
    private val listProductLiveData = MutableLiveData<Res>()

    fun getListProduct(): MutableLiveData<Res> {
        db.collection("products")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val list = arrayListOf<Product>()
                for (document in querySnapshot) {
                    list.add(covertToProduct(document))
                }
                insertListProduct(list)
                listProductLiveData.postValue(Res.Success(data = null))
            }
            .addOnFailureListener { exception ->
                listProductLiveData.postValue(Res.Error(exception.message.toString()))
            }
        return listProductLiveData
    }

    private fun insertListProduct(list: ArrayList<Product>){
        runOnIoThread {
            productDao.insertEntities(list)
        }
    }

    fun listProduct() = productDao.getAllEntities()

    private fun covertToProduct(document: QueryDocumentSnapshot): Product {
        return Product(
            productId = document.getString("").toString(),
            name = document.getString("").toString(),
            yearProduct = document.getString("").toString(),
            type = document.getString("").toString(),
            numberSold = document.getDouble("")!!.toInt(),
            numberInventory = document.getDouble("")!!.toInt(),
            price = document.getDouble("")!!,
            importPrice = document.getDouble("")!!,
            descriptions = document.getString("").toString(),
            imgUrl = document.getString("").toString()
        )
    }
}