package com.example.fithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.activity_exercise.*

class ExerciseActivity : AppCompatActivity(), YouTubePlayer.OnInitializedListener {

    private lateinit var ytplayer:YouTubePlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        // my_child_toolbar is defined in the layout file
        setSupportActionBar(findViewById(R.id.my_child_toolbar))

        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val youTubePlayerFragment =
            supportFragmentManager.findFragmentById(R.id.youtube_fragment) as YouTubePlayerSupportFragment;
        youTubePlayerFragment.initialize("AIzaSyALK-JnsLHtjznQ_1ngrkc7W776f5EHTgM", this)

        buttonStartStopExercise.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!ytplayer.isPlaying)
                    ytplayer.play()
            } else {
                if (ytplayer.isPlaying)
                    ytplayer.pause()
            }
        }
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        if (player == null) return
        ytplayer = player
        if (wasRestored) {
            player.play()
        } else {
            player.cueVideo("oHg5SJYRHA0")
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS)
        }
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        result: YouTubeInitializationResult?
    ) {
        Log.d("Youtube Player", "Failed to initialize")
    }

    //Make up button behave as back button
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return (super.onOptionsItemSelected(item))
    }
}

