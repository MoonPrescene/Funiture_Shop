package com.example.funiture_shop.data.model.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funiture_shop.R;
import com.example.funiture_shop.data.model.entity.Chat;
import com.example.funiture_shop.data.model.entity.User;
import com.example.funiture_shop.databinding.ListItemChatBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ChatAdapters extends RecyclerView.Adapter<ChatAdapters.ViewHolder> {
    private ArrayList<Chat> listChat;

    private User user = new User();
    private User customer = new User();

    public ChatAdapters(ArrayList<Chat> listOrder, User owner, User customer) {
        this.listChat = listOrder;
        this.user = owner;
        this.customer = customer;
    }

    public void setListChat(ArrayList<Chat> list) {
        this.listChat = list;
    }

    public ArrayList<Chat> getListChat() {
        return this.listChat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemChatBinding binding;

        public ViewHolder(ListItemChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(Chat chat, int position) {
            if (Objects.equals(chat.getCreater(), user.getEmail())) {
                binding.chatCustomer.setVisibility(View.GONE);
                binding.chatUser.setVisibility(View.VISIBLE);
                binding.userMessage.setText(chat.getMessage());
                Picasso.get().load(user.getImageUrl()).into(binding.userImage);
            } else {
                binding.chatCustomer.setVisibility(View.VISIBLE);
                binding.chatUser.setVisibility(View.GONE);
                binding.customerMessage.setText(chat.getMessage());
                Picasso.get().load(customer.getImageUrl()).into(binding.customerImage);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemChatBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.list_item_chat,
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(listChat.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listChat.size();
    }
}