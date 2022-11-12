package com.example.movieapp.repository.trailer

import androidx.lifecycle.LiveData
import com.example.movieapp.api.ApiInterface
import com.example.movieapp.model.Movie
import com.example.movieapp.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class Repository(
    private val apiInterface: ApiInterface
) {
    lateinit var dataSource: DataSource

    fun fetchTrailerList(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<Movie.TrailerResponse> {
        dataSource = DataSource(
            apiInterface,
            compositeDisposable
        )
        dataSource.fetchTrailerList(movieId)

        return dataSource.response
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return dataSource.networkState
    }
}