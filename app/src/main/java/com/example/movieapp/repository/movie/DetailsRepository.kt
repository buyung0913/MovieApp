package com.example.movieapp.repository.movie

import androidx.lifecycle.LiveData
import com.example.movieapp.api.ApiInterface
import com.example.movieapp.model.Movie
import com.example.movieapp.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class DetailsRepository(
    private val apiInterface: ApiInterface
) {
    lateinit var dataSource: DetailsDataSource

    fun fetchDetailsList(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<Movie.DetailsResponse> {
        dataSource = DetailsDataSource(
            apiInterface,
            compositeDisposable
        )
        dataSource.fetchDetailsList(movieId)

        return dataSource.response
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return dataSource.networkState
    }
}