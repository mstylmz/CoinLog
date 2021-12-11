package com.yilmaz.coinlog.service

import com.yilmaz.coinlog.model.models.info.Info
import com.yilmaz.coinlog.model.models.listing.CoinList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface CmcService {
    @GET("/v1/cryptocurrency/listings/latest?")
    fun getListingLatest(@Query("limit") limit: String, @HeaderMap headers: Map<String, String>): Single<CoinList>

    @GET("/v1/cryptocurrency/info")
    fun getListingInfo(@Query("symbol") symbol: String, @HeaderMap headers: Map<String, String>): Single<Info>

}