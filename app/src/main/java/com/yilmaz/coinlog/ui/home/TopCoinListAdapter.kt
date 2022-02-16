package com.yilmaz.coinlog.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yilmaz.coinlog.R
import com.yilmaz.coinlog.databinding.TopCoinListItemBinding
import com.yilmaz.coinlog.model.models.info.Info
import com.yilmaz.coinlog.model.models.listing.Coin

class TopCoinListAdapter(private val topCoinList: ArrayList<Coin>)
                                    : RecyclerView.Adapter<TopCoinListAdapter.ViewHolder>(), ClickListener{

    private var TAG = TopCoinListAdapter::class.java.name
    private var infos: Info? = null

    class ViewHolder(val binding: TopCoinListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<TopCoinListItemBinding>(inflater, R.layout.top_coin_list_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.coin = topCoinList[position]
        holder.binding.imageUrl = infos?.metaData?.get(topCoinList[position].symbol)?.logo
    }

    override fun getItemCount(): Int {
        return topCoinList.size
    }

    fun updateCoinList(newCoinList: ArrayList<Coin>){
        topCoinList.clear()
        topCoinList.addAll(newCoinList)
    }

    fun setInfos(t: Info){
        infos = t
        notifyDataSetChanged()
    }

    override fun itemClickListener(view: View) {
        Log.d(TAG, "click")
    }
}