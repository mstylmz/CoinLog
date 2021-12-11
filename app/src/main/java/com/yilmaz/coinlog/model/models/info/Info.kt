package com.yilmaz.coinlog.model.models.info

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("data")
    @Expose
    val metaData: Map<String, CoinInfo>,
    @SerializedName("status")
    @Expose
    val status: Status
) {
}