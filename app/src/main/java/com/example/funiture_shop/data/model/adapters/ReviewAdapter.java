package com.example.funiture_shop.data.model.adapters;


import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funiture_shop.R;
import com.example.funiture_shop.data.model.entity.Review;
import com.example.funiture_shop.databinding.ListItemReviewBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private ArrayList<Review> listReview;
    private OnItemInvoiceLineClickListener onItemInvoiceLineClickListener;
    public ReviewAdapter(ArrayList<Review> listReview) {
        this.listReview = listReview;
    }

    public void setListReview(ArrayList<Review> list) {
        this.listReview = list;
    }

    public ArrayList<Review> getListReview() {
        return this.listReview;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemReviewBinding binding;

        public ViewHolder(ListItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(Review review, int position) {
            binding.setReview(review);
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/newsapp-3d277.appspot.com/o/images%2Fusers%2Fdanchoi2k1perku%40gmail.com?alt=media&token=d3c5ad6c-6b92-4b02-b50f-4d5899762a5f").into(binding.imageView);

            binding.moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(binding.getRoot().getContext(), binding.moreButton);
                    popup.inflate(R.menu.menu_review_more);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.report:

                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show();

                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemReviewBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.list_item_review,
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(listReview.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listReview.size();
    }
}

