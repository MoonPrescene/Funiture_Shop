package com.example.funiture_shop.ui.authentication.fragment;

import static android.app.Activity.RESULT_OK;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.funiture_shop.R;
import com.example.funiture_shop.data.model.entity.User;
import com.example.funiture_shop.data.model.res.Res;
import com.example.funiture_shop.databinding.FragmentAccountBinding;
import com.example.funiture_shop.helper.SharedPreferencesHelper;
import com.example.funiture_shop.ui.authentication.viewmodel.AccountViewModel;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class AccountFragment extends Fragment {

    private AccountViewModel mViewModel;
    private FragmentAccountBinding binding;

    private User user = new User();

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        Picasso.get().load(user.getImageUrl()).into(binding.imageAvatar);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.back).popBackStack();
            }
        });
        binding.takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        binding.setUser(user);
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferencesHelper.logout();
                requireActivity().finish();
            }
        });

        observeData();
        return binding.getRoot();
    }

    private void observeData() {
        mViewModel.getUploadImageInfo().observe(getViewLifecycleOwner(), new Observer<Res>() {
            @Override
            public void onChanged(Res res) {
                if (res instanceof Res.Success) {
                    Toast.makeText(requireContext(), "Đẩy ảnh thành công!", Toast.LENGTH_SHORT).show();
                } else if (res instanceof Res.Error) {
                    Toast.makeText(requireContext(), ((Res.Error) res).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mViewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<Res>() {
            @Override
            public void onChanged(Res res) {
                if (res instanceof Res.Success) {
                    user = (User) ((Res.Success<?>) res).getData();
                    binding.setUser(user);
                } else if (res instanceof Res.Error) {
                    Toast.makeText(requireContext(), ((Res.Error) res).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static final int PICK_IMAGE_REQUEST = 1;

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                mViewModel.uploadAvatar(imageUri);
            } else {
                Toast.makeText(requireContext(), "Cannot get image!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}