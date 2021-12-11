package com.yilmaz.coinlog.service

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.yilmaz.coinlog.config.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CmcApiClient {

    private var config = Config()
    private  var retrofit: Retrofit? = null

    init {

    }

    /*
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build();
     */

    fun getClient() : Retrofit{
        if(retrofit == null){
                retrofit = Retrofit
                    .Builder()
                    .baseUrl(config.get_url())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit as Retrofit
    }
}