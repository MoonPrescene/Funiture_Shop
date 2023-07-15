package com.example.funiture_shop.data.model.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.funiture_shop.R
import com.example.funiture_shop.data.model.entity.Product
import com.example.funiture_shop.databinding.ListItemProductBestSaleBinding
import com.squareup.picasso.Picasso


class BestSaleProductAdapter(
    var listProduct: ArrayList<Product> = arrayListOf(),
    private val onItemProductClickListener: OnItemBestSaleProductListener
) : RecyclerView.Adapter<BestSaleProductAdapter.ViewHolder>() {
    class ViewHolder(
        val binding: ListItemProductBestSaleBinding,
        private val onItemProductClickListener: OnItemBestSaleProductListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(product: Product) {
            binding.product = product
            binding.apply {
                cardNavigate.setOnClickListener {
                    onItemProductClickListener.onItemNavigateClick(product)
                }
            }
            Picasso.get()
                .load(product.imgUrl)
                .into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListItemProductBestSaleBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_product_best_sale,
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

interface OnItemBestSaleProductListener {
    fun onItemNavigateClick(product: Product)
}