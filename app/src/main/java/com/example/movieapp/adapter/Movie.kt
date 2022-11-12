package com.example.movieapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.api.IMAGE_BASE_URL_185
import com.example.movieapp.model.Movie
import com.example.movieapp.repository.NetworkState
import com.example.movieapp.ui.MovieDetailsActivity
import kotlinx.android.synthetic.main.item_movie.view.*
import kotlinx.android.synthetic.main.item_network_state.view.*

class Movie(
    private val context: Context
) : PagedListAdapter<Movie.Response.Movie, RecyclerView.ViewHolder>(DiffCallback()) {
    val MOVIE_VIEW_TYPE = 1
    private val NETWORK_VIEW_TYPE = 2
    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            getItem(position)?.let { (holder as MovieViewHolder).bind(it, context) }
        } else if (getItemViewType(position) == NETWORK_VIEW_TYPE) {
            networkState?.let { (holder as NetworkViewHolder).bind(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View
        return if (viewType == MOVIE_VIEW_TYPE) {
            view = inflater.inflate(R.layout.item_movie, parent, false)
            MovieViewHolder(view)
        } else {
            view = inflater.inflate(R.layout.item_network_state, parent, false)
            NetworkViewHolder(view)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Movie.Response.Movie>() {
        override fun areItemsTheSame(
            oldItem: Movie.Response.Movie,
            newItem: Movie.Response.Movie
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Movie.Response.Movie,
            newItem: Movie.Response.Movie
        ): Boolean {
            return oldItem == newItem
        }
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: Movie.Response.Movie, context: Context) {
            itemView.txt_title.text = movie.title
            itemView.txt_rating.text = movie.voteAverage.toString()

            Glide.with(itemView.context)
                // Combine String v1
                .load(IMAGE_BASE_URL_185 + movie.posterPath)
                .into(itemView.img_poster)

            itemView.setOnClickListener {
                val intent = Intent(context, MovieDetailsActivity::class.java)
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("id", movie.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(networkState: NetworkState) {
            if (networkState == NetworkState.LOADING) {
                itemView.progress_bar.visibility = View.VISIBLE
            } else {
                itemView.progress_bar.visibility = View.GONE
            }

            when (networkState) {
                NetworkState.ERROR -> {
                    itemView.txt_state.visibility = View.VISIBLE
                    itemView.txt_state.text = networkState.msg
                }
                NetworkState.ENDOFLIST -> {
                    itemView.txt_state.visibility = View.VISIBLE
                    itemView.txt_state.text = networkState.msg
                }
                else -> {
                    itemView.txt_state.visibility = View.GONE
                }
            }
        }
    }

    fun setNetworkState(networkState: NetworkState) {
        val previousState: NetworkState? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState = networkState
        val hasExtraRow: Boolean = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != networkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}