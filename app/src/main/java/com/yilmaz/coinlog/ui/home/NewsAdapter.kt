package com.yilmaz.coinlog.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yilmaz.coinlog.R
import com.yilmaz.coinlog.databinding.ItemContainerNewsBinding
import me.toptas.rssconverter.RssItem


class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val TAG = NewsAdapter::class.java.name

    private val itemList = ArrayList<RssItem>()


    init {
        Log.d(TAG, "init: ")
    }

    class ViewHolder(val binding: ItemContainerNewsBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemContainerNewsBinding>(
            inflater,
            R.layout.item_container_news,
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.news = itemList[position]
        holder.binding.position = position
        Log.d(TAG, "onBindViewHolder: ${itemList[position].image}")
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setItems(items: List<RssItem>?) {
        itemList.clear()
        itemList.addAll(items!!)
        notifyDataSetChanged()
    }
}