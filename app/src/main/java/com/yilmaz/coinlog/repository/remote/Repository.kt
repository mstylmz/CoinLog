package com.yilmaz.coinlog.repository.remote

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.yilmaz.coinlog.config.Config
import com.yilmaz.coinlog.model.models.data_base.FavoriteCoin
import com.yilmaz.coinlog.model.models.info.Info
import com.yilmaz.coinlog.model.models.listing.Coin
import com.yilmaz.coinlog.model.models.listing.CoinList
import com.yilmaz.coinlog.service.local.FavoriteCoinsDao
import com.yilmaz.coinlog.service.remote.CmcService
import com.yilmaz.coinlog.service.remote.NewsService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import me.toptas.rssconverter.RssFeed
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class Repository(private val cmcApiClient: Retrofit,
                 private val favoriteCoinsDao: FavoriteCoinsDao,
                 private val newsApiClient: Retrofit) {
    private val TAG = Repository::class.java.name

    private var config = Config()

    private val disposable = CompositeDisposable()

    private var dataCoinList =  MutableLiveData<CoinList>()
    private var dataCoinMetaData = MutableLiveData<Info>()

    private var dataTopCoinList =  MutableLiveData<ArrayList<Coin>>()

    private var dataDownloading = MutableLiveData<Boolean>()
    private var dataDownloadingError = MutableLiveData<String>()
    
    //News variable
    
    private var dataNewsList = MutableLiveData<RssFeed>()


    fun getCoinLatestFromCmc(limit:String){
        val service = cmcApiClient.create(CmcService::class.java)
        disposable.add( service
            .getListingLatest(limit, config.get_header_map())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object  : DisposableSingleObserver<CoinList>(){

                override fun onStart() {
                    super.onStart()
                    dataDownloading.value = false
                    dataDownloadingError.value = "NO_ERROR"
                }

                override fun onSuccess(t: CoinList) {
                    dataCoinList.value = t
                    dataTopCoinList.value = t.coin as ArrayList
                    dataDownloading.value = true
                    getCoinMetaDataFromCmc(t)
                }
                override fun onError(e: Throwable) {
                    dataDownloadingError.value = e.localizedMessage
                }
            })
        )
    }

    fun getCoinMetaDataFromCmc(t: CoinList){
        val service = cmcApiClient.create(CmcService::class.java)
        val comma = ","
        val cryptoList = ArrayList<Coin>()
        cryptoList.addAll(t.coin)
        val csvBuilder = StringBuilder()
        for(item in cryptoList){
            csvBuilder.append(item.symbol)
            csvBuilder.append(comma)
        }
        var csv = csvBuilder.toString()
        csv = csv.substring(0, csv.length - comma.length) //Remove last comma
        disposable.add(service
            .getListingInfo(csv, config.get_header_map())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Info>(){
                override fun onSuccess(t: Info) {
                    dataCoinMetaData.value = t
                }
                override fun onError(e: Throwable) {
                    Log.d(TAG, "getCoinLogos error")

                }
            })
        )
    }


    fun getLatestNews(){
        Log.d(TAG, "getLatestNews:")

        val service = newsApiClient.create(NewsService::class.java)
        service.getNews(config.get_news_url())
            .enqueue(object : Callback<RssFeed> {
                override fun onResponse(call: Call<RssFeed>, response: Response<RssFeed>) {
                    if(response.isSuccessful){
                        var items = response.body()!!.items
                        Log.d(TAG, "onResponse: $items")
                        dataNewsList.value = response.body()!!
                    }
                }
                override fun onFailure(call: Call<RssFeed>, t: Throwable) {
                    // Show failure message
                    Log.d(TAG, "onFailure: $t")
                }
            })
    }

    val news_list:MutableLiveData<RssFeed> = dataNewsList

    val allCoins: MutableLiveData<CoinList> = dataCoinList

    val allMetaData: MutableLiveData<Info> = dataCoinMetaData

    val downloading : MutableLiveData<Boolean> = dataDownloading

    val downloadingError: MutableLiveData<String> = dataDownloadingError

    //DATABASE FUNCTIONS
    val allFavoriteCoin: Flow<List<FavoriteCoin>> = favoriteCoinsDao.getAllCoins()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(coin: FavoriteCoin){
        favoriteCoinsDao.insert(coin)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(coin: FavoriteCoin){
        favoriteCoinsDao.delete(coin)
    }
}

























