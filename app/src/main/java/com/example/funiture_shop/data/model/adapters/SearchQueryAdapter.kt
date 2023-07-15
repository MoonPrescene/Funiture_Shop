package com.example.funiture_shop.data.model.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.funiture_shop.R
import com.example.funiture_shop.data.model.entity.SearchQuery
import com.example.funiture_shop.databinding.ListItemSearchQueryBinding

class SearchQueryAdapter(
    var listSearchQuery: List<SearchQuery> = listOf(),
    private val onItemSearchQueryListener: OnItemSearchQueryListener
) : RecyclerView.Adapter<SearchQueryAdapter.ViewHolder>() {
    class ViewHolder(
        private val binding: ListItemSearchQueryBinding,
        private val onItemSearchQueryListener: OnItemSearchQueryListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(searchQuery: SearchQuery) {
            binding.searchQuery = searchQuery
            binding.parentView.setOnClickListener {
                onItemSearchQueryListener.onItemClick(searchQuery)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListItemSearchQueryBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_search_query,
            parent,
            false
        )

        return ViewHolder(binding, onItemSearchQueryListener)
    }

    override fun getItemCount(): Int {
        return listSearchQuery.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listSearchQuery[position])
    }
}

interface OnItemSearchQueryListener {
    fun onItemClick(searchQuery: SearchQuery)
}