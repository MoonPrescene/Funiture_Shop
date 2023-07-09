package com.example.funiture_shop.ui.authentication.viewmodel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.funiture_shop.data.model.res.Res;
import com.example.funiture_shop.repository.UserRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AccountViewModel extends ViewModel {
    private final UserRepository userRepository;
    private MutableLiveData<Res> uploadImageInfo = new MutableLiveData<>();
    private MutableLiveData<Res> getUserInfo = new MutableLiveData<>();

    public MutableLiveData<Res> getUploadImageInfo() {
        return uploadImageInfo;
    }

    public MutableLiveData<Res> getUserInfo() {
        return getUserInfo;
    }

    @Inject
    AccountViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void uploadAvatar(Uri image) {
        uploadImageInfo = userRepository.addAvatarToStorage(image);
    }

    public void getUser() {
        getUserInfo = userRepository.getUser();
    }

}