package com.example.movieapp.repository.movie

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.movieapp.api.ApiInterface
import com.example.movieapp.api.FIRST_PAGE
import com.example.movieapp.model.Movie
import com.example.movieapp.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DataSource(
    private val apiInterface: ApiInterface,
    private val compositeDisposable: CompositeDisposable,
    private val genreId: Int
) : PageKeyedDataSource<Int, Movie.Response.Movie>() {
    var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Movie.Response.Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiInterface.getMovieList(genreId, params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.totalPages >= params.key) {
                            callback.onResult(it.results, params.key + 1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    }, {
                        networkState.postValue(NetworkState.ERROR)
                        it.message?.let { it -> Log.e("Movie data source", it) }
                    }
                )
        )
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Movie.Response.Movie>
    ) {

    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie.Response.Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiInterface.getMovieList(genreId, page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.results, null, page + 1)
                        networkState.postValue(NetworkState.LOADED)
                    }, {
                        networkState.postValue(NetworkState.ERROR)
                        it.message?.let { it -> Log.e("Movie data source", it) }
                    }
                )
        )
    }
}