package com.yilmaz.coinlog.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yilmaz.coinlog.R

@BindingAdapter("profileImage")
fun ImageView.downloadFromUrl(url: String?){

    val option = RequestOptions()
        .placeholder(placeHolderProgressBar(context))
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(option)
        .load(url)
        .into(this)
}

fun placeHolderProgressBar(context: Context) : CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 20f
        start()
    }
}

@SuppressLint("ResourceAsColor")
@BindingAdapter("profileChangeTextColor")
fun setColor(textView: TextView, volume24: Double) {

    Log.d("profileChangeTextColor:",  "$volume24")
    if(volume24 < 0){
        Log.d("profileChangeTextColor:",  "change color")
        textView.setTextColor(Color.parseColor("#DD2C00"))
    }
}

