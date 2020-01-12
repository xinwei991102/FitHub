package com.example.fithub

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navListener =
            BottomNavigationView.OnNavigationItemSelectedListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.nav_home -> {
                        val fragment = HomeFragment()
                        supportFragmentManager.beginTransaction().replace(
                            R.id.fragment_container,
                            fragment,
                            fragment.javaClass.simpleName
                        )
                            .commit()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.nav_exercise -> {
                        val fragment = ExerciseFragment()
                        supportFragmentManager.beginTransaction().replace(
                            R.id.fragment_container,
                            fragment,
                            fragment.javaClass.simpleName
                        )
                            .commit()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.nav_profile -> {
                        val fragment = ProfileFragment()
                        supportFragmentManager.beginTransaction().replace(
                            R.id.fragment_container,
                            fragment,
                            fragment.javaClass.simpleName
                        )
                            .commit()
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }
        bottom_navigation.setOnNavigationItemSelectedListener(navListener)

        if (savedInstanceState == null) {
            val fragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, fragment.javaClass.simpleName)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (FirebaseAuth.getInstance().currentUser != null)
            writeProfile()
    }

    private fun writeProfile() {
        val context = this
        val database = FirebaseDatabase.getInstance().getReference("Profile")
        var profile: Profile
        val user = FirebaseAuth.getInstance().currentUser
        database.child(user!!.uid).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext,p0.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                profile = dataSnapshot.getValue(Profile::class.java)!!
                //get and store points
                val pref = context.getSharedPreferences(user?.uid, 0) // 0 - for private mode
                val points = pref.getInt("total_points", 0)
                profile.points = points

                database.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(profile)
                    .addOnCompleteListener {
                        Toast.makeText(
                            applicationContext,
                            "Points saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        })


    }

}
