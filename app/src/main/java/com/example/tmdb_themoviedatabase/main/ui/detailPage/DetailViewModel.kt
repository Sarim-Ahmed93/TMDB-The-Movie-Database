package com.example.tmdb_themoviedatabase.main.ui.detailPage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.tmdb_themoviedatabase.main.backend.AppModel
import com.example.tmdb_themoviedatabase.main.backend.data.Cast
import com.example.tmdb_themoviedatabase.main.backend.data.Genre
import com.example.tmdb_themoviedatabase.main.backend.data.MovieItem
import com.example.tmdb_themoviedatabase.main.common.Constants

class DetailViewModel : ViewModel() {
    private var TAG = "DetailViewModel"
    val movies: LiveData<List<MovieItem>>
        get() = _movies
    val genres: LiveData<List<Genre>>
        get() = _genres
    val cast: LiveData<List<Cast>>
        get() = _cast
    val castReady: LiveData<Boolean>
        get() = _castReady

    private var _movies = MutableLiveData(listOf<MovieItem>())
    private var _genres = MutableLiveData(listOf<Genre>())
    private var _cast = MutableLiveData(listOf<Cast>())
    private val _castReady = MutableLiveData(false)

    init {
        AppModel.backendModel.value?.let { backend ->
            backend.movies.observe(ProcessLifecycleOwner.get()) { allMovies ->
                _movies.value = allMovies
            }
            backend.genres.observe(ProcessLifecycleOwner.get()) {  genres ->
                _genres.value = genres
            }
            backend.cast.observe(ProcessLifecycleOwner.get()) { cast ->
                _cast.value = cast
            }
        }

    }

    fun getMovieDetail(id : Int) : MovieItem
    {
      return  _movies.value?.filter { it.id == id }!!.single()
    }

    fun getCast(id : Int)
    {
        AppModel.backendModel.value!!.fetchCast(id) { castList, successful ->
            if (successful) {
                castList.cast.forEach { it->
                    it.profile_path = Constants.profileImagePath + it.profile_path
                }
                _cast.value = castList.cast
                _castReady.value = true
                Log.d(TAG, "final cast list : ${_cast.value.toString()}")
            }
            else {
            }
        }
    }

    fun getGenres(movie: MovieItem) : ArrayList<String>
    {
        Log.d(TAG, "in function : ${movie.genre_ids}")

        var movieGenres = ArrayList<String>()
        movie.genre_ids?.forEach { it->

            AppModel.backendModel.value!!.genres.value?.forEach { genre ->
                if(it == genre.id) {
                    Log.d(TAG, "genre name : ${genre.name}")
                    movieGenres.add(genre.name!!)}
            }
        }
        return movieGenres
    }
}