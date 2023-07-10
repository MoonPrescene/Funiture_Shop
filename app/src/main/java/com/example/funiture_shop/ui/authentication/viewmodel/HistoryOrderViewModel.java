package com.example.funiture_shop.ui.authentication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.funiture_shop.data.model.entity.InvoiceLine;
import com.example.funiture_shop.data.model.entity.Order;
import com.example.funiture_shop.data.model.res.Res;
import com.example.funiture_shop.repository.ProductRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HistoryOrderViewModel extends ViewModel {
    private final ProductRepository productRepository;
    private MutableLiveData<Res> getOrdersInfo = new MutableLiveData<>();

    @Inject
    HistoryOrderViewModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public MutableLiveData<Res> getListOrderInfo() {
        return this.getOrdersInfo;
    }

    public void getOrderInfo() {
        this.getOrdersInfo = productRepository.getListOrder();
    }

    public LiveData<List<Order>> listOrder() {
        return productRepository.getOrders();
    }
}