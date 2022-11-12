package com.example.movieapp.repository.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movieapp.api.ApiInterface
import com.example.movieapp.api.POST_PER_PAGE
import com.example.movieapp.model.Movie
import com.example.movieapp.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class Repository(
    private val apiInterface: ApiInterface
) {
    lateinit var pagedList: LiveData<PagedList<Movie.Response.Movie>>
    lateinit var factory: DataSourceFactory

    fun fetchMovieList(
        compositeDisposable: CompositeDisposable,
        genreId: Int
    ): LiveData<PagedList<Movie.Response.Movie>> {
        factory = DataSourceFactory(apiInterface, compositeDisposable, genreId)
        val config = PagedList.Config
            .Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        pagedList = LivePagedListBuilder(factory, config)
            .build()

        return pagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<DataSource, NetworkState>(
            factory.liveDataSource, DataSource::networkState
        )
    }
}