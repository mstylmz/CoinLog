package com.yilmaz.coinlog.model.models.news
import com.google.gson.annotations.SerializedName

data class News (
    @SerializedName("status" ) var status : String?          = null,
    @SerializedName("feed"   ) var feed   : Feed?            = Feed(),
    @SerializedName("items"  ) var items  : ArrayList<Items> = arrayListOf()
)

