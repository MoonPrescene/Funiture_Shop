package com.example.funiture_shop.ui.authentication.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.funiture_shop.R;
import com.example.funiture_shop.data.model.adapters.ChatAdapters;
import com.example.funiture_shop.data.model.adapters.ConversationAdapter;
import com.example.funiture_shop.data.model.entity.Chat;
import com.example.funiture_shop.data.model.entity.Order;
import com.example.funiture_shop.data.model.entity.User;
import com.example.funiture_shop.databinding.FragmentChatBinding;
import com.example.funiture_shop.helper.SharedPreferencesHelper;
import com.example.funiture_shop.ui.authentication.viewmodel.ChatViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatFragment extends Fragment {

    private ChatViewModel mViewModel;

    @Inject
    FirebaseDatabase db;
    private FragmentChatBinding binding;
    private DatabaseReference databaseRef;
    private ArrayList<Chat> listChat = new ArrayList<>();
    private User owner = new User();
    private User customer = new User();
    private ChatAdapters chatAdapters;

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        owner = (User) getArguments().getSerializable("owner");
        customer = (User) getArguments().getSerializable("customer");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
        databaseRef = db.getReference();
        setupRecyclerView();
        getChats();
        binding.sendButton.setOnClickListener(view -> {
            createChat();
        });
        return binding.getRoot();
    }

    private void createChat() {
        Chat chat = new Chat();
        chat.setCreater(sharedPreferencesHelper.getUserName());
        chat.setTimeCreate(getCurrentTime());
        if (!binding.message.getText().toString().equals("")) {
            String key = databaseRef.push().getKey();
            chat.setMessage(binding.message.getText().toString());
            databaseRef.child(key).setValue(chat);
        }
        hideKeyboard(requireActivity());
        binding.message.setText("");
        binding.message.clearFocus();
        getChats();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getChats() {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listChat.clear();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Chat chat = userSnapshot.getValue(Chat.class);
                        listChat.add(chat);
                    }
                    ArrayList<Chat> list = new ArrayList<>();
                    for (Chat chat : listChat) {
                        if (Objects.equals(chat.getCreater(), owner.getEmail()) || Objects.equals(chat.getCreater(), customer.getEmail())) {
                            list.add(chat);
                        }
                    }
                    listChat = sortListChatByTime(list);
                    chatAdapters.setListChat(listChat);
                    chatAdapters.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    public String getCurrentTime() {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        return timeStamp;
    }

    private void setupRecyclerView() {
        chatAdapters = new ChatAdapters(listChat, owner, customer);
        binding.recyclerviewChat.setAdapter(chatAdapters);
        binding.recyclerviewChat.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    }


    private ArrayList<Chat> sortListChatByTime(ArrayList<Chat> list) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Collections.sort(list, new Comparator<Chat>() {
            @Override
            public int compare(Chat item1, Chat item2) {
                try {
                    Date date1 = dateFormat.parse(item1.getTimeCreate());
                    Date date2 = dateFormat.parse(item2.getTimeCreate());
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        return list;
    }


}
