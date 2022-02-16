package com.yilmaz.coinlog.ui.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yilmaz.coinlog.R
import com.yilmaz.coinlog.databinding.CoinListItemBinding
import com.yilmaz.coinlog.model.models.info.CoinInfo
import com.yilmaz.coinlog.model.models.listing.Coin
import java.util.*


class CoinListAdapter(private val coinList: ArrayList<Coin>) : RecyclerView.Adapter<CoinListAdapter.ViewHolder>(), Filterable {

    private var TAG = CoinListAdapter::class.java.name

    private lateinit var mListener: ClickListener
    private lateinit var myClickHandlers: MyClickHandlers

    private val cryptoListInfo =  HashMap<String, CoinInfo>()

    var coinListFiltered = ArrayList<Coin>()
    var coinListFilteredFavoriteStatus = HashMap<String, Boolean>()

    init {
        coinListFiltered = coinList

        //set all false
        for(coin in coinListFiltered)
            coinListFilteredFavoriteStatus[coin.symbol] = false
    }

    fun setOnItemClickListener(listener:ClickListener){
        mListener = listener
    }

    interface ClickListener {
        fun itemClickListener(coin:Coin, info: CoinInfo)
        fun itemFavoriteClickListener(coin:Coin, position: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.coin = coinListFiltered[position]
        holder.binding.info = cryptoListInfo.get(coinListFiltered[position].symbol)
        holder.binding.itemClick = myClickHandlers
        holder.binding.favorite = myClickHandlers
        holder.binding.position = position
        val imageUrl = cryptoListInfo.get(coinListFiltered[position].symbol)?.logo
        imageUrl?.replace("64X64", "200X200")
        holder.binding.imageUrl = imageUrl
        holder.binding.favoriteStatus = coinListFilteredFavoriteStatus.get(coinListFiltered[position].symbol)
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

        fun favoriteClick(view: View?, coin: Coin, position: Int) {
            listener?.itemFavoriteClickListener(coin, position)
        }

        fun onButtonLongPressed(view: View?): Boolean {
            return false
        }
    }

    override fun getItemCount(): Int {
        return coinListFiltered.size
    }

    fun updateCoinList(newList: ArrayList<Coin>){
        coinList.clear()
        coinList.addAll(newList)
    }

    fun setInfos(metadata: HashMap<String, CoinInfo>){
        cryptoListInfo.putAll(metadata)
        notifyDataSetChanged()
    }

    fun setCryptoFavoriteStatus(favoriteCoin: Coin, position: Int, status: Boolean){
        coinListFilteredFavoriteStatus[favoriteCoin.symbol] = status
        notifyItemChanged(position)
    }

    fun setCryptoFavoriteStatus(favoriteCoin: Coin, status: Boolean){
        coinListFilteredFavoriteStatus[favoriteCoin.symbol] = status
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    coinListFiltered = coinList
                }
                val resultList = ArrayList<Coin>()
                for(row in coinList){
                    var coin_name = row.name
                    var coin_symbol = row.symbol
                    if (coin_name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))
                        || coin_symbol.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(
                            Locale.ROOT)))
                    {
                        resultList.add(row)
                    }
                    coinListFiltered = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = coinListFiltered
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                coinListFiltered = results?.values as ArrayList<Coin>
                notifyDataSetChanged()
            }
        }
    }
}
