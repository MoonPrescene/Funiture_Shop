package com.example.funiture_shop.ui.authentication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funiture_shop.R
import com.example.funiture_shop.data.model.adapters.ProductAdapter
import com.example.funiture_shop.data.model.entity.Product
import com.example.funiture_shop.databinding.FragmentHomeBinding
import com.example.funiture_shop.helper.SharedPreferencesHelper
import com.example.funiture_shop.ui.authentication.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    private var listProduct = arrayListOf<Product>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        /*binding.buttonFirst.setOnClickListener {
            sharedPreferencesHelper.logout()
            requireActivity().finish()
        }*/
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        if (!::productAdapter.isInitialized) {
            productAdapter = ProductAdapter(listProduct)
            binding.recyclerViewProduct.apply {
                adapter = productAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

}