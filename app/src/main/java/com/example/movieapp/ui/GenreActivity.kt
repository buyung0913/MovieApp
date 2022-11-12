package com.example.movieapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.api.ApiClient
import com.example.movieapp.repository.NetworkState
import com.example.movieapp.repository.genre.Repository
import com.example.movieapp.viewmodel.Genre
import kotlinx.android.synthetic.main.activity_genre.*
import kotlinx.android.synthetic.main.activity_genre.progress_bar

class GenreActivity : AppCompatActivity() {
    private lateinit var viewModel: Genre
    lateinit var repository: Repository
    private lateinit var adapter: com.example.movieapp.adapter.Genre

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre)

        val toolbar = findViewById<View>(R.id.toolbar)
        val txt_title = toolbar.findViewById<TextView>(R.id.txt_title)
        val txt_subtitle = toolbar.findViewById<TextView>(R.id.txt_subtitle)
        val btn_toolbar = toolbar.findViewById<ImageButton>(R.id.btn_toolbar)
        btn_toolbar.visibility = View.GONE
        txt_title.text = "Genre"
        txt_subtitle.text = "Click on a genre to get a list of movies"

        val apiClient = ApiClient.getClient()
        repository = Repository(apiClient)
        viewModel = getViewModel()

        viewModel.genreList.observe(this, Observer {
            adapter = com.example.movieapp.adapter.Genre(it.genres, this)
            val linearLayoutManager = LinearLayoutManager(this)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            rv_genre.layoutManager = linearLayoutManager
            rv_genre.setHasFixedSize(true)
            rv_genre.adapter = adapter
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility =
                if (viewModel.isEmptyList() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if (viewModel.isEmptyList() && it == NetworkState.ERROR) {
                Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getViewModel(): Genre {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return Genre(repository) as T
            }
        })[Genre::class.java]
    }
}