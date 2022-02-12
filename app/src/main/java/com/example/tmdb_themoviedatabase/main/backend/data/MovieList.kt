package com.example.tmdb_themoviedatabase.main.backend.data

import java.lang.StringBuilder

data class MovieList(var results : List<MovieItem> )
{
    constructor(otherMovieList : MovieList) : this
    (
        results = otherMovieList.results
    )
}

data class MovieItem( var id : Int?, var title : String?, var original_language: String?,
var original_title : String?, var overview: String?, var popularity : Double,
var poster_path : String?, var release_date: String?, var video: String?,
var vote_average : Double?, var vote_count: Int?, var adult : Boolean = false,
var backdrop_path : String?)

