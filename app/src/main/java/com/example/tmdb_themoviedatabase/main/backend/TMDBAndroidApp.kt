package com.example.tmdb_themoviedatabase.main.backend

import android.app.Application
import android.content.Context
import com.example.tmdb_themoviedatabase.main.common.Constants

class TMDBAndroidApp : Application() {
    private var mContext: Context? = null

    @Override
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)


    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        Constants.CONTEXT = applicationContext

        setBackend()

    }


    companion object
    {
        @get:Synchronized var instance: TMDBAndroidApp? = null
            private set

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    fun setBackend(/* backendType */) {
        AppModel.backendModel.value = BackendModel()
    }
}