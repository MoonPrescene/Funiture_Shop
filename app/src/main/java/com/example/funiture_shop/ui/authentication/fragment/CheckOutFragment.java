package com.example.funiture_shop.ui.authentication.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.funiture_shop.R;
import com.example.funiture_shop.ui.authentication.viewmodel.CheckOutViewModel;

public class CheckOutFragment extends Fragment {

    private CheckOutViewModel mViewModel;

    public static CheckOutFragment newInstance() {
        return new CheckOutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.check_out_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CheckOutViewModel.class);
        // TODO: Use the ViewModel
    }

}