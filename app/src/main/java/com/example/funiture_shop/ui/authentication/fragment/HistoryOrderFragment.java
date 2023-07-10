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
import com.example.funiture_shop.data.model.adapters.OrderAdapter;
import com.example.funiture_shop.data.model.entity.InvoiceLine;
import com.example.funiture_shop.data.model.entity.Order;
import com.example.funiture_shop.data.model.res.Res;
import com.example.funiture_shop.databinding.FragmentHistoryOrderBinding;
import com.example.funiture_shop.ui.authentication.viewmodel.HistoryOrderViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HistoryOrderFragment extends Fragment {

    private HistoryOrderViewModel mViewModel;

    private FragmentHistoryOrderBinding binding;

    private ArrayList<Order> listOrder = new ArrayList<>();

    private OrderAdapter orderAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(HistoryOrderViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history_order, container, false);
        mViewModel.getOrderInfo();
        setupRecyclerView();
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
        mViewModel.listOrder().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Order> orders) {
                if (orders != null) {
                    listOrder = new ArrayList<>(orders);
                    orderAdapter.setListOrder(listOrder);
                    orderAdapter.notifyDataSetChanged();
                }
            }
        });
        mViewModel.getListOrderInfo().observe(getViewLifecycleOwner(), new Observer<Res>() {
            @Override
            public void onChanged(Res res) {
                if (res instanceof Res.Error) {
                    Toast.makeText(requireContext(), ((Res.Error) res).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(listOrder);
        binding.recyclerview.setAdapter(orderAdapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    }

}