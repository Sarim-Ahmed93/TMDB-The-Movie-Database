package com.example.tmdb_themoviedatabase.main.ui.homePage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.tmdb_themoviedatabase.main.backend.AppModel
import com.example.tmdb_themoviedatabase.main.backend.data.MovieItem
import com.example.tmdb_themoviedatabase.main.backend.data.MovieList

class HomeViewModel : ViewModel() {
    val movies: LiveData<List<MovieItem>>
        get() = _movies
    val dataReady: LiveData<Boolean>
        get() = _dataReady
    private var _movies = MutableLiveData(listOf<MovieItem>())
    private val _dataReady = MutableLiveData(false)

    init {
        AppModel.backendModel.value?.let { backend ->
            backend.movies.observe(ProcessLifecycleOwner.get()) { allMovies ->
                _movies.value = allMovies
                _dataReady.value = true
            }

        }
    }

}