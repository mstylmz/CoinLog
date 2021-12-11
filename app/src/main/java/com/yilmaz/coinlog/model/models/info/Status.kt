package com.yilmaz.coinlog.model.models.info

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("timestamp") @Expose
    private var timestamp: String,
    @SerializedName("error_code")
    @Expose
    private val errorCode: Int,
    @SerializedName("error_message")
    @Expose
    private val errorMessage: String,
    @SerializedName("elapsed")
    @Expose
    private val elapsed: Int,
    @SerializedName("credit_count")
    @Expose
    private val creditCount: Int) {
}