package com.yilmaz.coinlog

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//Creates a separate Hilt component for each Android class.
@HiltAndroidApp
class CoinApplication: Application() {
}