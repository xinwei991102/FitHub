package com.example.fithub

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_exercise_complete.*
import java.util.*

class ExerciseCompleteActivity : AppCompatActivity() {

    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_complete)
        // my_child_toolbar is defined in the layout file
        setSupportActionBar(findViewById(R.id.my_child_toolbar))
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imageViewTrophy.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.wobble_anim))
        }
        setTextViews()

        val points: Int = (intent.getIntExtra("burned_calories", 0) / 1000)
        textViewPointsEarned.text = "$points points"

        val user = FirebaseAuth.getInstance().currentUser
        pref = applicationContext.getSharedPreferences(user?.uid, 0) // 0 - for private mode
        editor = pref.edit()

        val prevPoints = pref.getInt("total_points", 0)
        val newTotalPoints = prevPoints + points
        editor.putInt("total_points", newTotalPoints)
        editor.apply()
        writeProfile()
        val currentUId = FirebaseAuth.getInstance().currentUser?.uid
        val eventData = intent.getStringExtra("exercise_name")
        var evColor = 0
        //set date into 12mn in term milliseconds
        val c = GregorianCalendar()
        c.set(Calendar.HOUR_OF_DAY, 0) //anything 0 - 23
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        val currentTimeInLong = c.time.time
        when (eventData) {
            getString(R.string.aerobic_exercise) -> {
                evColor = 0
            }
            getString(R.string.core_exercise) -> {
                evColor = 1
            }
            getString(R.string.arm_exercise) -> {
                evColor = 2
            }
            getString(R.string.leg_exercise) -> {
                evColor = 3
            }
            getString(R.string.testing_exercise) -> {
                evColor = 0
            }
        }
        if (currentUId != null) {
            logRecord(currentUId, evColor, currentTimeInLong, eventData)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return (super.onOptionsItemSelected(item))
    }

    private fun writeProfile() {
        val database = FirebaseDatabase.getInstance().getReference("Profile")

        //get and store points
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val pref = this.getSharedPreferences(uid, 0) // 0 - for private mode
        val points = pref.getInt("total_points", 0)

        val childUpdates = HashMap<String, Any>()
        childUpdates["/$uid/points"] = points

        database.updateChildren(childUpdates)
    }

    private fun setTextViews() {
        val exercisedTime = intent.getIntExtra("exercised_time", 0)
        val burnedCalories = intent.getIntExtra("burned_calories", 0)
        val exercisedSec = exercisedTime / 1000
        val exercisedMin = exercisedSec / 60
        val timeStr = java.lang.String.format(
            Locale.UK,
            "%d m %d s",
            exercisedMin % 60,
            exercisedSec % 60
        )
        val calStr = java.lang.String.format(
            Locale.UK,
            "%d calories",
            burnedCalories
        )
        textViewExercisedTime.text = timeStr
        textViewCaloriesCount.text = calStr
    }

    override fun onResume() {
        super.onResume()
        val animation = AnimationUtils.loadAnimation(this, R.anim.wobble_anim)
        imageViewTrophy.startAnimation(animation)
    }

    private fun logRecord(uid: String, evColor: Int, time: Long, data: String) {
        val recordDB = FirebaseDatabase.getInstance()
        val recordRef = recordDB.getReference("ExerciseRecord")
        val exerciseRecord = ExerciseRecord(uid, evColor, time, data)
        recordRef.push().setValue(exerciseRecord)
    }

}
