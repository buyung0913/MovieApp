package com.example.movieapp.ui

import android.os.Bundle
import android.widget.Toast
import com.example.movieapp.R
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_youtube.*

class YoutubeActivity : YouTubeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

        img_youtube.initialize("AIzaSyBcD9SOuLZFoMY40rpVDKBudUaROalArg4",
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubePlayer?,
                    p2: Boolean
                ) {
                    p1?.loadVideo(intent.getStringExtra("key"))
                    p1?.setFullscreen(true)
                    p1?.play()
                }

                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?
                ) {
                    Toast.makeText(
                        this@YoutubeActivity,
                        "Unable to play the video",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}