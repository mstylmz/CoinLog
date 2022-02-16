package com.yilmaz.coinlog.model.models.listing

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Coin(
                @SerializedName("id")
                @Expose
                var id: Int,
                @SerializedName("name")
                @Expose
                var name: String,
                @SerializedName("symbol")
                @Expose
                var symbol: String,
                @SerializedName("slug")
                @Expose
                var slug:String,
                @SerializedName("cmc_rank")
                @Expose
                var cmc_rank:Int,
                @SerializedName("num_market_pairs")
                @Expose
                var num_market_pairs:Int,
                @SerializedName("circulating_supply")
                @Expose
                var circulating_supply:Double,
                @SerializedName("total_supply")
                @Expose
                var total_supply:Double,
                @SerializedName("max_supply")
                @Expose
                var max_supply:Double,
                @SerializedName("last_updated")
                @Expose
                var last_updated:String,
                @SerializedName("date_added")
                @Expose
                var date_added:String,
                @SerializedName("quote")
                @Expose
                var quote: Quote
                ) {

}