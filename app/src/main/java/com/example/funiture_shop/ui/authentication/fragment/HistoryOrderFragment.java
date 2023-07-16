package com.example.funiture_shop.ui.authentication.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.funiture_shop.R;
import com.example.funiture_shop.data.dao.OrderDao;
import com.example.funiture_shop.data.model.adapters.OrderAdapter;
import com.example.funiture_shop.data.model.entity.Order;
import com.example.funiture_shop.databinding.FragmentHistoryOrderBinding;
import com.example.funiture_shop.ui.authentication.viewmodel.HistoryOrderViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HistoryOrderFragment extends Fragment {

    private HistoryOrderViewModel mViewModel;

    private FragmentHistoryOrderBinding binding;

    private ArrayList<Order> listOrder = new ArrayList<>();

    private OrderAdapter orderAdapter;

    @Inject
    FirebaseFirestore db;

    @Inject
    OrderDao orderDao;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(HistoryOrderViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history_order, container, false);
        getListOrder();
        setupRecyclerView();
        observeData();
        return binding.getRoot();
    }

    public void observeData() {
        orderDao.getOrder().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Order> orders) {
                if (orders != null) {
                    listOrder = new ArrayList<>(orders);
                    listOrder = sortListOrderByTime(listOrder);
                    orderAdapter.setListOrder(listOrder);
                    orderAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(listOrder);
        binding.recyclerview.setAdapter(orderAdapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void getListOrder() {
        binding.loading.setVisibility(View.VISIBLE);
        db.collection("orders")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        binding.loading.setVisibility(View.GONE);
                        ArrayList<Order> list = new ArrayList<>();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            list.add(convertToOrder(document));
                        }
                        orderDao.insertEntities(list);
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

    private Order convertToOrder(QueryDocumentSnapshot document) {
        return new Order(
                document.getId(),
                document.getString("userID"),
                Objects.requireNonNull(document.getDouble("quantity")).intValue(),
                document.getString("timeCreate"),
                document.getString("address"),
                Objects.requireNonNull(document.getDouble("status")).intValue(),
                document.getDouble("total")
        );
    }

    private ArrayList<Order> sortListOrderByTime(ArrayList<Order> list) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Collections.sort(list, new Comparator<Order>() {
            @Override
            public int compare(Order item1, Order item2) {
                try {
                    Date date1 = dateFormat.parse(item1.getTimeCreate());
                    Date date2 = dateFormat.parse(item2.getTimeCreate());
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        return list;
    }


}