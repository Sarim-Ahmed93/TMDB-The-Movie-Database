package com.example.tmdb_themoviedatabase.main.backend.rest

import com.example.tmdb_themoviedatabase.main.backend.data.Detail
import com.example.tmdb_themoviedatabase.main.backend.data.Genre
import com.example.tmdb_themoviedatabase.main.backend.data.GenreData
import com.example.tmdb_themoviedatabase.main.backend.data.MovieList
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GenreApiService {
    @Headers("Domain-Name: base_url") // Add the BaseURL header
    @GET("genre/movie/list")
    fun all(@Query("api_key")  api_key: String): Observable<GenreData>
}