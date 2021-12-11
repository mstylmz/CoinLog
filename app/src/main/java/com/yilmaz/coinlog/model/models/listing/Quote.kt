package com.yilmaz.coinlog.model.models.listing

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Quote(
                @SerializedName("USD")
                @Expose
                var usd: USD
                ) {
}