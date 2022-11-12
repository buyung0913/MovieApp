package com.example.movieapp.repository.movie

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.movieapp.api.ApiInterface
import com.example.movieapp.model.Movie
import io.reactivex.disposables.CompositeDisposable

class DataSourceFactory(
    private val apiInterface: ApiInterface,
    private val compositeDisposable: CompositeDisposable,
    private val genreId: Int
) : DataSource.Factory<Int, Movie.Response.Movie>() {
    val liveDataSource = MutableLiveData<com.example.movieapp.repository.movie.DataSource>()

    override fun create(): DataSource<Int, Movie.Response.Movie> {
        val dataSource =
            DataSource(
                apiInterface,
                compositeDisposable,
                genreId
            )
        liveDataSource.postValue(dataSource)
        return dataSource
    }
}