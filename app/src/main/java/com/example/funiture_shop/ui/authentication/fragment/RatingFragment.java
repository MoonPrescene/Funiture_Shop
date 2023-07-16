package com.example.funiture_shop.ui.authentication.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.funiture_shop.R;
import com.example.funiture_shop.data.dao.InvoiceLineDao;
import com.example.funiture_shop.data.model.entity.Review;
import com.example.funiture_shop.data.model.res.Res;
import com.example.funiture_shop.databinding.FragmentRatingBinding;
import com.example.funiture_shop.helper.SharedPreferencesHelper;
import com.example.funiture_shop.ui.authentication.viewmodel.RatingViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RatingFragment extends Fragment {

    private RatingViewModel mViewModel;

    private FragmentRatingBinding binding;

    private Review review = new Review("", "", 0f, "", "", "");

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;
    @Inject
    FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(RatingViewModel.class);
        review.setTitle(getArguments().getString("ratingArguments"));
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_rating, container, false
        );
        binding.submitReviewButton.setOnClickListener(view -> {
            if (Objects.requireNonNull(binding.reviewEditText.getText()).toString().isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập đánh giá!", Toast.LENGTH_SHORT).show();
            } else {
                review.setRating(binding.ratingBar.getRating());
                review.setTimeCreate(getCurrentTime());
                review.setTextReview(binding.reviewEditText.getText().toString());
                createReview(review);
            }
        });
        return binding.getRoot();
    }

    public void createReview(Review review) {
        binding.loading.setVisibility(View.VISIBLE);
        review.setCreater(sharedPreferencesHelper.getUserName());
        review.setReviewID(UUID.randomUUID().toString());

        CollectionReference reviewsCollection = db.collection("reviews, comments");
        DocumentReference reviewDocument = reviewsCollection.document(review.getReviewID());

        reviewDocument.set(review)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        binding.loading.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "Đánh giá thành công!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(binding.getRoot()).popBackStack();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.loading.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("SimpleDateFormat")
    public String getCurrentTime() {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        return timeStamp;
    }

}