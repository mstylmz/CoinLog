package com.yilmaz.coinlog.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application): AndroidViewModel(application), CoroutineScope  {

    private val job = Job() // background job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main //back to main thread after finish job

    //if closed the app
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}