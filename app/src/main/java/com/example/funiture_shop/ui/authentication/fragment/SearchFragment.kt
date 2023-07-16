package com.example.funiture_shop.ui.authentication.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funiture_shop.R
import com.example.funiture_shop.data.dao.ProductDao
import com.example.funiture_shop.data.model.adapters.BestSaleProductAdapter
import com.example.funiture_shop.data.model.adapters.OnItemBestSaleProductListener
import com.example.funiture_shop.data.model.entity.Product
import com.example.funiture_shop.databinding.FragmentSearchBinding
import com.example.funiture_shop.ui.authentication.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(), OnItemBestSaleProductListener {

    private lateinit var binding: FragmentSearchBinding
    private var searchQuery = ""
    private lateinit var bestSaleProductAdapter: BestSaleProductAdapter
    private var listProduct = arrayListOf<Product>()
    private lateinit var viewModel: SearchViewModel

    @Inject
    lateinit var productDao: ProductDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        searchQuery = SearchFragmentArgs.fromBundle(requireArguments()).searchQuery
        viewModel =
            ViewModelProvider(this).get<SearchViewModel>(SearchViewModel::class.java)
        setupRecyclerView()
        observeData()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
        productDao.getAllEntities().observe(viewLifecycleOwner) { list ->
            Log.d("_SEARCH", list.size.toString())
            listProduct = list as ArrayList<Product>
            bestSaleProductAdapter.listProduct =
                listProduct.filter {
                    it.name.toLowerCase()
                        .contains(searchQuery.toLowerCase()) || it.productId.toLowerCase()
                        .contains(searchQuery.toLowerCase())
                } as ArrayList<Product>
            Log.d(
                "_SEARCH",
                (listProduct.filter { it.name.contains(searchQuery) } as ArrayList<Product>).size.toString())
            bestSaleProductAdapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        bestSaleProductAdapter = BestSaleProductAdapter(listProduct, this)
        binding.recyclerViewBestSale.apply {
            adapter = bestSaleProductAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onItemNavigateClick(product: Product) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToProductDetailFragment(
                product
            )
        )
    }

}