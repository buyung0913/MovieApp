package com.example.movieapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.model.Movie
import com.example.movieapp.ui.MovieActivity
import com.example.movieapp.ui.YoutubeActivity
import kotlinx.android.synthetic.main.item_trailer.view.*

class Trailer(
    private val trailer: List<Movie.TrailerResponse.Trailer>,
    private val context: Context
) : RecyclerView.Adapter<Trailer.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var view = view
        private lateinit var trailer: Movie.TrailerResponse.Trailer
        private lateinit var context: Context

        fun bind(trailer: Movie.TrailerResponse.Trailer, context: Context) {
            this.trailer = trailer
            this.context = context

            val trailerUrl = "https://img.youtube.com/vi/${trailer.key}/0.jpg"
            Glide.with(itemView.context)
                .load(trailerUrl)
                .into(itemView.img_trailer)

            itemView.setOnClickListener {
                val intent = Intent(context, YoutubeActivity::class.java)
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("key", trailer.key)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_trailer, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = trailer[position]
        holder.bind(item, context)
    }

    override fun getItemCount() = trailer.size
}