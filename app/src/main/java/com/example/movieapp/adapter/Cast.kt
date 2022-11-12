package com.example.movieapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.api.IMAGE_BASE_URL_185
import com.example.movieapp.model.Movie
import kotlinx.android.synthetic.main.item_cast.view.*

class Cast(
    private val cast: List<Movie.CastResponse.Cast>,
    private val context: Context
) : RecyclerView.Adapter<Cast.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var view = view
        private lateinit var cast: Movie.CastResponse.Cast
        private lateinit var context: Context

        fun bind(cast: Movie.CastResponse.Cast, context: Context) {
            this.cast = cast
            this.context = context
            view.txt_cast.text = cast.name
            
            Glide.with(itemView.context)
                .load("${IMAGE_BASE_URL_185}${cast.profilePath}")
                .into(itemView.img_cast)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cast, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cast[position]
        holder.bind(item, context)
    }

    override fun getItemCount() = cast.size
}