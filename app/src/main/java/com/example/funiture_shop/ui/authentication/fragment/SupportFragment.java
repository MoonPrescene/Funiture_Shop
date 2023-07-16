package com.example.funiture_shop.ui.authentication.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.funiture_shop.R;
import com.example.funiture_shop.data.model.adapters.ConversationAdapter;
import com.example.funiture_shop.data.model.adapters.OnItemConversationClick;
import com.example.funiture_shop.data.model.adapters.ReviewAdapter;
import com.example.funiture_shop.data.model.entity.User;
import com.example.funiture_shop.databinding.FragmentSupportBinding;
import com.example.funiture_shop.helper.SharedPreferencesHelper;
import com.example.funiture_shop.ui.authentication.viewmodel.SupportViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SupportFragment extends Fragment implements OnItemConversationClick {

    private SupportViewModel mViewModel;

    private FragmentSupportBinding binding;

    @Inject
    FirebaseFirestore db;

    private ArrayList<User> listUser = new ArrayList<>();
    private User owner = new User();
    private ConversationAdapter conversationAdapter;

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(SupportViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_support, container, false);
        setupRecyclerView();
        getUsers();
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

    public void getUsers() {
        db.collection("users")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<User> list = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            User user = convertToUser(document);
                            if (user.getEmail().equals(sharedPreferencesHelper.getUserName())) {
                                owner = user;
                            } else {
                                list.add(user);
                            }
                        }
                        listUser = list;
                        conversationAdapter.setListUser(listUser);
                        conversationAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupRecyclerView() {
        conversationAdapter = new ConversationAdapter(listUser, this);
        binding.recyclerView.setAdapter(conversationAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onItemClick(User customer) {
        Bundle args = new Bundle();
        args.putSerializable("owner", owner);
        args.putSerializable("customer", customer);
        ChatFragment destinationFragment = new ChatFragment();
        destinationFragment.setArguments(args);
        Navigation.findNavController(requireView()).navigate(R.id.action_supportFragment_to_chatFragment, args);
    }
}