package com.example.movieapp.model

class Genre {
    data class Response(
        val genres: List<Genre>
    ) {
        data class Genre(
            val id: Int,
            val name: String
        )
    }
}