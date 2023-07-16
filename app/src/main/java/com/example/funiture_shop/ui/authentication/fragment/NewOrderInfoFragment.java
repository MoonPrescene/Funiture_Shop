package com.example.funiture_shop.ui.authentication.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.funiture_shop.R;
import com.example.funiture_shop.data.dao.InvoiceLineDao;
import com.example.funiture_shop.data.model.entity.Order;
import com.example.funiture_shop.databinding.FragmentNewOrderInfoBinding;
import com.example.funiture_shop.ui.authentication.viewmodel.NewOrderInfoViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewOrderInfoFragment extends Fragment {

    private NewOrderInfoViewModel mViewModel;
    private FragmentNewOrderInfoBinding binding;

    LatLng locationAddress = new LatLng(0, 0);

    public static class Companion {
        public static LatLng location;

        public static void setLocation(LatLng l) {
            location = l;
        }
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
        if (Companion.location != null) {
            getAddressFromLatLng(Companion.location);
        } else {
            Toast.makeText(requireContext(), "NULL", Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    public void createOrder(Order order) {
        if (!binding.test.getText().toString().isEmpty()) {
            order.setAddress(binding.test.getText().toString());
        }
        db.collection("orders").document(order.getOrderID()).set(order)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "Tạo đơn thành công!", Toast.LENGTH_SHORT).show();
                    deleteAllInvoiceLine();
                    Navigation.findNavController(requireView()).popBackStack(R.id.HomeFragment, false);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String fullAddress = address.getAddressLine(0);
                Toast.makeText(requireContext(), fullAddress, Toast.LENGTH_SHORT).show();// Get the full address
                binding.test.setText(fullAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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