package com.example.tmdb_themoviedatabase.main.backend.rest

import com.example.tmdb_themoviedatabase.main.backend.data.MovieItem
import com.example.tmdb_themoviedatabase.main.backend.data.MovieList
import com.example.tmdb_themoviedatabase.main.common.Constants
import com.example.tmdb_themoviedatabase.main.common.Keys
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers

interface MovieListApiService {
    @Headers("Domain-Name: base_url") // Add the BaseURL header
    @GET("list/5?api_key=5775c8ec0af689c38b0e198bb6e7c96a")
    fun getList(): Observable<MovieList>
}