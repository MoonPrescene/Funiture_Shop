package com.example.funiture_shop.ui.authentication.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.funiture_shop.R
import com.example.funiture_shop.common.showToast
import com.example.funiture_shop.data.model.adapters.ProductAdapter
import com.example.funiture_shop.data.model.entity.Product
import com.example.funiture_shop.data.model.res.Res
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
    private val snapHelper = PagerSnapHelper()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel.getList()
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
        binding.apply {

        }
        observeData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
        viewModel.apply {
            listProduct().observe(viewLifecycleOwner) {
                if (it != null) {
                    listProduct = it as ArrayList<Product>
                    productAdapter.listProduct = this@HomeFragment.listProduct
                    productAdapter.notifyDataSetChanged()
                }
            }
            info.observe(viewLifecycleOwner) {
                when (it) {
                    is Res.Success<*> -> {
                        requireContext().showToast("Get list product success!")
                    }

                    is Res.Error -> {
                        requireContext().showToast(
                            it.message
                        )
                    }
                }
            }
        }
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
        snapHelper.attachToRecyclerView(binding.recyclerViewProduct)
    }

}