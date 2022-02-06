package com.example.tmdb_themoviedatabase.main.backend

import androidx.lifecycle.MutableLiveData

object AppModel {
    var backendModel = MutableLiveData<BackendModel?>(null)

    enum class AppError {
        NONE, NETWORK_ERROR
    }

    init {

    }
}