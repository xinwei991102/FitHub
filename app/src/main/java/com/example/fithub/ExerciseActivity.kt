package com.example.fithub

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.*
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_exercise.*
import java.util.*

class ExerciseActivity : AppCompatActivity(), OnInitializedListener {

    var context = this
    private lateinit var ytplayer: YouTubePlayer
    private lateinit var mHandler: Handler
    private var startMillis: Int = 0
    private var totalCalories: Double = 0.0
    var weight = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        // my_child_toolbar is defined in the layout file
        setSupportActionBar(findViewById(R.id.my_child_toolbar))

        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra("exercise_name")

        val youTubePlayerFragment =
            supportFragmentManager.findFragmentById(R.id.youtube_fragment) as YouTubePlayerSupportFragment
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
        mHandler = Handler()

        val user = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance().getReference("Profile")
        var profile: Profile?
        weight = 0.0
        database.child(user!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(context, p0.message, Toast.LENGTH_LONG).show()
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    profile = dataSnapshot.getValue(Profile::class.java)
                    if (profile!=null)
                        weight = profile!!.weight
                }
            })
    }

    override fun onInitializationSuccess(
        provider: Provider?,
        player: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        if (player == null) return
        ytplayer = player

        if (wasRestored) {
            player.play()
        } else {
            var vidId = ""
            when (intent.getStringExtra("exercise_name")) {
                getString(R.string.aerobic_exercise) -> {
                    vidId = getString(R.string._10_min_aerobic_vid)
                    startMillis = 17000
                }
                getString(R.string.core_exercise) -> {
                    vidId = getString(R.string._10_min_core_vid)
                    startMillis = 16000
                }
                getString(R.string.arm_exercise) -> {
                    vidId = getString(R.string._10_min_arm_vid)
                    startMillis = 11000
                }
                getString(R.string.leg_exercise) -> {
                    vidId = getString(R.string._10_min_leg_vid)
                    startMillis = 17000
                }
                getString(R.string.testing_exercise) -> {
                    vidId = getString(R.string.placeholder_vid)
                    startMillis = 0
                }
            }
            player.cueVideo(vidId, startMillis)
            player.setPlayerStyle(PlayerStyle.CHROMELESS)
            player.setPlayerStateChangeListener(mPlayerStateChangeListener)
            player.setPlaybackEventListener(mPlaybackEventListener)
            player.fullscreenControlFlags = FULLSCREEN_FLAG_CUSTOM_LAYOUT
        }
    }

    override fun onInitializationFailure(
        provider: Provider?,
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

    private var mPlayerStateChangeListener: PlayerStateChangeListener =
        object : PlayerStateChangeListener {
            override fun onAdStarted() {
            }

            override fun onLoading() {
            }

            override fun onVideoStarted() {
                displayCurrentTime()
                displayCurrentCalories()
            }

            override fun onLoaded(p0: String?) {
                displayCurrentTime()
                displayCurrentCalories()
            }

            override fun onVideoEnded() {
                val intent2 = Intent(context, ExerciseCompleteActivity::class.java)
                intent2.putExtra("exercised_time", ytplayer.durationMillis - startMillis)
                intent2.putExtra("burned_calories", totalCalories.toInt())
                intent2.putExtra("exercise_name",intent.getStringExtra("exercise_name"))
                context.startActivity(intent2)
                context.finish()
            }

            override fun onError(p0: ErrorReason?) {

            }
        }

    private var mPlaybackEventListener: PlaybackEventListener =
        object : PlaybackEventListener {
            override fun onSeekTo(p0: Int) {
                mHandler.post(runnable)
            }

            override fun onBuffering(p0: Boolean) {

            }

            override fun onPlaying() {
                mHandler.post(runnable)
                displayCurrentTime()
                displayCurrentCalories()
            }

            override fun onStopped() {
                mHandler.removeCallbacks(runnable)
            }

            override fun onPaused() {
                mHandler.removeCallbacks(runnable)
            }

        }

    private fun displayCurrentTime() {
        val formattedTime = formatTime(ytplayer.durationMillis - ytplayer.currentTimeMillis)
        textViewTimer.text = formattedTime
    }

    private fun formatTime(millis: Int): String {
        val seconds = millis / 1000
        val minutes = seconds / 60
        return java.lang.String.format(
            Locale.UK,
            "%02d:%02d",
            minutes % 60,
            seconds % 60
        )
    }

    private var runnable: Runnable = object : Runnable {
        override fun run() {
            displayCurrentTime()
            displayCurrentCalories()
            mHandler.post(this)
        }
    }

    private fun displayCurrentCalories() {
        val durationHours = (ytplayer.durationMillis - startMillis) / 1000.0 / 60.0 / 60.0
        totalCalories = (intent.getDoubleExtra("met_score", 1.0) * weight) * durationHours * 1000
        val caloriesPerSec = totalCalories / ((ytplayer.durationMillis - startMillis) / 1000)
        var caloriesNow: Int =
            (caloriesPerSec * ((ytplayer.currentTimeMillis - startMillis) / 1000)).toInt()
        val calStr = java.lang.String.format(
            Locale.UK,
            "%02d cal",
            caloriesNow
        )
        textViewCaloriesBurntCounter.text = calStr
    }
}

