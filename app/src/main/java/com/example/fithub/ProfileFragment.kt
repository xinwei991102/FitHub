package com.example.fithub

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_exercise_complete.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment:Fragment() {
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pref = requireContext().getSharedPreferences("MyPref", 0) // 0 - for private mode
        editor = pref.edit()

        val user = FirebaseAuth.getInstance().currentUser

        buttonLogOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(),LogInActivity::class.java)
            requireContext().startActivity(intent)
        }

        imageButtonEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            requireContext().startActivity(intent)
        }

        buttonSetting.setOnClickListener {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            requireContext().startActivity(intent)
        }

        textViewLevelNum.text = calcLevel().toString()
        textViewPointsNum.text = pref.getInt("total_points",0).toString()

    }

    private fun calcLevel(): Int {
        val points = pref.getInt("total_points",0)
        var level = 1
        when {
            points < 100 -> {
                level = 1
            }
            points in 100..199 -> {
                level = 2
            }
            points in 200..349 -> {
                level = 3
            }
            points in 350..499 -> {
                level = 4
            }
            points in 500..699 -> {
                level = 5
            }
            points in 700..899 -> {
                level = 6
            }
            points in 900..1199 -> {
                level = 7
            }
            points in 1200..1499 -> {
                level = 8
            }
            points in 1500..1999 -> {
                level = 9
            }
            points >= 2000 -> {
                level = 10
            }
        }
        return level
    }


}