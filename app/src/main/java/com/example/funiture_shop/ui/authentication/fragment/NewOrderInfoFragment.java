package com.example.funiture_shop.ui.authentication.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.funiture_shop.AR_Activity;
import com.example.funiture_shop.R;
import com.example.funiture_shop.data.dao.InvoiceLineDao;
import com.example.funiture_shop.data.model.entity.Order;
import com.example.funiture_shop.databinding.FragmentNewOrderInfoBinding;
import com.example.funiture_shop.ui.authentication.viewmodel.NewOrderInfoViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewOrderInfoFragment extends Fragment {

    private NewOrderInfoViewModel mViewModel;
    private FragmentNewOrderInfoBinding binding;

    public static NewOrderInfoFragment newInstance() {
        return new NewOrderInfoFragment();
    }

    private Order order;

    @Inject
    FirebaseFirestore db;
    @Inject
    InvoiceLineDao invoiceLineDao;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(NewOrderInfoViewModel.class);
        order = (Order) getArguments().getSerializable("order");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_order_info, container, false);
        binding.setOrder(order);
        binding.address.setOnClickListener(view -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_newOrderInfoFragment_to_chooseLocationFragment);
        });
        binding.newOrderButton.setOnClickListener(view -> {
            createOrder(order);
        });
        return binding.getRoot();
    }

    public void createOrder(Order order) {
        db.collection("orders").document(order.getOrderID()).set(order)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "Tạo đơn thành công!", Toast.LENGTH_SHORT).show();
                    deleteAllInvoiceLine();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    public void deleteAllInvoiceLine() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                invoiceLineDao.deleteAll();
            }
        }).start();
    }
}