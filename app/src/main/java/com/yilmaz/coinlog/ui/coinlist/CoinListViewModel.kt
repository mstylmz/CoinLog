package com.yilmaz.coinlog.ui.coinlist

import androidx.lifecycle.*
import com.yilmaz.coinlog.model.models.data_base.FavoriteCoin
import com.yilmaz.coinlog.model.models.info.Info
import com.yilmaz.coinlog.model.models.listing.CoinList
import com.yilmaz.coinlog.repository.remote.Repository
import kotlinx.coroutines.launch

class CoinListViewModel(private val repository: Repository): ViewModel(){
    private val TAG = CoinListViewModel::class.java.name

    /*
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
    */

    var liveDataMerger = MediatorLiveData<List<FavoriteCoin>>()

    fun refreshCoinLatest(limit:String) = repository.getCoinLatestFromCmc(limit)

    val allCoins: MutableLiveData<CoinList> = repository.allCoins

    val allMeteData: MutableLiveData<Info> = repository.allMetaData

    val downloading: MutableLiveData<Boolean> = repository.downloading

    val downloadingError: MutableLiveData<String> = repository.downloadingError

    //DATABASE FUNCTIONS
    val allFavoriteCoin: LiveData<List<FavoriteCoin>> = repository.allFavoriteCoin.asLiveData()

    fun insert(coin: FavoriteCoin) = viewModelScope.launch{
        repository.insert(coin)
    }

    fun delete(coin: FavoriteCoin) = viewModelScope.launch{
        repository.delete(coin)
    }

        //FACTORY
    class DashboardViewModelFactory(private val repository: Repository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CoinListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CoinListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}