package com.example.funiture_shop.data.model.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.funiture_shop.R;
import com.example.funiture_shop.data.model.entity.Order;
import com.example.funiture_shop.databinding.ListItemOrdersBinding;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private ArrayList<Order> listOrder;

    public OrderAdapter(ArrayList<Order> listOrder) {
        this.listOrder = listOrder;
    }

    public void setListOrder(ArrayList<Order> list) {
        this.listOrder = list;
    }

    public ArrayList<Order> getListOrder() {
        return this.listOrder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemOrdersBinding binding;

        public ViewHolder(ListItemOrdersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(Order order, int position) {
            binding.setOrder(order);


            binding.moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(binding.getRoot().getContext(), binding.moreButton);
                    popup.inflate(R.menu.menu_order_more);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.deleteOrder:

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
        ListItemOrdersBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.list_item_orders,
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(listOrder.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }
}

