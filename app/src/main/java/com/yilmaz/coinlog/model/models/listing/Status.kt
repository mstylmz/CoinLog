package com.yilmaz.coinlog.model.models.listing

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Status(
                    @SerializedName("timestamp")
                    @Expose
                    var timestamp:String,
                    @SerializedName("error_code")
                    @Expose
                    var errorCode:Integer,
                    @SerializedName("error_message")
                    @Expose
                    var errorMessage:Object,
                    @SerializedName("elapsed")
                    @Expose
                    var elapsed:Integer,
                    @SerializedName("credit_count")
                    @Expose
                    var creditCount:Integer,
) {
}