package com.example.fithub

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlin.math.pow

class ProfileFragment:Fragment() {
    private lateinit var pref: SharedPreferences
    private lateinit var thisContext:Context
    private lateinit var textViewProfileName: TextView
    private lateinit var textViewHeightCm:TextView
    private lateinit var textViewWeightKg:TextView
    private lateinit var textViewUserGender:TextView
    private lateinit var imageViewProfilePic:ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        //TODO getViewByID
        textViewProfileName.findViewById<TextView>(R.id.textViewProfileName)
        textViewHeightCm.findViewById<TextView>(R.id.textViewHeight)
        textViewWeightKg.findViewById<TextView>(R.id.textViewWeight)
        imageViewProfilePic.findViewById<ImageView>(R.id.imageViewProfilePic)
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        thisContext = requireContext()

        val user = FirebaseAuth.getInstance().currentUser
        pref = requireContext().getSharedPreferences(user?.uid, 0) // 0 - for private mode

        buttonLogOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
        }

        imageButtonEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            requireContext().startActivity(intent)
        }

        buttonChangePassword.setOnClickListener {
            val intent = Intent(requireContext(),  ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        val database = FirebaseDatabase.getInstance().getReference("Profile")
        var profile: Profile
        database.child(user!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(thisContext,p0.message,Toast.LENGTH_LONG)
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    profile = dataSnapshot.getValue(Profile::class.java)!!
                    textViewProfileName.text = profile.name
                    textViewHeightCm.text = profile.height.toString()
                    textViewWeightKg.text = profile.weight.toString()
                    textViewUserGender.text = profile.gender
                    //textViewPointsNum.text = profile.points.toString()
                    Picasso.get().load(profile.downloadUrl).placeholder(R.drawable.ic_child_face).into(imageViewProfilePic)

                    val heightCm = profile.height
                    val weightKg = profile.weight
                    val bmi: Double
                    val heightM = (heightCm/100.0)
                    val heightSquare = heightM.pow(2)

                    bmi = (weightKg / heightSquare)
                    textViewBMI_count.text = String.format("%.1f", bmi)
                }
            })
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