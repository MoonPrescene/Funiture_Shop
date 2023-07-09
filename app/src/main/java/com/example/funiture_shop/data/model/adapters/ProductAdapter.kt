package com.example.funiture_shop.data.model.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.funiture_shop.R
import com.example.funiture_shop.data.model.entity.Product
import com.example.funiture_shop.databinding.ListItemProductBinding
import com.squareup.picasso.Picasso

class ProductAdapter(
    var listProduct: ArrayList<Product> = arrayListOf(),
    private val onItemProductClickListener: OnItemProductClickListener
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    class ViewHolder(
        val binding: ListItemProductBinding,
        private val onItemProductClickListener: OnItemProductClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(product: Product) {
            binding.product = product
            binding.apply {
                parentView.setOnClickListener {
                    onItemProductClickListener.onItemProductClick(product)
                }
                addToCartButton.setOnClickListener {
                    onItemProductClickListener.onItemProductAdded(product)
                }
            }
            Picasso.get()
                .load(product.imgUrl)
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListItemProductBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_product,
            parent,
            false
        )
        return ViewHolder(binding, onItemProductClickListener)
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bindView(listProduct[position])
    }
}

interface OnItemProductClickListener {
    fun onItemProductClick(product: Product)
    fun onItemProductAdded(product: Product)
}