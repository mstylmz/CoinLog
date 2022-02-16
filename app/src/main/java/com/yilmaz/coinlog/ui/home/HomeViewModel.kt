package com.yilmaz.coinlog.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yilmaz.coinlog.model.models.info.Info
import com.yilmaz.coinlog.model.models.listing.CoinList
import com.yilmaz.coinlog.repository.remote.Repository
import me.toptas.rssconverter.RssFeed

class HomeViewModel(private val repository: Repository) : ViewModel() {
    private val TAG = HomeViewModel::class.java.name

    fun getLatestNews() = repository.getLatestNews()

    val topCoins: MutableLiveData<CoinList> = repository.allCoins

    val allMeteData: MutableLiveData<Info> = repository.allMetaData

    val downloading: MutableLiveData<Boolean> = repository.downloading

    val downloadingError: MutableLiveData<String> = repository.downloadingError

    val news_list: MutableLiveData<RssFeed> = repository.news_list

    class HomeViewModelFactory(private val repository: Repository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}