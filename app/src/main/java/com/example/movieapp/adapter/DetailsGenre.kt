package com.example.movieapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.model.Genre
import com.example.movieapp.ui.MovieActivity
import kotlinx.android.synthetic.main.item_details_genre.view.*

class DetailsGenre(
    private val genre: List<Genre.Response.Genre>,
    private val context: Context
) : RecyclerView.Adapter<DetailsGenre.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var view = view
        private lateinit var genre: Genre.Response.Genre
        private lateinit var context: Context

        fun bind(genre: Genre.Response.Genre, context: Context) {
            this.genre = genre
            this.context = context
            view.txt_genre.text = genre.name

            itemView.setOnClickListener {
                val intent = Intent(context, MovieActivity::class.java)
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("id", genre.id)
                intent.putExtra("name", genre.name)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_details_genre, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = genre[position]
        holder.bind(item, context)
    }

    override fun getItemCount() = genre.size
}