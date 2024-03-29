package com.example.funiture_shop.ui.authentication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.funiture_shop.data.model.entity.InvoiceLine
import com.example.funiture_shop.data.model.entity.SearchQuery
import com.example.funiture_shop.data.model.entity.User
import com.example.funiture_shop.data.model.res.Res
import com.example.funiture_shop.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _getListProductInfo: MutableLiveData<String> = MutableLiveData()

    val info: LiveData<Res> = _getListProductInfo.switchMap {
        productRepository.getListProduct()
    }

    fun getList() {
        _getListProductInfo.value = ""
    }

    fun listProduct() = productRepository.listProduct()

    fun listInvoiceLineInCart() = productRepository.getInvoiceLines()

    fun insertInvoiceLines(invoiceLines: ArrayList<InvoiceLine>) {
        productRepository.insertInvoiceLine(invoiceLine = invoiceLines)
    }

    fun insertSearchQuery(searchQuery: List<SearchQuery>) {
        productRepository.insertSearchQuery(searchQuery)
    }

    fun getListSearchQuery() = productRepository.getListSearchQuery()
}