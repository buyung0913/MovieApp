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
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.adapter.DetailsGenre
import com.example.movieapp.api.ApiClient
import com.example.movieapp.api.IMAGE_BASE_URL
import com.example.movieapp.api.IMAGE_BASE_URL_500
import com.example.movieapp.model.Movie
import com.example.movieapp.repository.NetworkState
import com.example.movieapp.repository.cast.Repository
import com.example.movieapp.repository.movie.DetailsRepository
import com.example.movieapp.viewmodel.Cast
import com.example.movieapp.viewmodel.MovieDetails
import com.example.movieapp.viewmodel.Trailer
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.activity_movie_details.progress_bar
import kotlinx.android.synthetic.main.activity_movie_details.rv_genre
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var movieViewModel: MovieDetails
    lateinit var movieRepository: DetailsRepository

    private lateinit var castViewModel: Cast
    lateinit var castRepository: Repository
    private lateinit var castAdapter: com.example.movieapp.adapter.Cast

    private lateinit var trailerViewModel: Trailer
    lateinit var trailerRepository: com.example.movieapp.repository.trailer.Repository
    private lateinit var trailerAdapter: com.example.movieapp.adapter.Trailer
    private lateinit var genreAdapter: DetailsGenre

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val toolbar = findViewById<View>(R.id.toolbar)
        val txt_title = toolbar.findViewById<TextView>(R.id.txt_title)
        val txt_subtitle = toolbar.findViewById<TextView>(R.id.txt_subtitle)
        val btn_toolbar = toolbar.findViewById<ImageButton>(R.id.btn_toolbar)
        txt_title.text = "Details Movies"
        txt_subtitle.visibility = View.GONE
        btn_toolbar.setOnClickListener {
            super.onBackPressed()
        }

        val apiClient = ApiClient.getClient()
        movieRepository = DetailsRepository(apiClient)
        movieViewModel = getMovieViewModel(intent.getIntExtra("id", 0))
        castRepository = com.example.movieapp.repository.cast.Repository(apiClient)
        castViewModel = getCastViewModel(intent.getIntExtra("id", 0))
        trailerRepository = com.example.movieapp.repository.trailer.Repository(apiClient)
        trailerViewModel = getTrailerViewModel(intent.getIntExtra("id", 0))

        movieViewModel.movieList.observe(this, Observer {
            populatingView(it)
        })

        movieViewModel.networkState.observe(this, Observer {
            progress_bar.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if (it == NetworkState.ERROR) {
                Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_LONG).show()
            }
        })

        castViewModel.castList.observe(this, Observer {
            if (it.cast.isNotEmpty()) {
                txt_cast_title.text = "Cast"

                castAdapter = com.example.movieapp.adapter.Cast(it.cast, this)
                val linearLayoutManager = LinearLayoutManager(this)
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                rv_cast.layoutManager = linearLayoutManager
                rv_cast.setHasFixedSize(true)
                rv_cast.adapter = castAdapter
            }
        })

        castViewModel.networkState.observe(this, Observer {
            txt_cast_title.visibility =
                if (castViewModel.isEmptyList() && it == NetworkState.ERROR) View.GONE else View.VISIBLE
            rv_cast.visibility =
                if (castViewModel.isEmptyList() && it == NetworkState.ERROR) View.GONE else View.VISIBLE
        })

        trailerViewModel.trailerList.observe(this, Observer {
            if (it.results.isNotEmpty()) {
                txt_trailer_title.text = "Trailers & clips"
                txt_trailer_subtitle.text = "Click the video to watch the trailer"

                trailerAdapter = com.example.movieapp.adapter.Trailer(it.results, this)
                val linearLayoutManager = LinearLayoutManager(this)
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                rv_trailer.layoutManager = linearLayoutManager
                rv_trailer.setHasFixedSize(true)
                rv_trailer.adapter = trailerAdapter
            }
        })

        trailerViewModel.networkState.observe(this, Observer {
            txt_trailer_title.visibility =
                if (trailerViewModel.isEmptyList() && it == NetworkState.ERROR) View.GONE else View.VISIBLE
            txt_trailer_subtitle.visibility =
                if (trailerViewModel.isEmptyList() && it == NetworkState.ERROR) View.GONE else View.VISIBLE
            rv_trailer.visibility =
                if (trailerViewModel.isEmptyList() && it == NetworkState.ERROR) View.GONE else View.VISIBLE
        })
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun populatingView(it: Movie.DetailsResponse) {
        txt_movie_title.text = it.title
        txt_status.text = it.status
        txt_overview_title.text = "Synopsis"
        txt_overview.text = it.overview

        // Formating date
        if (it.releaseDate.isNotEmpty()) {
            val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH)
            val targetFormat: DateFormat = SimpleDateFormat("MMM dd, yyyy")
            val date: Date = originalFormat.parse(it.releaseDate) as Date
            val formattedDate: String = targetFormat.format(date)
            txt_releaseDate.text = "$formattedDate    ● ${it.runtime} minute(s)"
        }

        if (it.genres.isNotEmpty()) {
            txt_genre_title.text = "Genres"
            txt_genre_subtitle.text =
                "Click one of the genres below to get a list of movies by genre"

            genreAdapter = DetailsGenre(
                it.genres,
                this
            )
            val linearLayoutManager = LinearLayoutManager(this)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            rv_genre.layoutManager = linearLayoutManager
            rv_genre.setHasFixedSize(true)
            rv_genre.adapter = genreAdapter
        }

        container_backdrop.visibility = View.VISIBLE
        img_backdrop.visibility = View.VISIBLE
        Glide.with(this)
            .load(
                // Combine String v2
                "${IMAGE_BASE_URL}${it.backdropPath}"
            ).into(img_backdrop)

        container_poster.visibility = View.VISIBLE
        Glide.with(this)
            .load(
                // Combine String v3
                StringBuilder().append(IMAGE_BASE_URL_500)
                    .append(it.posterPath)
                    .toString()
            ).into(img_poster)
    }

    private fun getMovieViewModel(movieId: Int): MovieDetails {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieDetails(movieRepository, movieId) as T
            }
        })[MovieDetails::class.java]
    }

    private fun getCastViewModel(movieId: Int): Cast {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return Cast(castRepository, movieId) as T
            }
        })[Cast::class.java]
    }

    private fun getTrailerViewModel(movieId: Int): Trailer {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return Trailer(trailerRepository, movieId) as T
            }
        })[Trailer::class.java]
    }
}