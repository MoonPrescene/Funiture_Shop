package com.example.funiture_shop.repository

import androidx.lifecycle.MutableLiveData
import com.example.funiture_shop.data.dao.InvoiceLineDao
import com.example.funiture_shop.data.dao.OrderDao
import com.example.funiture_shop.data.dao.ProductDao
import com.example.funiture_shop.data.dao.ReviewDao
import com.example.funiture_shop.data.dao.SearchQueryDao
import com.example.funiture_shop.data.model.entity.InvoiceLine
import com.example.funiture_shop.data.model.entity.Order
import com.example.funiture_shop.data.model.entity.Product
import com.example.funiture_shop.data.model.entity.Review
import com.example.funiture_shop.data.model.entity.SearchQuery
import com.example.funiture_shop.data.model.res.Res
import com.example.funiture_shop.di.runOnIoThread
import com.example.funiture_shop.helper.SharedPreferencesHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val storage: FirebaseStorage,
    private val db: FirebaseFirestore,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val productDao: ProductDao,
    private val orderDao: OrderDao,
    private val reviewDao: ReviewDao,
    private val invoiceLineDao: InvoiceLineDao,
    private val searchQueryDao: SearchQueryDao
) {
    private val listProductLiveData = MutableLiveData<Res>()
    private val createOrderLiveData = MutableLiveData<Res>()
    private val reviewsLiveData = MutableLiveData<Res>()
    private val listOrderLiveData = MutableLiveData<Res>()
    private val createReview = MutableLiveData<Res>()

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

    fun getListOrder(): MutableLiveData<Res> {
        db.collection("orders")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val list = arrayListOf<Order>()
                for (document in querySnapshot) {
                    list.add(convertToOrder(document))
                }
                insertOrders(list)
                listOrderLiveData.postValue(Res.Success(data = null))
            }
            .addOnFailureListener { exception ->
                listOrderLiveData.postValue(Res.Error(exception.message.toString()))
            }
        return listOrderLiveData
    }


    fun getListReview(): MutableLiveData<Res> {
        db.collection("reviews, comments")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val list = arrayListOf<Review>()
                for (document in querySnapshot) {
                    list.add(convertToReview(document))
                }
                insertListReview(list)
                reviewsLiveData.postValue(Res.Success(data = null))
            }
            .addOnFailureListener { exception ->
                reviewsLiveData.postValue(Res.Error(exception.message.toString()))
            }
        return reviewsLiveData
    }



    fun createReview(review: Review): MutableLiveData<Res> {

        review.creater = sharedPreferencesHelper.getUserName()
        review.reviewID = UUID.randomUUID().toString()

        val ordersCollection = db.collection("reviews, comments")
        val orderDocument = ordersCollection.document(review.reviewID)

        orderDocument.set(review)
            .addOnSuccessListener {
                createReview.postValue(Res.Success(data = null))
                deleteAllInvoiceLine()
            }
            .addOnFailureListener { e ->
                createReview.postValue(Res.Error(e.message.toString()))
            }
        return createReview
    }

    fun createOrder(order: Order): MutableLiveData<Res> {

        order.userID = sharedPreferencesHelper.getUserName()
        order.orderID = UUID.randomUUID().toString()

        val ordersCollection = db.collection("orders")
        val orderDocument = ordersCollection.document(order.orderID)

        orderDocument.set(order)
            .addOnSuccessListener {
                createOrderLiveData.postValue(Res.Success(data = null))
                deleteAllInvoiceLine()
            }
            .addOnFailureListener { e ->
                createOrderLiveData.postValue(Res.Error(e.message.toString()))
            }
        return createOrderLiveData
    }


    private fun insertListProduct(list: ArrayList<Product>) {
        runOnIoThread {
            productDao.insertEntities(list)
        }
    }

    private fun insertListReview(list: ArrayList<Review>) {
        runOnIoThread {
            reviewDao.insertEntities(list)
        }
    }

    private fun insertOrders(orders: List<Order>) {
        runOnIoThread {
            orderDao.deleteCart()
            orderDao.insertEntities(orders)
        }
    }

    fun insertSearchQuery(searchQuery: List<SearchQuery>) {
        runOnIoThread {
            searchQueryDao.insertEntities(searchQuery)
        }
    }

    fun getListSearchQuery() = searchQueryDao.getAllEntities()

    fun insertInvoiceLine(invoiceLine: ArrayList<InvoiceLine>) {
        runOnIoThread {
            invoiceLineDao.deleteAll()
            invoiceLineDao.insertEntities(invoiceLine)
        }
    }

    fun deleteAllInvoiceLine() {
        runOnIoThread {
            invoiceLineDao.deleteAll()
        }
    }

    fun reviewList() = reviewDao.reviews

    fun getInvoiceLines() = invoiceLineDao.invoiceLines

    fun listProduct() = productDao.getAllEntities()

    fun getOrders() = orderDao.order

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

    private fun convertToReview(document: QueryDocumentSnapshot): Review {
        return Review(
            document.id,
            document.getString("creater").toString(),
            document.getDouble("rating")!!.toFloat(),
            document.getString("textreview").toString(),
            document.getString("title").toString(),
            document.getString("timecreate").toString()
        )
    }

    private fun convertToOrder(document: QueryDocumentSnapshot): Order {
        return Order(
            document.id,
            document.getString("userID").toString(),
            document.getDouble("quantity")!!.toInt(),
            document.getString("timeCreate").toString(),
            document.getString("address").toString(),
            document.getDouble("status")!!.toInt(),
            document.getDouble("total")!!,
        )
    }
}