package com.example.funiture_shop.ui.authentication.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.funiture_shop.R
import com.example.funiture_shop.common.showToast
import com.example.funiture_shop.data.model.adapters.BestSaleProductAdapter
import com.example.funiture_shop.data.model.adapters.OnItemBestSaleProductListener
import com.example.funiture_shop.data.model.adapters.OnItemProductClickListener
import com.example.funiture_shop.data.model.adapters.OnItemSearchQueryListener
import com.example.funiture_shop.data.model.adapters.ProductAdapter
import com.example.funiture_shop.data.model.adapters.SearchQueryAdapter
import com.example.funiture_shop.data.model.entity.InvoiceLine
import com.example.funiture_shop.data.model.entity.Product
import com.example.funiture_shop.data.model.entity.SearchQuery
import com.example.funiture_shop.data.model.res.Res
import com.example.funiture_shop.databinding.FragmentHomeBinding
import com.example.funiture_shop.helper.SharedPreferencesHelper
import com.example.funiture_shop.ui.authentication.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), OnItemProductClickListener, OnItemSearchQueryListener,
    OnItemBestSaleProductListener {

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    private var listProduct = arrayListOf<Product>()
    private val snapHelper = PagerSnapHelper()
    private var listInvoiceLineInCart = arrayListOf<InvoiceLine>()
    private var listSearchQuery = arrayListOf<SearchQuery>()
    private lateinit var searchQueryAdapter: SearchQueryAdapter
    private lateinit var bestSaleProductAdapter: BestSaleProductAdapter
    private var searchQuery = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.searchSuggestView.visibility = View.GONE
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s.toString()
                if (s.toString().isBlank()) {
                    binding.searchSuggestView.visibility = View.GONE
                } else {
                    binding.searchSuggestView.visibility = View.VISIBLE
                    val list = listSearchQuery.filter { it.content.contains(searchQuery) }
                    searchQueryAdapter.listSearchQuery = list
                    searchQueryAdapter.notifyDataSetChanged()
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun afterTextChanged(s: Editable?) {
                searchQuery = s.toString()
                if (s.toString().isBlank()) {
                    binding.searchSuggestView.visibility = View.GONE
                } else {
                    binding.searchSuggestView.visibility = View.VISIBLE
                    val list = listSearchQuery.filter { it.content.contains(searchQuery) }
                    searchQueryAdapter.listSearchQuery = list
                    searchQueryAdapter.notifyDataSetChanged()
                }
            }
        })
        binding.search.setOnClickListener {
            navigateToSearch(searchQuery)
        }
        viewModel.getList()
        binding.loading.visibility = View.VISIBLE
        return binding.root
    }

    private fun navigateToSearch(searchQuery: String) {
        val query = SearchQuery(content = searchQuery)
        if (listSearchQuery.isNotEmpty()) {
            if (query !in listSearchQuery) {
                listSearchQuery.add(query)
                viewModel.insertSearchQuery(listSearchQuery)
            }
        } else {
            listSearchQuery.add(query)
            viewModel.insertSearchQuery(listSearchQuery)
        }

        findNavController().navigate(
            HomeFragmentDirections.actionFirstFragmentToSearchFragment(
                searchQuery
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        binding.apply {
            userButton.setOnClickListener {
                val popup = PopupMenu(binding.root.context, binding.userButton)
                popup.inflate(R.menu.menu_user_more)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.editInfo -> {
                            findNavController().navigate(R.id.action_FirstFragment_to_accountFragment)
                            true
                        }

                        R.id.order_history -> {
                            findNavController().navigate(R.id.action_FirstFragment_to_historyOrderFragment)
                            true
                        }

                        R.id.supportUser -> {
                            findNavController().navigate(R.id.action_HomeFragment_to_supportFragment)
                            true
                        }

                        R.id.logout -> {
                            sharedPreferencesHelper.logout()
                            requireActivity().finish()
                            true
                        }

                        else -> false
                    }
                }
                popup.show()
            }
            cart.setOnClickListener {
                findNavController().navigate(R.id.action_FirstFragment_to_cartFragment)
            }
        }
        observeData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
        viewModel.apply {
            listProduct().observe(viewLifecycleOwner) {
                if (it != null) {
                    listProduct = it as ArrayList<Product>
                    productAdapter.listProduct = it
                    productAdapter.notifyDataSetChanged()
                    bestSaleProductAdapter.listProduct = it
                    bestSaleProductAdapter.notifyDataSetChanged()
                }
            }

            listInvoiceLineInCart().observe(viewLifecycleOwner) {
                if (it != null) {
                    listInvoiceLineInCart = it as ArrayList<InvoiceLine>
                    if (listInvoiceLineInCart.isNotEmpty()) {
                        binding.isNotEmpty.visibility = View.VISIBLE
                    } else {
                        binding.isNotEmpty.visibility = View.GONE
                    }
                }
            }
            info.observe(viewLifecycleOwner) {
                binding.loading.visibility = View.GONE
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
            getListSearchQuery().observe(viewLifecycleOwner) {
                if (it != null && it.isNotEmpty()) {
                    listSearchQuery = it as ArrayList<SearchQuery>
                    searchQueryAdapter.listSearchQuery = it
                    searchQueryAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(listProduct, this)
        binding.recyclerViewProduct.apply {
            adapter = productAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        snapHelper.attachToRecyclerView(binding.recyclerViewProduct)

        searchQueryAdapter = SearchQueryAdapter(listSearchQuery, this)
        binding.recyclerHistory.apply {
            adapter = searchQueryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        bestSaleProductAdapter = BestSaleProductAdapter(listProduct, this)
        binding.recyclerViewBestSale.apply {
            adapter = bestSaleProductAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onItemProductClick(product: Product) {
        navigateToProductDetail(product)
    }

    private fun navigateToProductDetail(product: Product) {
        val action = HomeFragmentDirections.actionFirstFragmentToProductDetailFragment(product)
        findNavController().navigate(action)
    }

    override fun onItemProductAdded(product: Product) {
        val existingProduct =
            listInvoiceLineInCart.firstOrNull { it.productId == product.productId }
        if (existingProduct != null) {
            existingProduct.quantity += 1
        } else {
            product.quantity = 1
            listInvoiceLineInCart.add(product.convertToInvoiceLine())
        }
        val message = "Đã thêm sản phẩm " + product.name
        requireContext().showToast(message)
        viewModel.insertInvoiceLines(listInvoiceLineInCart)
    }

    override fun onItemClick(searchQuery: SearchQuery) {
        navigateToSearch(searchQuery.content)
    }

    override fun onItemNavigateClick(product: Product) {
        val action = HomeFragmentDirections.actionFirstFragmentToProductDetailFragment(product)
        findNavController().navigate(action)
    }

}