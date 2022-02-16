package com.yilmaz.coinlog.ui.coinlist

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yilmaz.coinlog.CoinApplication
import com.yilmaz.coinlog.R
import com.yilmaz.coinlog.config.Config
import com.yilmaz.coinlog.databinding.FragmentCoinlistBinding
import com.yilmaz.coinlog.model.models.data_base.FavoriteCoin
import com.yilmaz.coinlog.model.models.info.CoinInfo
import com.yilmaz.coinlog.model.models.listing.Coin
import com.yilmaz.coinlog.service.remote.NewsService
import com.yilmaz.coinlog.ui.detail.BottomSheetCoin
import me.toptas.rssconverter.RssConverterFactory
import me.toptas.rssconverter.RssFeed
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class CoinListFragment : Fragment(), CoinListAdapter.ClickListener, FavoriteCoinListAdapter.ClickListener {
    private val TAG = CoinListFragment::class.java.name

    private var _binding: FragmentCoinlistBinding? = null
    private val binding get() = _binding!!
    var root: View? = null
    private var config = Config()
    //private lateinit var viewModel: DashboardViewModel
    private var favoriteClickStatus = false

    private var coinList = ArrayList<Coin>()                    //CoinListAdapterd
    var adapter:CoinListAdapter? = null

    private var coinListFavorite = ArrayList<Coin>()            //FavoriteCoinListAdapter
    private var favoriteCoinDatabase = ArrayList<FavoriteCoin>() //from database
    var adapter_favorite_coin: FavoriteCoinListAdapter ? = null

    private var coinListInfo =  HashMap<String, CoinInfo>()

    private val viewModel: CoinListViewModel by viewModels {
        CoinListViewModel.
        DashboardViewModelFactory((activity?.applicationContext as CoinApplication).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCoinlistBinding.inflate(inflater, container, false)
        root = binding.root

        //coin list adapter
        adapter = CoinListAdapter(arrayListOf())
        val recyclerView = binding.coinList
        recyclerView.adapter = adapter
        adapter!!.setOnItemClickListener(this)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        //favorite coin list adapter
        adapter_favorite_coin = FavoriteCoinListAdapter(arrayListOf())
        val recyclerViewFavorite = binding.coinListFavorite
        recyclerViewFavorite.adapter = adapter_favorite_coin
        adapter_favorite_coin!!.setOnItemClickListener(this)
        recyclerViewFavorite.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        val refreshView: ProgressBar = binding.progressBarRefresh

        val searchIcon = binding.coinSearch.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.BLACK)

        val cancelIcon = binding.coinSearch.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.BLACK)

        val textView = binding.coinSearch.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
        textView.setTextColor(Color.BLACK)
        textView.hint = "Search"

        viewModel.allMeteData.observe(viewLifecycleOwner){ info ->
            coinListInfo.putAll(info.metaData)
            adapter!!.setInfos(coinListInfo)
        }

        viewModel.downloading.observe(viewLifecycleOwner){
            Log.d(TAG,"downloading $it")
            if(!it)
                refreshView.visibility = View.VISIBLE
            else
                refreshView.visibility = View.GONE

        }

        viewModel.downloadingError.observe(viewLifecycleOwner){
            if(!it.equals("NO_ERROR")){
                refreshView.visibility = View.GONE
                val toast = Toast.makeText(context, "$it ERROR", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }

        //OBSERVER REMOTE COIN LIST - FROM CMC
        viewModel.allCoins.observe(viewLifecycleOwner){
            coinList = it.coin as ArrayList<Coin>
            adapter!!.updateCoinList(coinList)
        }

        //OBSERVER FAVORITE COINS - FROM DATABASE
        viewModel.allFavoriteCoin.observe(viewLifecycleOwner){
                favoriteCoinDatabase.clear()
                favoriteCoinDatabase = it as ArrayList<FavoriteCoin>
                updateFavoriteCoinList()
        }

        //TOGGLE FAVORITE CLICK
        binding.layoutFavorite.setOnClickListener(){
            favoriteClickStatus = !favoriteClickStatus
            if(favoriteClickStatus){
                showFavoriteCoinList()
            }else{
                showCoinList()
            }
        }

        binding.coinSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter!!.filter.filter(newText)
                adapter_favorite_coin!!.filter.filter(newText)
                return false
            }
        })

        //fetchRss()
        return root as View
    }

    private fun fetchRss() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://github.com")
            .addConverterFactory(RssConverterFactory.create())
            .build()

        val service = retrofit.create(NewsService::class.java)

            service.getNews(config.get_news_url())
                .enqueue(object : Callback<RssFeed> {
                    override fun onResponse(call: Call<RssFeed>, response: Response<RssFeed>) {

                        Log.d(TAG, "onResponse: ${response.isSuccessful}")
                        Log.d(TAG, "onResponse: ${response.body()!!.items}")

                    }

                    override fun onFailure(call: Call<RssFeed>, t: Throwable) {
                        Toast.makeText(binding.root.context, "Failed to fetchRss RSS feed!", Toast.LENGTH_SHORT).show()
                    }
                })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.refreshCoinLatest(config.get_coin_limit())
    }

    override fun itemClickListener(coin: Coin, info: CoinInfo) {
        val bottomSheetFragment: BottomSheetCoin = BottomSheetCoin.newInstance(coin, info)
        bottomSheetFragment.show(
            parentFragmentManager,
            "bottomsheet_coin"
        )
    }

    private fun showFavoriteCoinList(){
        binding.layoutFavorite.setBackgroundResource(R.drawable.favorite_view_on_rounded_corner)
        binding.imageViewShowFavorite.setBackgroundResource(R.drawable.favorite_on)
        binding.coinList.visibility = View.INVISIBLE
        binding.coinListFavorite.visibility = View.VISIBLE
        if(coinListFavorite.isEmpty()){
            binding.txtCoinListFavoriteInfo.visibility = View.VISIBLE
        }else{
            binding.txtCoinListFavoriteInfo.visibility = View.INVISIBLE
        }
    }

    private fun showCoinList() {
        binding.layoutFavorite.setBackgroundResource(R.drawable.favorite_view_off_rounded_corner)
        binding.imageViewShowFavorite.setBackgroundResource(R.drawable.favorite_off)
        binding.coinListFavorite.visibility = View.INVISIBLE
        binding.coinList.visibility = View.VISIBLE
        binding.txtCoinListFavoriteInfo.visibility = View.INVISIBLE
    }

    private fun updateFavoriteCoinList(){
        coinListFavorite.clear()
        for(coin in coinList){
            for(favorite in favoriteCoinDatabase){
                if(coin.symbol.equals(favorite.symbol)){
                    Log.d(TAG, "added to coinListFavorite -> ${coin.symbol}")
                    coinListFavorite.add(coin)
                }
            }
        }
        adapter_favorite_coin!!.update_list(coinListFavorite)
        adapter_favorite_coin!!.setInfos(coinListInfo)
    }

    //COINLIST ADAPTER FAVORITE
    override fun itemFavoriteClickListener(coin: Coin, position: Int) {
        var favoriteCoin = FavoriteCoin(0, coin.name, coin.symbol)
        var deleteStatus = false
        for (favoriteDatabase in favoriteCoinDatabase){
            if(favoriteDatabase.symbol == favoriteCoin.symbol){
                deleteStatus = true
                adapter!!.setCryptoFavoriteStatus(coin, position, false)
                viewModel.delete(favoriteDatabase)
                Log.d(TAG, "delete $favoriteDatabase")
            }
        }

        if(!deleteStatus){
            Log.d(TAG, "insert $favoriteCoin")
            adapter!!.setCryptoFavoriteStatus(coin, position, true)
            viewModel.insert(favoriteCoin)
        }
    }

    //FAVORITECOIN ADAPTER FAVORI CLICK
    override fun itemFavoriteAdapterClickListener(coin: Coin, position:Int) {
        for(favorite in favoriteCoinDatabase){
            if(favorite.symbol == coin.symbol){
                viewModel.delete(favorite)
                Log.d(TAG, "delete $favorite")
            }
        }
        adapter_favorite_coin?.remove(position)
        adapter!!.setCryptoFavoriteStatus(coin, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}