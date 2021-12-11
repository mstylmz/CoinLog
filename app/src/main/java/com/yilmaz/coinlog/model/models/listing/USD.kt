package com.yilmaz.coinlog.model.models.listing

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class USD(
                @SerializedName("price")
                @Expose
                var price: Double,
                @SerializedName("volume_24h")
                @Expose
                var volume24h: Double,
                @SerializedName("percent_change_1h")
                @Expose
                var percentChange1h: Double,
                @SerializedName("percent_change_24h")
                @Expose
                var percentChange24h:Double,
                @SerializedName("percent_change_7d")
                @Expose
                var percentChange7d:Double,
                @SerializedName("market_cap")
                @Expose
                var marketCap:Double,
                @SerializedName("last_updated")
                @Expose
                var lastUpdated:String
            ) {
}