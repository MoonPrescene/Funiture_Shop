package com.example.funiture_shop.ui.authentication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.funiture_shop.data.model.entity.InvoiceLine;
import com.example.funiture_shop.data.model.entity.Review;
import com.example.funiture_shop.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProductDetailViewModel extends ViewModel {
    private final ProductRepository productRepository;

    @Inject
    ProductDetailViewModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public LiveData<List<InvoiceLine>> listInvoiceLineInCart() {
        return productRepository.getInvoiceLines();
    }

    public void insertInvoiceLines(ArrayList<InvoiceLine> invoiceLines) {
        productRepository.insertInvoiceLine(invoiceLines);
    }

   /* public ArrayList<Review> getListReview() {
        return productRepository.getListReview();
    }*/

}