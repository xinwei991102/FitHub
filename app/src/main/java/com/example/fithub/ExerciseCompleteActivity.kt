package com.example.fithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_exercise_complete.*

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
    }

    override fun onResume() {
        super.onResume()
        var animation = AnimationUtils.loadAnimation( this, R.anim.wobble_anim)
        imageViewTrophy.startAnimation(animation)
    }

}
