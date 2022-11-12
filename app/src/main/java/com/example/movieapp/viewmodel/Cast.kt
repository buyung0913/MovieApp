package com.example.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.Movie
import com.example.movieapp.repository.NetworkState
import com.example.movieapp.repository.cast.Repository
import io.reactivex.disposables.CompositeDisposable

class Cast(
    private val repository: Repository,
    movieId: Int
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val castList: LiveData<Movie.CastResponse> by lazy {
        repository.fetchCastList(compositeDisposable, movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        repository.getNetworkState()
    }

    fun isEmptyList(): Boolean {
        return castList.value?.cast?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}