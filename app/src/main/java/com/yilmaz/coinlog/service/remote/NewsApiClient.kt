package com.yilmaz.coinlog.service.remote

import me.toptas.rssconverter.RssConverterFactory
import retrofit2.Retrofit

class NewsApiClient {

    companion object{

        private  var retrofit: Retrofit? = null

        fun getNewsClient() : Retrofit {
            if(retrofit == null){
                retrofit = Retrofit
                    .Builder()
                    .baseUrl("https://github.com")
                    .addConverterFactory(RssConverterFactory.create())
                    .build()
            }
            return retrofit as Retrofit
        }

    }
}