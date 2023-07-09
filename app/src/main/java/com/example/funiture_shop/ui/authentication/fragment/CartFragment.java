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
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.funiture_shop.R;
import com.example.funiture_shop.data.model.adapters.InvoiceLineAdapter;
import com.example.funiture_shop.data.model.adapters.OnItemInvoiceLineClickListener;
import com.example.funiture_shop.data.model.entity.InvoiceLine;
import com.example.funiture_shop.data.model.entity.Order;
import com.example.funiture_shop.data.model.res.Res;
import com.example.funiture_shop.databinding.FragmentCartBinding;
import com.example.funiture_shop.ui.authentication.viewmodel.CartViewModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CartFragment extends Fragment implements OnItemInvoiceLineClickListener {

    private CartViewModel mViewModel;
    private FragmentCartBinding binding;
    private Order order = new Order();
    private InvoiceLineAdapter invoiceLineAdapter;
    private ArrayList<InvoiceLine> listInvoiceLineInCart = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_cart,
                container,
                false
        );
        setupRecyclerView();
        binding.newOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewOrder();
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.back).popBackStack();
            }
        });
        observeData();
        return binding.getRoot();
    }

    public void observeData() {
        mViewModel.listInvoiceLineInCart().observe(getViewLifecycleOwner(), new Observer<List<InvoiceLine>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<InvoiceLine> invoiceLines) {
                if (invoiceLines != null) {
                    Log.d("_CART", String.valueOf(invoiceLines.size()));
                    listInvoiceLineInCart = new ArrayList<>(invoiceLines);
                    Log.d("_CART", String.valueOf(listInvoiceLineInCart.size()));
                    if (!listInvoiceLineInCart.isEmpty()) {
                        binding.isNotEmpty.setVisibility(View.VISIBLE);
                        binding.sumPrice.setText(updateSumPrice());
                        if (updateListInvoiceLine().isEmpty()) {
                            binding.newOrderButton.setEnabled(false);
                        } else {
                            binding.newOrderButton.setEnabled(true);
                        }

                    } else {
                        binding.newOrderButton.setEnabled(false);
                        binding.isNotEmpty.setVisibility(View.GONE);
                    }
                    invoiceLineAdapter.setListInvoiceLine(listInvoiceLineInCart);
                    invoiceLineAdapter.notifyDataSetChanged();
                }
            }
        });
        mViewModel.getCreateOrderInfo().observe(getViewLifecycleOwner(), new Observer<Res>() {
            @Override
            public void onChanged(Res res) {
                if (res instanceof Res.Success) {
                    Toast.makeText(requireContext(), "Tạo đơn thành công!", Toast.LENGTH_SHORT).show();
                } else if (res instanceof Res.Error) {
                    Toast.makeText(requireContext(), ((Res.Error) res).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupRecyclerView() {
        invoiceLineAdapter = new InvoiceLineAdapter(listInvoiceLineInCart, this);
        binding.recyclerview.setAdapter(invoiceLineAdapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void selectInvoiceLine(InvoiceLine invoiceLine, int index) {
        listInvoiceLineInCart.set(index, invoiceLine);
        invoiceLineAdapter.setListInvoiceLine(listInvoiceLineInCart);
        binding.sumPrice.setText(updateSumPrice());
        invoiceLineAdapter.notifyDataSetChanged();
    }

    @Override
    public void add(InvoiceLine invoiceLine, int index) {
        listInvoiceLineInCart.set(index, invoiceLine);
        invoiceLineAdapter.setListInvoiceLine(listInvoiceLineInCart);
        binding.sumPrice.setText(updateSumPrice());
        invoiceLineAdapter.notifyItemChanged(index);
    }

    @Override
    public void sub(InvoiceLine invoiceLine, int index) {
        listInvoiceLineInCart.set(index, invoiceLine);
        invoiceLineAdapter.setListInvoiceLine(listInvoiceLineInCart);
        binding.sumPrice.setText(updateSumPrice());
        invoiceLineAdapter.notifyItemChanged(index);
    }

    public String updateSumPrice() {
        double sumPrice = 0.0;
        for (InvoiceLine invoiceLine : listInvoiceLineInCart) {
            if (invoiceLine.isSelected()) {
                sumPrice += invoiceLine.sumPrice();
            }
        }
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return numberFormat.format(sumPrice / 1000.0);
    }

    public ArrayList<InvoiceLine> updateListInvoiceLine() {
        ArrayList<InvoiceLine> list = new ArrayList<>();
        for (InvoiceLine invoiceLine : listInvoiceLineInCart) {
            if (invoiceLine.isSelected()) {
                list.add(invoiceLine);
            }
        }
        return list;
    }

    public void createNewOrder() {
        ArrayList<InvoiceLine> list = new ArrayList<>();
        double sumPrice = 0.0;
        list = updateListInvoiceLine();
        for (InvoiceLine invoiceLine : list) {
            sumPrice += invoiceLine.sumPrice();
        }
        if (!list.isEmpty()) {
            order.setList(list);
        }
        order.setTimeCreate(getCurrentTime());
        order.setQuantity(list.size());
        order.setTotal(sumPrice);
        order.setStatus(1);
        mViewModel.createOrder(order);
    }

    @SuppressLint("SimpleDateFormat")
    public String getCurrentTime() {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        return timeStamp;
    }
}