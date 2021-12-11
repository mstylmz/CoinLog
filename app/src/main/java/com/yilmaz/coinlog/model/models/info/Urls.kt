package com.yilmaz.coinlog.model.models.info

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Urls(
                @SerializedName("website")
                @Expose
                val website: List<String>,
                @SerializedName("technical_doc")
                @Expose
                val technical_doc: List<String>,
                @SerializedName("twitter")
                @Expose
                val twitter:List<String>,
                @SerializedName("reddit")
                @Expose
                val reddit: List<String>,
                @SerializedName("message_board")
                @Expose
                val messageBoard: List<String>,
                @SerializedName("announcement")
                @Expose
                val  announcement :List<Object>,
                @SerializedName("chat")
                @Expose
                val chat: List<String>,
                @SerializedName("explorer")
                @Expose
                val explorer: List<String>,
                @SerializedName("source_code")
                @Expose
                val source_code: List<String>,
                )
{
}