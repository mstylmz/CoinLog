package com.yilmaz.coinlog.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yilmaz.coinlog.model.models.info.Info
import com.yilmaz.coinlog.model.models.listing.CoinList
import com.yilmaz.coinlog.repository.CoinRepository

class DashboardViewModel : ViewModel() {

    private val TAG = DashboardViewModel::class.java.name

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private var coinList: MutableLiveData<CoinList>? = null
    private var topCoinList: MutableLiveData<CoinList>? = null
    private var refresh:MutableLiveData<Boolean>? = null
    private var refresh_error:MutableLiveData<String>? = null
    private var coinsMetaData: MutableLiveData<Info>? = null

    private var mRepo =  CoinRepository()

    fun init() {
        if(coinList != null){
            return
        }
        Log.d(TAG, "init")
        mRepo = CoinRepository().getInstance()
        coinList = mRepo.getCoinLatest()
        refresh = mRepo.getRefreshStatus()
        refresh_error = mRepo.getRefreshError()
        coinsMetaData = mRepo.getCoinsMetaData()
    }

    fun refreshCoinLatest(limit:String){
        mRepo.getCoinLatestFromCmc(limit)
    }

    fun getCoinLatest(): MutableLiveData<CoinList>? {
        Log.d(TAG, "getCoinLatest")
        return coinList
    }

    fun getRefreshStatus() : MutableLiveData<Boolean>? {
        return refresh
    }

    fun getRefreshError() : MutableLiveData<String>?{
        return refresh_error
    }

    fun getCoinsMetaData(): MutableLiveData<Info>? {
        return coinsMetaData
    }

}