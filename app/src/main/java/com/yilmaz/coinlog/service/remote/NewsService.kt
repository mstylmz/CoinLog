package com.yilmaz.coinlog.service.remote

import me.toptas.rssconverter.RssFeed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface NewsService {
    @GET
    fun getNews(@Url url: String): Call<RssFeed>
}