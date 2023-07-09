package com.example.funiture_shop.data.model.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funiture_shop.R;
import com.example.funiture_shop.data.model.entity.InvoiceLine;
import com.example.funiture_shop.databinding.ListItemInvoiceLineBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InvoiceLineAdapter extends RecyclerView.Adapter<InvoiceLineAdapter.ViewHolder> {
    private ArrayList<InvoiceLine> listInvoiceLine;
    private OnItemInvoiceLineClickListener onItemInvoiceLineClickListener;

    public InvoiceLineAdapter(ArrayList<InvoiceLine> listInvoiceLine, OnItemInvoiceLineClickListener onItemInvoiceLineClickListener) {
        this.listInvoiceLine = listInvoiceLine;
        this.onItemInvoiceLineClickListener = onItemInvoiceLineClickListener;
    }

    public void setListInvoiceLine(ArrayList<InvoiceLine> list) {
        this.listInvoiceLine = list;
    }

    public ArrayList<InvoiceLine> getListInvoiceLine() {
        return this.listInvoiceLine;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemInvoiceLineBinding binding;

        public ViewHolder(ListItemInvoiceLineBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(InvoiceLine invoiceLine, int position) {
            binding.setInvoiceLine(invoiceLine);
            Picasso.get().load(invoiceLine.getImgUrl()).into(binding.imageView);
            binding.checkBox.setChecked(invoiceLine.isSelected());

            binding.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invoiceLine.setQuantity(invoiceLine.getQuantity() + 1);
                    onItemInvoiceLineClickListener.add(invoiceLine, position);
                }
            });

            binding.subButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (invoiceLine.getQuantity() > 0) {
                        invoiceLine.setQuantity(invoiceLine.getQuantity() - 1);
                    }
                    onItemInvoiceLineClickListener.sub(invoiceLine, position);
                }
            });

            binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    invoiceLine.setSelected(isChecked);
                    onItemInvoiceLineClickListener.selectInvoiceLine(invoiceLine, position);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemInvoiceLineBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.list_item_invoice_line,
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(listInvoiceLine.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listInvoiceLine.size();
    }
}




