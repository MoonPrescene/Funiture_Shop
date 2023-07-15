package com.example.funiture_shop.ui.authentication.fragment;

import static android.app.Activity.RESULT_OK;

import androidx.core.content.FileProvider;
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

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.funiture_shop.R;
import com.example.funiture_shop.data.model.entity.User;
import com.example.funiture_shop.data.model.res.Res;
import com.example.funiture_shop.databinding.FragmentAccountBinding;
import com.example.funiture_shop.helper.SharedPreferencesHelper;
import com.example.funiture_shop.ui.authentication.viewmodel.AccountViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AccountFragment extends Fragment {

    private AccountViewModel mViewModel;
    private FragmentAccountBinding binding;
    private User user = new User();

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_CAPTURE_REQUEST = 2;

    @Inject
    FirebaseFirestore db;
    @Inject
    FirebaseStorage firebaseStorage;
    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        binding.takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(binding.getRoot().getContext(), binding.takePhoto);
                popup.inflate(R.menu.menu_take_photo);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.takeShot:
                                cameraCapture();
                                return true;
                            case R.id.openGallery:
                                openGallery();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();

            }
        });
        getUser();
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

    private User convertToUser(DocumentSnapshot document) {
        return new User(
                document.getId(),
                Objects.requireNonNull(document.getString("name")),
                Objects.requireNonNull(document.getString("address")),
                Objects.requireNonNull(document.getString("imgUrl")),
                0,
                Objects.requireNonNull(document.getString("phoneNumbers"))
        );
    }

    public void getUser() {
        db.collection("users").document(sharedPreferencesHelper.getUserName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = convertToUser(documentSnapshot);
                        Picasso.get().load(user.getImageUrl()).into(binding.imageAvatar);
                        binding.setUser(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(requireContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void observeData() {
        mViewModel.getUploadImageInfo().observe(getViewLifecycleOwner(), new Observer<Res>() {
            @Override
            public void onChanged(Res res) {
                if (res instanceof Res.Success) {
                    Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
                } else if (res instanceof Res.Error) {
                    Toast.makeText(requireContext(), ((Res.Error) res).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void cameraCapture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
        Uri uri = FileProvider.getUriForFile(requireContext(), "com.example.funiture_shop.provider", file);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA_CAPTURE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                addImageToFirebaseStorage(imageUri);
            } else {
                Toast.makeText(requireContext(), "Cannot get image!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CAMERA_CAPTURE_REQUEST) {
            File file = new File(Environment.getExternalStorageDirectory(), "Avatar.jpg");
            Uri uri = FileProvider.getUriForFile(requireContext(), "com.example.funiture_shop.provider", file);
            addImageToFirebaseStorage(uri);
            Log.d("_ACCOUNT", uri.toString());
        }
    }

    private void addImageToFirebaseStorage(Uri image) {
        StorageReference storageRef = firebaseStorage.getReference();

        String fileName = sharedPreferencesHelper.getUserName();
        StorageReference imagesRef = storageRef.child("images/users/" + fileName);

        UploadTask uploadTask = imagesRef.putFile(image);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Exception exception = task.getException();
                    if (exception != null) {
                        throw exception;
                    }
                    Toast.makeText(requireContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return imagesRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    Toast.makeText(requireContext(), "Đẩy ảnh thành công!", Toast.LENGTH_SHORT).show();
                    addImageUrlToFireStore(downloadUrl.toString());
                } else {
                    Toast.makeText(requireContext(), "push image failed lamo!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addImageUrlToFireStore(String image) {
        CollectionReference collectionRef = db.collection("users");
        collectionRef.document(sharedPreferencesHelper.getUserName())
                .update("imgUrl", image)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "update info success!", Toast.LENGTH_SHORT).show();
                            getUser();
                        } else {
                            Toast.makeText(requireContext(), String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}