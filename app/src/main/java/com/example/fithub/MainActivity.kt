package com.example.fithub

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        } else {
            //TODO get user points from database
        }

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

}
