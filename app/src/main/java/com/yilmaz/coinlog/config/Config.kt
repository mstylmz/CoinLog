package com.yilmaz.coinlog.config

@Suppress("FunctionName")
class Config{

    //https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest/limit

    private val BASE_URL = "https://pro-api.coinmarketcap.com"
    private val LIMIT_COIN = "100"
    private val API_KEY = "1af60f8f-5154-4831-8b12-31a610d895dd"

    private val NEWS_URL = "https://kriptokoin.com/feed"

    //"https://www.koinbulteni.com/feed"

    fun get_cmc_url(): String {
        return BASE_URL
    }

    fun get_coin_limit() : String{
        return LIMIT_COIN
    }

    fun get_header_map(): Map<String, String>{
        val headerMap = mutableMapOf<String, String>()
        headerMap["Accept"] = "application/json"
        headerMap["Content-Type"] = "application/json"
        headerMap["X-CMC_PRO_API_KEY"] = API_KEY
        return  headerMap
    }

    fun get_news_url(): String{
        return  NEWS_URL
    }
}