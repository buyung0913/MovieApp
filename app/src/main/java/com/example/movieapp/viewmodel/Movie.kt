package com.example.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.movieapp.model.Movie
import com.example.movieapp.repository.NetworkState
import com.example.movieapp.repository.movie.Repository
import io.reactivex.disposables.CompositeDisposable

class Movie(
    private val repository: Repository,
    private val genreId: Int
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val movieList: LiveData<PagedList<Movie.Response.Movie>> by lazy {
        repository.fetchMovieList(compositeDisposable, genreId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        repository.getNetworkState()
    }

    fun isEmptyList(): Boolean {
        return movieList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}