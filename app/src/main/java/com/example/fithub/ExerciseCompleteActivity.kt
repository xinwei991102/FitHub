package com.example.fithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_exercise_complete.*
import java.util.*

class ExerciseCompleteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_complete)
        // my_child_toolbar is defined in the layout file
        setSupportActionBar(findViewById(R.id.my_child_toolbar))
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        imageViewTrophy.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this,R.anim.wobble_anim))
        }
        setTextViews()
        //TODO calculate points
    }

    private fun setTextViews(){
        val exercisedTime =intent.getIntExtra("exercised_time",0)
        val burnedCalories = intent.getDoubleExtra("burned_calories", 0.0)
        val exercisedSec = exercisedTime/1000
        val exercisedMin = exercisedSec/60
        val timeStr = java.lang.String.format(
            Locale.UK,
            "%02d m %02d s",
            exercisedMin % 60,
            exercisedSec % 60
        )
        val calStr = java.lang.String.format(
            Locale.UK,
            "%.02f calories",
            burnedCalories
        )
        textViewExercisedTime.text = timeStr
        textViewCaloriesCount.text = calStr
    }

    override fun onResume() {
        super.onResume()
        val animation = AnimationUtils.loadAnimation( this, R.anim.wobble_anim)
        imageViewTrophy.startAnimation(animation)
    }

}
