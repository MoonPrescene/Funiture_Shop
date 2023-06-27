package com.example.funiture_shop.ui.authentication.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.funiture_shop.R;
import com.example.funiture_shop.data.model.entity.Product;
import com.example.funiture_shop.databinding.FragmentProductDetailBinding;
import com.example.funiture_shop.ui.authentication.viewmodel.ProductDetailViewModel;
import com.squareup.picasso.Picasso;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductDetailFragment extends Fragment {

    private ProductDetailViewModel mViewModel;
    private FragmentProductDetailBinding binding;
    private Product product;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail, container, false);
        mViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        product = ProductDetailFragmentArgs.fromBundle(requireArguments()).getProduct();
        binding.setProduct(product);
        binding.add.setOnClickListener(view -> {
            //Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
        });
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String message = "Selected Option: " + checkedId;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
        Picasso.get()
                .load(product.getImgUrl())
                .into(binding.imageView);
        return binding.getRoot();
    }

    private void addProductToCart(Product product) {

    }
}