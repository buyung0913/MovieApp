package com.example.movieapp.repository.cast

import androidx.lifecycle.LiveData
import com.example.movieapp.api.ApiInterface
import com.example.movieapp.model.Movie
import com.example.movieapp.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class Repository(
    private val apiInterface: ApiInterface
) {
    lateinit var dataSource: DataSource

    fun fetchCastList(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<Movie.CastResponse> {
        dataSource = DataSource(
            apiInterface,
            compositeDisposable
        )
        dataSource.fetchCastList(movieId)

        return dataSource.response
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return dataSource.networkState
    }
}