package com.yilmaz.coinlog.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.yilmaz.coinlog.config.Config
import com.yilmaz.coinlog.model.models.info.Info
import com.yilmaz.coinlog.model.models.listing.Coin
import com.yilmaz.coinlog.model.models.listing.CoinList
import com.yilmaz.coinlog.service.CmcApiClient
import com.yilmaz.coinlog.service.CmcService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class CoinRepository {
    private val TAG = CoinRepository::class.java.name

    private var instance: CoinRepository ? = null
    private var config = Config()

    private val disposable = CompositeDisposable()
    private var client = CmcApiClient()

    private var dataCoinList =  MutableLiveData<CoinList>()
    private var dataCoinMetaData = MutableLiveData<Info>()

    private var coins = ArrayList<Coin>()
    private var dataTopCoinList =  MutableLiveData<ArrayList<Coin>>()

    private var refresh_status = MutableLiveData<Boolean>()
    private var refresh_error = MutableLiveData<String>()


    fun getInstance() : CoinRepository{
        if(instance == null){
            Log.d(TAG, "get instance")
            instance = CoinRepository()
        }
        return instance!!
    }

    fun getCoinLatestFromCmc(limit:String){
        val service = client.getClient().create(CmcService::class.java)
        disposable.add( service
            .getListingLatest(limit, config.get_header_map())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object  : DisposableSingleObserver<CoinList>(){

                override fun onStart() {
                    super.onStart()
                    refresh_status.value = false
                    refresh_error.value = "NO_ERROR"
                }

                override fun onSuccess(t: CoinList) {
                    dataCoinList.value = t
                    dataTopCoinList.value = t.coin as ArrayList
                    refresh_status.value = true
                    getCoinMetaDataFromCmc(t)
                }
                override fun onError(e: Throwable) {
                    refresh_error.value = e.localizedMessage
                }
            })
        )
    }

    fun getCoinMetaDataFromCmc(t: CoinList){
        val service = client.getClient().create(CmcService::class.java)
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

    fun getCoinLatest(): MutableLiveData<CoinList>{
        Log.d(TAG, "getCoinLatest")
        return dataCoinList
    }

    fun getCoinsMetaData(): MutableLiveData<Info>{
        return dataCoinMetaData
    }

    fun getTopCoinList(): MutableLiveData<ArrayList<Coin>>{
        return dataTopCoinList
    }

    fun getRefreshStatus(): MutableLiveData<Boolean>{
        return refresh_status
    }

    fun getRefreshError(): MutableLiveData<String>{
        return refresh_error
    }
}

























