package com.yilmaz.coinlog.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yilmaz.coinlog.model.models.info.Info
import com.yilmaz.coinlog.model.models.listing.Coin
import com.yilmaz.coinlog.repository.CoinRepository

class HomeViewModel : ViewModel() {


    private val TAG = HomeViewModel::class.java.name

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private var topCoinList: MutableLiveData<ArrayList<Coin>>? = null
    private var refresh:MutableLiveData<Boolean>? = null
    private var mRepo = CoinRepository()
    private var coinsMetaData: MutableLiveData<Info>? = null


    fun init()
    {
        if(topCoinList != null)
        {
            return
        }
        mRepo = CoinRepository().getInstance()
        topCoinList = mRepo.getTopCoinList()
        refresh = mRepo.getRefreshStatus()
        coinsMetaData = mRepo.getCoinsMetaData()
    }

    fun refreshCoinLatest(limit:String){
        mRepo.getCoinLatestFromCmc(limit)
    }

    fun getRefreshStatus() : MutableLiveData<Boolean>? {
        return refresh
    }

    fun getTopCoinList(): MutableLiveData<ArrayList<Coin>>? {
        Log.d(TAG, "getTopCoinList")
        return  topCoinList
    }

    fun getCoinsMetaData(): MutableLiveData<Info>? {
        return coinsMetaData
    }
}