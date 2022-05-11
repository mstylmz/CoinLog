package com.yilmaz.coinlog.ui.home

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dfl.newsapi.NewsApiRepository
import com.dfl.newsapi.enums.Category
import com.dfl.newsapi.enums.Country
import com.dfl.newsapi.enums.Language
import com.dfl.newsapi.enums.SortBy
import com.yilmaz.coinlog.CoinApplication
import com.yilmaz.coinlog.config.Config
import com.yilmaz.coinlog.databinding.FragmentHomeBinding
import com.yilmaz.coinlog.model.models.listing.Coin
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeFragment : Fragment() {

    private val TAG = HomeFragment::class.java.name

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private var config = Config()

    private lateinit var news_list: RecyclerView
    private lateinit var news_adapter: NewsAdapter

    private val compositeDisposable = CompositeDisposable()
    val newsApiRepository = NewsApiRepository("4a5f1af28e4a438b86452e4bded213b0")

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModel.
        HomeViewModelFactory((activity?.applicationContext as CoinApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val top_coin_adapter = TopCoinListAdapter(arrayListOf())
        val top_coin_list = binding.listTopCoin //RecyclerView
        val downloading_top_coin_list: ProgressBar = binding.progressBarTopCoin
        top_coin_list.setHasFixedSize(true)
        top_coin_list.adapter = top_coin_adapter
        top_coin_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)


        //NEWS DASHBOARD
        news_adapter = NewsAdapter()
        news_list = binding.recyclerViewNews //RecyclerView
        news_list.setHasFixedSize(true)
        news_list.setHasFixedSize(true)
        news_list.adapter = news_adapter
        news_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        //showNewsDashboard(news_adapter)

        viewModel.downloading.observe(viewLifecycleOwner){
            Log.d(TAG,"refresh $it")
            if(!it)
                downloading_top_coin_list.visibility = View.VISIBLE
            else
                downloading_top_coin_list.visibility = View.GONE
        }

        viewModel.downloadingError.observe(viewLifecycleOwner){
            if(!it.equals("NO_ERROR")){
                downloading_top_coin_list.visibility = View.GONE
                val toast = Toast.makeText(context, "$it ERROR", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }

        viewModel.topCoins.observe(viewLifecycleOwner){ coin_list ->
            val topCoins = ArrayList<Coin>(10)
            for (i in 0..9) {
                topCoins.add(coin_list.coin[i])
            }
            top_coin_adapter.updateCoinList(topCoins)
        }

        viewModel.allMeteData.observe(viewLifecycleOwner){
            top_coin_adapter.setInfos(it)
        }

      viewModel.news_list.observe(viewLifecycleOwner){
          Log.d(TAG, "onCreateView: ${it.items}")
          news_adapter.setItems(it.items)
      }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //homeViewModel.refreshCoinLatest(config.get_top_coin_limit())
        //viewModel.getLatestNews()

        /*
        newsApiRepository.getTopHeadlines(sources = "fox-news", q = "", pageSize = 20, page = 1)
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .flatMapIterable { articles -> articles.articles }
            .subscribe({
                Log.d(TAG, "getTopHeadlines: ${it.toString()}")
               },
            {
                Log.d(TAG, "getTopHeadlines error ${it.message.toString()}")
            })*/


        newsApiRepository.getEverything(q = "tesla",
                                        sources = "nbc-news",
                                        domains = null,
                                        from =  "2021-10-01",
                                        to = "2021-12-12",
                                        language = Language.EN,
                                        sortBy = SortBy.POPULARITY,
                                        pageSize = 20,
                                        page = 1)
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .flatMapIterable { articles -> articles.articles }
            .subscribe({
                Log.d(TAG, "getEverything  $it ")

                       },
                {
                    Log.d(TAG, "getEverything error: ${it.message}")
                })

        newsApiRepository.getSources(category = Category.GENERAL, language = Language.EN, country = Country.US)
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .flatMapIterable { sources -> sources.sources }
            .subscribe({
                Log.d(TAG, "getSources: ${it.name}  ${it.id} ")
           },
            { t ->
                Log.d("getSources error", t.message.toString())
            })


        newsApiRepository.getTopHeadlines(category = Category.GENERAL, country = Country.US, q = "etherium", pageSize = 20, page = 1)
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .flatMapIterable { articles -> articles.articles }
            .subscribe({

                Log.d(TAG, "getTopHeadlines: ${it.title}")
                       },
                { t -> Log.d("getTopHeadlines error", t.message.toString()) })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }


    /*
    fun showNewsDashboard(adapter:NewsAdapter){

        val newsList = ArrayList<News>()

        val news1 = News("Terra’nın (LUNA) Yükselişi Her Alanda Devam Ediyor",
        "2021’in en çok yükselen coin’lerinden biri olan Terra’nın (LUNA) 20 milyar doların üzerinde TVL’e (kilitlenen toplam varlık değeri) sahip olduğu açıklandı.",
        "https://dappradar.com/blog/static/8b61b82892887129a0b905fed78aff3b/beb58/dappradar.com-image-1.png")

        val news2 = News("Terra’nın (LUNA) Yükselişi Her Alanda Devam Ediyor",
            "2021’in en çok yükselen coin’lerinden biri olan Terra’nın (LUNA) 20 milyar doların üzerinde TVL’e (kilitlenen toplam varlık değeri) sahip olduğu açıklandı.",
            "https://dappradar.com/blog/static/8b61b82892887129a0b905fed78aff3b/beb58/dappradar.com-image-1.png")

        val news3 = News("Terra’nın (LUNA) Yükselişi Her Alanda Devam Ediyor",
            "2021’in en çok yükselen coin’lerinden biri olan Terra’nın (LUNA) 20 milyar doların üzerinde TVL’e (kilitlenen toplam varlık değeri) sahip olduğu açıklandı.",
            "https://dappradar.com/blog/static/8b61b82892887129a0b905fed78aff3b/beb58/dappradar.com-image-1.png")

        newsList.add(news1)
        newsList.add(news2)
        newsList.add(news3)

        adapter.updateNews(newsList)

    }*/
}