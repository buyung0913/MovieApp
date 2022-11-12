package com.example.movieapp.model

import com.google.gson.annotations.SerializedName

class Movie {
    data class Response(
        val page: Int,
        val results: List<Movie>,
        @SerializedName("total_pages")
        val totalPages: Int,
        @SerializedName("total_results")
        val totalResults: Int,
    ) {
        data class Movie(
            val id: Int,
            val title: String,
            @SerializedName("poster_path")
            val posterPath: String,
            @SerializedName("vote_average")
            val voteAverage: Double
        )
    }

    data class DetailsResponse(
        val id: Int,
        val title: String,
        val overview: String,
        val genres: List<Genre.Response.Genre>,
        @SerializedName("release_date")
        val releaseDate: String,
        @SerializedName("vote_average")
        val voteAverage: Double,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("backdrop_path")
        val backdropPath: String,
        val runtime: Int,
        val status: String
    )

    data class CastResponse(
        val id: Int,
        val cast: List<Cast>
    ) {
        data class Cast(
            val id: Int,
            val name: String,
            @SerializedName("profile_path")
            val profilePath: String
        )
    }

    data class TrailerResponse(
        val id: Int,
        val results: List<Trailer>
    ) {
        data class Trailer(
            val id: String,
            val key: String,
            val name: String
        )
    }
}
