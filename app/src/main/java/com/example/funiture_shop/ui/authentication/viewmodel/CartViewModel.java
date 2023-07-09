package com.example.funiture_shop.ui.authentication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.funiture_shop.data.model.entity.InvoiceLine;
import com.example.funiture_shop.data.model.entity.Order;
import com.example.funiture_shop.data.model.res.Res;
import com.example.funiture_shop.repository.ProductRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CartViewModel extends ViewModel {

    private final ProductRepository productRepository;
    private MutableLiveData<Res> createOrderInfo = new MutableLiveData<>();

    @Inject
    CartViewModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public MutableLiveData<Res> getCreateOrderInfo() {
        return this.createOrderInfo;
    }

    public LiveData<List<InvoiceLine>> listInvoiceLineInCart() {
        return productRepository.getInvoiceLines();
    }

    public void createOrder(Order order) {
        createOrderInfo.postValue(productRepository.createOrder(order).getValue());
    }

}