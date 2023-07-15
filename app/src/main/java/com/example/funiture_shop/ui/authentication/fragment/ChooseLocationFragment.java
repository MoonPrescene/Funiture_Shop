package com.example.funiture_shop.ui.authentication.fragment;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.funiture_shop.R;
import com.example.funiture_shop.databinding.FragmentChooseLocationBinding;
import com.example.funiture_shop.ui.authentication.viewmodel.ChooseLocationViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChooseLocationFragment extends Fragment {

    private ChooseLocationViewModel mViewModel;

    public static ChooseLocationFragment newInstance() {
        return new ChooseLocationFragment();
    }

    private GoogleMap googleMap;

    private FragmentChooseLocationBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ChooseLocationViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_location, container, false);
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                }
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        getAddressFromLatLng(latLng);
                    }
                });
            }
        });
        return binding.getRoot();
    }

    private void getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String fullAddress = address.getAddressLine(0); // Get the full address
                // Use the address as needed
                Toast.makeText(requireContext(), fullAddress, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}