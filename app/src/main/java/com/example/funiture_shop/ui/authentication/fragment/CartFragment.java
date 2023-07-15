package com.example.funiture_shop.ui.authentication.fragment;

import static com.example.funiture_shop.di.AppExecutorsKt.runOnIoThread;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
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
import com.example.funiture_shop.data.dao.InvoiceLineDao;
import com.example.funiture_shop.data.model.adapters.InvoiceLineAdapter;
import com.example.funiture_shop.data.model.adapters.OnItemInvoiceLineClickListener;
import com.example.funiture_shop.data.model.entity.InvoiceLine;
import com.example.funiture_shop.data.model.entity.Order;
import com.example.funiture_shop.data.model.res.Res;
import com.example.funiture_shop.databinding.FragmentCartBinding;
import com.example.funiture_shop.helper.SharedPreferencesHelper;
import com.example.funiture_shop.ui.authentication.viewmodel.CartViewModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CartFragment extends Fragment implements OnItemInvoiceLineClickListener {

    private CartViewModel mViewModel;
    private FragmentCartBinding binding;
    private Order order = new Order("", "", 0, "", "", 0, 0.0);
    private InvoiceLineAdapter invoiceLineAdapter;
    private ArrayList<InvoiceLine> listInvoiceLineInCart = new ArrayList<>();


    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    @Inject
    InvoiceLineDao invoiceLineDao;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_cart,
                container,
                false
        );
        setupRecyclerView();
        binding.newOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewOrder();
            }
        });
        binding.newOrderButton.setEnabled(listInvoiceLineInCart.isEmpty());
        observeData();
        return binding.getRoot();
    }

    public void observeData() {
        invoiceLineDao.getInvoiceLines().observe(getViewLifecycleOwner(), new Observer<List<InvoiceLine>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<InvoiceLine> invoiceLines) {
                if (invoiceLines != null) {
                    listInvoiceLineInCart = new ArrayList<>(invoiceLines);
                    invoiceLineAdapter.setListInvoiceLine(listInvoiceLineInCart);
                    invoiceLineAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setupRecyclerView() {
        invoiceLineAdapter = new InvoiceLineAdapter(listInvoiceLineInCart, this);
        binding.recyclerview.setAdapter(invoiceLineAdapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void selectInvoiceLine(InvoiceLine invoiceLine, int index) {
        listInvoiceLineInCart.set(index, invoiceLine);
        invoiceLineAdapter.setListInvoiceLine(listInvoiceLineInCart);
        binding.sumPrice.setText(updateSumPrice());
        invoiceLineAdapter.notifyDataSetChanged();
    }

    @Override
    public void add(InvoiceLine invoiceLine, int index) {
        listInvoiceLineInCart.set(index, invoiceLine);
        invoiceLineAdapter.setListInvoiceLine(listInvoiceLineInCart);
        binding.sumPrice.setText(updateSumPrice());
        invoiceLineAdapter.notifyItemChanged(index);
    }

    @Override
    public void sub(InvoiceLine invoiceLine, int index) {
        listInvoiceLineInCart.set(index, invoiceLine);
        invoiceLineAdapter.setListInvoiceLine(listInvoiceLineInCart);
        binding.sumPrice.setText(updateSumPrice());
        invoiceLineAdapter.notifyItemChanged(index);
    }

    public String updateSumPrice() {
        double sumPrice = 0.0;
        for (InvoiceLine invoiceLine : listInvoiceLineInCart) {
            if (invoiceLine.isSelected()) {
                sumPrice += invoiceLine.sumPrice();
            }
        }
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return numberFormat.format(sumPrice / 1000.0);
    }

    public ArrayList<InvoiceLine> updateListInvoiceLine() {
        ArrayList<InvoiceLine> list = new ArrayList<>();
        for (InvoiceLine invoiceLine : listInvoiceLineInCart) {
            if (invoiceLine.isSelected()) {
                list.add(invoiceLine);
            }
        }
        return list;
    }

    public void createNewOrder() {
        ArrayList<InvoiceLine> list = new ArrayList<>();
        double sumPrice = 0.0;
        list = updateListInvoiceLine();
        for (InvoiceLine invoiceLine : list) {
            sumPrice += invoiceLine.sumPrice();
        }
        if (!list.isEmpty()) {
            order.setList(list);
        }
        order.setTimeCreate(getCurrentTime());
        order.setQuantity(list.size());
        order.setTotal(sumPrice);
        order.setStatus(1);
        order.setUserID(sharedPreferencesHelper.getUserName());
        order.setOrderID(UUID.randomUUID().toString());
        Bundle args = new Bundle();
        args.putSerializable("order", order);
        NewOrderInfoFragment destinationFragment = new NewOrderInfoFragment();
        destinationFragment.setArguments(args);
        Navigation.findNavController(requireView()).navigate(R.id.action_cartFragment_to_newOrderInfoFragment, args);
        //createOrder(order);
    }

    @SuppressLint("SimpleDateFormat")
    public String getCurrentTime() {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        return timeStamp;
    }
}