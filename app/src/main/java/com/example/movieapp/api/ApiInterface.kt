package com.example.movieapp.api

import com.example.movieapp.model.Genre
import com.example.movieapp.model.Movie
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("genre/movie/list")
    fun getGenreList(): Single<Genre.Response>

    @GET("discover/movie")
    fun getMovieList(
        @Query("with_genres") id: Int,
        @Query("page") page: Int
    ): Single<Movie.Response>

    @GET("movie/{movie_id}")
    fun getMovieDetailsList(
        @Path("movie_id") id: Int
    ): Single<Movie.DetailsResponse>

    @GET("movie/{movie_id}/casts")
    fun getMovieCastList(
        @Path("movie_id") id: Int
    ): Single<Movie.CastResponse>

    @GET("movie/{movie_id}/videos")
    fun getMovieTrailerList(
        @Path("movie_id") id: Int
    ): Single<Movie.TrailerResponse>
}