package com.yilmaz.coinlog

import android.app.Application
import com.yilmaz.coinlog.repository.remote.Repository
import com.yilmaz.coinlog.service.local.FavoriteCoinDatabase
import com.yilmaz.coinlog.service.remote.CmcApiClient
import com.yilmaz.coinlog.service.remote.NewsApiClient
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

//Creates a separate Hilt component for each Android class.
@HiltAndroidApp
class CoinApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy {
        FavoriteCoinDatabase.getDatabase(this, applicationScope)
    }

    val cmcApiClicent by lazy {
        CmcApiClient.getCmcClient()
    }

    val newsApiClient by lazy {
        NewsApiClient.getNewsClient()
    }

    val repository by lazy {
        Repository(cmcApiClicent, database.coinDao(), newsApiClient)
    }
}