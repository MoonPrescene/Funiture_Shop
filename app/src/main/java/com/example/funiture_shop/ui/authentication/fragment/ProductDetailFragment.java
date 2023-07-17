package com.example.funiture_shop.ui.authentication.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.funiture_shop.AR_Activity;
import com.example.funiture_shop.R;
import com.example.funiture_shop.data.dao.InvoiceLineDao;
import com.example.funiture_shop.data.dao.ReviewDao;
import com.example.funiture_shop.data.model.adapters.ReviewAdapter;
import com.example.funiture_shop.data.model.entity.InvoiceLine;
import com.example.funiture_shop.data.model.entity.Product;
import com.example.funiture_shop.data.model.entity.Review;
import com.example.funiture_shop.databinding.FragmentProductDetailBinding;
import com.example.funiture_shop.ui.authentication.viewmodel.ProductDetailViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductDetailFragment extends Fragment {

    private ProductDetailViewModel mViewModel;
    private FragmentProductDetailBinding binding;
    private Product product;
    private ArrayList<InvoiceLine> listInvoiceLineInCart;
    private ArrayList<Review> listReview = new ArrayList<>();
    private ReviewAdapter reviewAdapter;

    @Inject
    FirebaseFirestore db;

    @Inject
    InvoiceLineDao invoiceLineDao;

    @Inject
    ReviewDao reviewDao;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail, container, false);
        mViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        product = ProductDetailFragmentArgs.fromBundle(requireArguments()).getProduct();
        getListReview();
        binding.setProduct(product);
        binding.add.setOnClickListener(view -> {
            addProductToCart(product);
            String message = "Đã thêm sản phẩm " + product.getName();
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        });
        binding.review.setOnClickListener(view -> {
            Bundle args = new Bundle();
            args.putString("ratingArguments", product.getProductId());
            RatingFragment destinationFragment = new RatingFragment();
            destinationFragment.setArguments(args);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_productDetailFragment_to_ratingFragment, args);
        });
        binding.review3D.setOnClickListener(view -> {
            startActivity(new Intent(requireActivity(), AR_Activity.class));
        });
        Picasso.get()
                .load(product.getImgUrl())
                .into(binding.imageView);
        setupRecyclerView();
        observeData();
        return binding.getRoot();
    }

    public void observeData() {
        reviewDao.getReviews().observe(getViewLifecycleOwner(), new Observer<List<Review>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Review> reviews) {
                if (reviews != null) {
                    listReview = new ArrayList<>();
                    for (Review item : reviews) {
                        if (Objects.equals(item.getTitle(), product.getProductId())) {
                            listReview.add(item);
                        }
                    }
                    reviewAdapter.setListReview(listReview);
                    reviewAdapter.notifyDataSetChanged();
                }
            }
        });
        invoiceLineDao.getInvoiceLines().observe(getViewLifecycleOwner(), new Observer<List<InvoiceLine>>() {

            @Override
            public void onChanged(List<InvoiceLine> invoiceLines) {
                listInvoiceLineInCart = new ArrayList<>(invoiceLines);
            }
        });
    }

    private void setupRecyclerView() {
        reviewAdapter = new ReviewAdapter(listReview);
        binding.recyclerview.setAdapter(reviewAdapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void addProductToCart(Product product) {
        InvoiceLine existingProduct = null;
        for (InvoiceLine invoiceLine : listInvoiceLineInCart) {
            if (invoiceLine.getProductId().equals(product.getProductId())) {
                existingProduct = invoiceLine;
                break;
            }
        }

        if (existingProduct != null) {
            existingProduct.setQuantity(existingProduct.getQuantity() + 1);
        } else {
            product.setQuantity(1);
            listInvoiceLineInCart.add(product.convertToInvoiceLine());
        }
        Toast.makeText(requireContext(), product.getProductId(), Toast.LENGTH_SHORT).show();
        insertAllInvoiceLine();
    }

    public void getListReview() {
        binding.loading.setVisibility(View.VISIBLE);
        db.collection("reviews, comments")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        binding.loading.setVisibility(View.GONE);
                        ArrayList<Review> list = new ArrayList<>();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            list.add(convertToReview(document));
                        }
                        insertReview(list);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        binding.loading.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Review convertToReview(QueryDocumentSnapshot document) {
        return new Review(
                document.getId(),
                document.getString("creater"),
                document.getDouble("rating").floatValue(),
                document.getString("textreview"),
                document.getString("title"),
                document.getString("timecreate")
        );
    }

    public void insertAllInvoiceLine() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                invoiceLineDao.deleteAll();
                invoiceLineDao.insertEntities(listInvoiceLineInCart);
            }
        }).start();
    }

    public void insertReview(ArrayList<Review> list) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                reviewDao.insertEntities(list);
            }
        }).start();
    }
}