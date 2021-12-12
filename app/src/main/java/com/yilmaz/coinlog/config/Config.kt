package com.yilmaz.coinlog.config

@Suppress("FunctionName")
class Config{

    //https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest/limit

    private val BASE_URL = "https://pro-api.coinmarketcap.com"
    private val LIMIT_TO_COIN = "10"
    private val LIMIT_COIN = "100"
    private val API_KEY = "1af60f8f-5154-4831-8b12-31a610d895dd"

    public fun get_url(): String {
        return BASE_URL
    }

    public fun get_top_coin_limit() : String{
        return LIMIT_TO_COIN
    }

    public fun get_coin_limit() : String{
        return LIMIT_COIN
    }

    fun get_header_map(): Map<String, String>{
        val headerMap = mutableMapOf<String, String>()
        headerMap["Accept"] = "application/json"
        headerMap["Content-Type"] = "application/json"
        headerMap["X-CMC_PRO_API_KEY"] = API_KEY
        return  headerMap
    }
}