package com.example.movieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.Genre
import com.example.movieapp.repository.NetworkState
import com.example.movieapp.repository.genre.Repository
import io.reactivex.disposables.CompositeDisposable

class Genre(
    private val repository: Repository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val genreList: LiveData<Genre.Response> by lazy {
        repository.fetchGenreList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        repository.getNetworkState()
    }

    fun isEmptyList(): Boolean {
        return genreList.value?.genres?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}