package com.example.tmdb_themoviedatabase.main.backend.rest

import com.example.tmdb_themoviedatabase.main.backend.data.Cast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CastApiService {
    @Headers("Domain-Name: base_url") // Add the BaseURL header
    @GET("movie/movie_id/credits")
    fun getCast(@Query("movie_id")  movie_id: Int, @Query("api_key")  api_key: String): Call<List<Cast>>
}