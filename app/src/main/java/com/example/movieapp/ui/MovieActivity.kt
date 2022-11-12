package com.example.movieapp.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.R
import com.example.movieapp.api.ApiClient
import com.example.movieapp.repository.NetworkState
import com.example.movieapp.repository.movie.Repository
import com.example.movieapp.viewmodel.Movie
import kotlinx.android.synthetic.main.activity_movie.*

class MovieActivity : AppCompatActivity() {
    private lateinit var viewModel: Movie
    lateinit var repository: Repository
    private lateinit var adapter: com.example.movieapp.adapter.Movie

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val toolbar = findViewById<View>(R.id.toolbar)
        val txt_title = toolbar.findViewById<TextView>(R.id.txt_title)
        val txt_subtitle = toolbar.findViewById<TextView>(R.id.txt_subtitle)
        val btn_toolbar = toolbar.findViewById<ImageButton>(R.id.btn_toolbar)
        txt_title.text = "${intent.getStringExtra("name")} Movies"
        txt_subtitle.text = "Click one of the movies below for details"
        btn_toolbar.setOnClickListener {
            super.onBackPressed()
        }

        val apiClient = ApiClient.getClient()
        repository = Repository(apiClient)
        viewModel = getViewModel(intent.getIntExtra("id", 0))
        adapter = com.example.movieapp.adapter.Movie(this)

        val layoutManager = GridLayoutManager(this, 3)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == adapter.MOVIE_VIEW_TYPE) 1
                else 3
            }
        }

        viewModel.movieList.observe(this, Observer {
            adapter.submitList(it)
            rv_movie.layoutManager = layoutManager
            rv_movie.setHasFixedSize(true)
            rv_movie.adapter = adapter
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility =
                if (viewModel.isEmptyList() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if (viewModel.isEmptyList() && it == NetworkState.ERROR) {
                Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_LONG).show()
            }

            if (!viewModel.isEmptyList()) {
                adapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(genreId: Int): Movie {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return Movie(repository, genreId) as T
            }
        })[Movie::class.java]
    }
}