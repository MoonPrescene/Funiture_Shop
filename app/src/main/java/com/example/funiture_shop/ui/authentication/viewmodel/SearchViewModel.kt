package com.example.funiture_shop.ui.authentication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.funiture_shop.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    fun getListProduct() = productRepository.listProduct()
}