package com.yilmaz.coinlog.ui.dashboard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yilmaz.coinlog.R
import com.yilmaz.coinlog.databinding.CoinListItemBinding
import com.yilmaz.coinlog.model.models.info.CoinInfo
import com.yilmaz.coinlog.model.models.info.Info
import com.yilmaz.coinlog.model.models.listing.Coin


class CoinListAdapter(private val coinList: ArrayList<Coin>) : RecyclerView.Adapter<CoinListAdapter.ViewHolder>() {

    private var TAG = CoinListAdapter::class.java.name

    public lateinit var mListener: ClickListener
    private lateinit var myClickHandlers: MyClickHandlers

    private val cryptoListInfo =  HashMap<String, CoinInfo>()

    fun setOnItemClickListener(listener:ClickListener){
        mListener = listener
    }

    interface ClickListener {
        fun itemClickListener(coin:Coin, info: CoinInfo)
        fun itemFavoriteClickListener(coin:Coin)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.coin = coinList[position]
        holder.binding.info = cryptoListInfo.get(coinList[position].symbol)
        holder.binding.itemClick = myClickHandlers
        holder.binding.favorite = myClickHandlers
        val imageUrl = cryptoListInfo.get(coinList[position].symbol)?.logo//infos?.metaData?.get(coinList[position].symbol)?.logo
        imageUrl?.replace("64X64", "200X200")
        holder.binding.imageUrl = imageUrl
        Log.d(TAG, "onBindViewHolder")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        myClickHandlers = MyClickHandlers(parent.context, mListener)
        val binding = DataBindingUtil.inflate<CoinListItemBinding>(inflater,R.layout.coin_list_item, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(val binding: CoinListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }

    class MyClickHandlers(context: Context, listener:ClickListener) {
        private val TAG = MyClickHandlers::class.java.name
        var context: Context
        var listener: ClickListener? = null

        init {
            this.context = context
            this.listener = listener
        }

        fun onButtonClick(view: View?, coin: Coin, info: CoinInfo) {
            listener?.itemClickListener(coin, info)
        }

        fun favoriteClick(view: View?, coin: Coin) {
            listener?.itemFavoriteClickListener(coin)
        }

        fun onButtonLongPressed(view: View?): Boolean {
            return false
        }
    }

    override fun getItemCount(): Int {
        return coinList.size
    }

    fun updateCoinList(newList: ArrayList<Coin>){
        coinList.clear()
        coinList.addAll(newList)
    }

    fun setInfos(t: Info){
        cryptoListInfo.putAll(t.metaData)
        notifyDataSetChanged()
    }

}
