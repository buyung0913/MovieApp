package com.example.movieapp.repository.genre

import androidx.lifecycle.LiveData
import com.example.movieapp.api.ApiInterface
import com.example.movieapp.model.Genre
import com.example.movieapp.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class Repository(
    private val apiInterface: ApiInterface
) {
    lateinit var dataSource: DataSource

    fun fetchGenreList(
        compositeDisposable: CompositeDisposable
    ): LiveData<Genre.Response> {
        dataSource = DataSource(
            apiInterface,
            compositeDisposable
        )
        dataSource.fetchGenreList()

        return dataSource.response
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return dataSource.networkState
    }
}