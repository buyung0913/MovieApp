package com.example.movieapp.repository.movie

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.api.ApiInterface
import com.example.movieapp.model.Movie
import com.example.movieapp.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailsDataSource(
    private val apiInterface: ApiInterface,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _response = MutableLiveData<Movie.DetailsResponse>()
    val response: LiveData<Movie.DetailsResponse>
        get() = _response

    @SuppressLint("LongLogTag")
    fun fetchDetailsList(
        movieId: Int
    ) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiInterface.getMovieDetailsList(movieId)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _response.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    }, {
                        _networkState.postValue(NetworkState.ERROR)
                        it.message?.let { it -> Log.e("Movie details data source", it) }
                    }
                )?.let {
                    compositeDisposable.add(
                        it
                    )
                }
        } catch (e: Exception) {
            e.message?.let { Log.e("Movie details data source", it) }
        }
    }
}