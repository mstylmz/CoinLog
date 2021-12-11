package com.yilmaz.coinlog.model.models.listing

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CoinList(
    @SerializedName("data")
                    @Expose
                    var coin: List<Coin>,
    @SerializedName("status")
                    @Expose
                    var status: Status
                    )
{}