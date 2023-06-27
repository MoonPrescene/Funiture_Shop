package com.example.funiture_shop.ui.authentication.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.funiture_shop.repository.ProductRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProductDetailViewModel extends ViewModel {
    private final ProductRepository productRepository;
    @Inject
    ProductDetailViewModel(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
}