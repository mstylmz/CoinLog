package com.yilmaz.coinlog.model.models.news
import com.google.gson.annotations.SerializedName


data class Feed (
    @SerializedName("url"         ) var url         : String? = null,
    @SerializedName("title"       ) var title       : String? = null,
    @SerializedName("link"        ) var link        : String? = null,
    @SerializedName("author"      ) var author      : String? = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("image"       ) var image       : String? = null
)