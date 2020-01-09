package com.example.fithub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

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
