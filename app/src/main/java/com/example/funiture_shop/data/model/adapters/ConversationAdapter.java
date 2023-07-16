package com.example.funiture_shop.data.model.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funiture_shop.R;
import com.example.funiture_shop.data.model.entity.User;
import com.example.funiture_shop.databinding.ListItemConversationBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {
    private ArrayList<User> listUser;
    private OnItemConversationClick onItemConversationClick;

    public ConversationAdapter(ArrayList<User> listUser, OnItemConversationClick onItemConversationClick) {
        this.listUser = listUser;
        this.onItemConversationClick = onItemConversationClick;
    }

    public void setListUser(ArrayList<User> list) {
        this.listUser = list;
    }

    public ArrayList<User> getListUser() {
        return this.listUser;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemConversationBinding binding;

        public ViewHolder(ListItemConversationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(User user, int position) {
            binding.customerName.setText(user.getName());
            Picasso.get().load(user.getImageUrl()).into(binding.imageView);
            binding.conversation.setOnClickListener(view -> {
                onItemConversationClick.onItemClick(user);
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemConversationBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.list_item_conversation,
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(listUser.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }
}

