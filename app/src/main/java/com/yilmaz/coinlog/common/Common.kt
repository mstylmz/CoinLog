package com.yilmaz.coinlog.common

import android.content.Context
import android.widget.Toast

class Common {
    fun showToast(contex:Context,message:String, duration: Int){
        Toast.makeText(contex, message, duration).show()
    }
}