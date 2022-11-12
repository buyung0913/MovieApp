package com.example.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.Movie
import com.example.movieapp.repository.NetworkState
import com.example.movieapp.repository.trailer.Repository
import io.reactivex.disposables.CompositeDisposable

class Trailer(
    private val repository: Repository,
    movieId: Int
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val trailerList: LiveData<Movie.TrailerResponse> by lazy {
        repository.fetchTrailerList(compositeDisposable, movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        repository.getNetworkState()
    }

    fun isEmptyList(): Boolean {
        return trailerList.value?.results?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}