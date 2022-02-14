package com.example.tmdb_themoviedatabase.main.backend.data

data class GenreData(var genres : List<Genre>)

data class Genre(var id : Int = 0,var name: String? = "")
