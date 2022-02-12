package com.example.tmdb_themoviedatabase.main.backend.data

data class Cast(var gender : Int?,var id: Int?, var known_for_department: String?,
var name: String?, var original_name : String?, var popularity: Double = 0.0,
var profile_path: String?, var cast_id : Int = 0, var character : String,
var credit_id: String?, var order: Int = 0)
