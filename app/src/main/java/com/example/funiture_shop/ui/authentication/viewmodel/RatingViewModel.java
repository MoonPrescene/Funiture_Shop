package com.example.funiture_shop.ui.authentication.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.funiture_shop.data.model.entity.Review;
import com.example.funiture_shop.data.model.res.Res;
import com.example.funiture_shop.repository.ProductRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RatingViewModel extends ViewModel {
    private final ProductRepository productRepository;
    private MutableLiveData<Res> createReviewInfo = new MutableLiveData<>();

    @Inject
    public RatingViewModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public MutableLiveData<Res> getCreateReviewInfo() {
        return createReviewInfo;
    }

    public void createReview(Review review) {
        this.createReviewInfo = productRepository.createReview(review);
    }
}