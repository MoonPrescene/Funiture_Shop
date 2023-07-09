package com.example.funiture_shop.repository

import androidx.lifecycle.MutableLiveData
import com.example.funiture_shop.data.dao.InvoiceLineDao
import com.example.funiture_shop.data.dao.OrderDao
import com.example.funiture_shop.data.dao.ProductDao
import com.example.funiture_shop.data.model.entity.InvoiceLine
import com.example.funiture_shop.data.model.entity.Order
import com.example.funiture_shop.data.model.entity.Product
import com.example.funiture_shop.data.model.res.Res
import com.example.funiture_shop.di.runOnIoThread
import com.example.funiture_shop.helper.SharedPreferencesHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val storage: FirebaseStorage,
    private val db: FirebaseFirestore,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val productDao: ProductDao,
    private val orderDao: OrderDao,
    private val invoiceLineDao: InvoiceLineDao
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

    private fun insertListProduct(list: ArrayList<Product>) {
        runOnIoThread {
            productDao.insertEntities(list)
        }
    }

    private fun insertCart(order: Order) {
        runOnIoThread {
            orderDao.deleteCart()
            orderDao.insertEntities(order)
        }
    }

    fun insertInvoiceLine(invoiceLine: ArrayList<InvoiceLine>) {
        runOnIoThread {
            invoiceLineDao.deleteAll()
            invoiceLineDao.insertEntities(invoiceLine)
        }
    }

    fun getInvoiceLines() = invoiceLineDao.invoiceLines

    fun listProduct() = productDao.getAllEntities()

    private fun covertToProduct(document: QueryDocumentSnapshot): Product {
        return Product(
            productId = document.id,
            name = document.getString("name").toString(),
            yearProduct = document.getString("year_of_product").toString(),
            type = document.getString("type").toString(),
            numberSold = document.getDouble("number_sold")!!.toInt(),
            numberInventory = document.getDouble("number_inventory")!!.toInt(),
            price = document.getDouble("price")!!,
            importPrice = document.getDouble("importPrice")!!,
            descriptions = document.getString("descriptions").toString(),
            imgUrl = document.getString("imgUrl").toString()
        )
    }
}