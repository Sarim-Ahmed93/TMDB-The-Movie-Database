package com.example.tmdb_themoviedatabase.main.ui.detailPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.tmdb_themoviedatabase.main.backend.AppModel
import com.example.tmdb_themoviedatabase.main.backend.data.MovieItem

class DetailViewModel : ViewModel() {
    val movies: LiveData<List<MovieItem>>
        get() = _movies
    private var _movies = MutableLiveData(listOf<MovieItem>())


    init {
        AppModel.backendModel.value?.let { backend ->
            backend.movies.observe(ProcessLifecycleOwner.get()) { allMovies ->
                _movies.value = allMovies
            }

        }
    }

    fun getMovieDetail(id : Int) : MovieItem
    {
      return  _movies.value?.filter { it.id == id }!!.single()
    }



}