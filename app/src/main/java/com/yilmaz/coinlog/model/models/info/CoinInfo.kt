package com.yilmaz.coinlog.model.models.info

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CoinInfo(
                @SerializedName("urls")
                @Expose
                val usls: Urls,
                @SerializedName("logo")
                @Expose
                val logo: String,
                @SerializedName("id")
                @Expose
                val id: Int,
                @SerializedName("name")
                @Expose
                val name: String,
                @SerializedName("symbol")
                @Expose
                val symbol: String,
                @SerializedName("slug")
                @Expose
                val slug: String,
                @SerializedName("description")
                @Expose
                val description: String,
                @SerializedName("tags")
                @Expose
                val tags: List<String>,
                @SerializedName("category")
                @Expose
                val category: String) {
}