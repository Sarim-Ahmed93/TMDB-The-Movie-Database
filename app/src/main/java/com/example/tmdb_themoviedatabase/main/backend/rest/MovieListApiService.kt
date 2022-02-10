package com.example.tmdb_themoviedatabase.main.backend.rest

import com.example.tmdb_themoviedatabase.main.backend.data.MovieItem
import com.example.tmdb_themoviedatabase.main.backend.data.MovieList
import com.example.tmdb_themoviedatabase.main.common.Constants
import com.example.tmdb_themoviedatabase.main.common.Keys
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MovieListApiService {
    @Headers("Domain-Name: base_url") // Add the BaseURL header
    @GET("movie/now_playing")
    fun getList(@Query("page")  page: Int, @Query("api_key")  api_key: String): Observable<MovieList>
}