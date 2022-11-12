package com.example.movieapp.repository.trailer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.api.ApiInterface
import com.example.movieapp.model.Movie
import com.example.movieapp.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DataSource(
    private val apiInterface: ApiInterface,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _response = MutableLiveData<Movie.TrailerResponse>()
    val response: LiveData<Movie.TrailerResponse>
        get() = _response

    fun fetchTrailerList(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            apiInterface.getMovieTrailerList(movieId)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(
                    {
                        _response.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    }, {
                        _networkState.postValue(NetworkState.ERROR)
                        it.message?.let { it -> Log.e("Trailer data source", it) }
                    }
                )?.let {
                    compositeDisposable.add(
                        it
                    )
                }
        } catch (e: Exception) {
            e.message?.let { Log.e("Trailer data source", it) }
        }
    }
}