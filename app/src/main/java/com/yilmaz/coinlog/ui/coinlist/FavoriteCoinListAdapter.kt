package com.yilmaz.coinlog.ui.coinlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yilmaz.coinlog.R
import com.yilmaz.coinlog.databinding.FavoriteCoinListItemBinding
import com.yilmaz.coinlog.model.models.info.CoinInfo
import com.yilmaz.coinlog.model.models.listing.Coin
import java.util.*

class FavoriteCoinListAdapter(private val favorite_list: ArrayList<Coin>): RecyclerView.Adapter<FavoriteCoinListAdapter.ViewHolder>(),
    Filterable {
    private var TAG = FavoriteCoinListAdapter::class.java.name

    private val cryptoListInfo =  HashMap<String, CoinInfo>()

    private lateinit var mListener: ClickListener
    private lateinit var myClickHandlers: ClickHandlers

    var coinListFiltered = ArrayList<Coin>()

    init {
        coinListFiltered = favorite_list
    }

    fun setOnItemClickListener(listener: ClickListener){
        mListener = listener
    }

    interface ClickListener {
        fun itemClickListener(coin:Coin, info: CoinInfo)
        fun itemFavoriteAdapterClickListener(coin:Coin, position: Int)
    }

    class ViewHolder(val binding: FavoriteCoinListItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    class ClickHandlers(context: Context, listener: ClickListener) {
        private val TAG = ClickHandlers::class.java.name
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
            listener?.itemFavoriteAdapterClickListener(coin, position)
        }

        fun onButtonLongPressed(view: View?): Boolean {
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        myClickHandlers = ClickHandlers(parent.context, mListener)

        val binding = DataBindingUtil.inflate<FavoriteCoinListItemBinding>(inflater, R.layout.favorite_coin_list_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.coin = coinListFiltered[position]
        holder.binding.info = cryptoListInfo.get(coinListFiltered[position].symbol)
        val imageUrl = cryptoListInfo.get(coinListFiltered[position].symbol)?.logo
        holder.binding.favorite = myClickHandlers
        holder.binding.position = position
        holder.binding.favoriteStatus = true
        imageUrl?.replace("64X64", "200X200")
        holder.binding.imageUrl = imageUrl
     }

    override fun getItemCount(): Int {
        return coinListFiltered.size
    }

    fun update_list(newList: ArrayList<Coin>){
        favorite_list.clear()
        favorite_list.addAll(newList)
    }

    fun setInfos(metadata: HashMap<String, CoinInfo>){
        cryptoListInfo.clear()
        cryptoListInfo.putAll(metadata)
        notifyDataSetChanged()
    }

    fun remove(position: Int){
        notifyItemRemoved(position)
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    coinListFiltered = favorite_list
                }
                val resultList = ArrayList<Coin>()
                for(row in favorite_list){
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