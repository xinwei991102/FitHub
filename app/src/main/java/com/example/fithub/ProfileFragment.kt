package com.example.fithub

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
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

class ProfileFragment : Fragment() {
    private lateinit var pref: SharedPreferences
    private lateinit var thisContext: Context
    private lateinit var textViewProfileName: TextView
    private lateinit var textViewHeightCm: TextView
    private lateinit var textViewWeightKg: TextView
    private lateinit var textViewUserGender: TextView
    private lateinit var imageViewProfilePic: ImageView
    private lateinit var textViewBMICount: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        textViewProfileName = view.findViewById(R.id.textViewProfileName)
        textViewHeightCm = view.findViewById(R.id.textViewHeight_cm)
        textViewWeightKg = view.findViewById(R.id.textViewWeight_kg)
        textViewUserGender = view.findViewById(R.id.textViewUserGender)
        imageViewProfilePic = view.findViewById(R.id.imageViewProfilePic)
        textViewBMICount = view.findViewById(R.id.textViewBMI_count)
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        thisContext = requireContext()

        val user = FirebaseAuth.getInstance().currentUser
        pref = requireContext().getSharedPreferences(user?.uid, 0) // 0 - for private mode

        buttonLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LogInActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        imageButtonEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            requireContext().startActivity(intent)
        }

        buttonChangePassword.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }
        val database = FirebaseDatabase.getInstance().getReference("Profile")
        var profile: Profile
        database.child(user!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(thisContext, p0.message, Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    profile = dataSnapshot.getValue(Profile::class.java)!!
                    textViewProfileName.text = profile.name
                    textViewHeightCm.text = profile.height.toString()
                    textViewWeightKg.text = profile.weight.toString()
                    textViewUserGender.text = profile.gender
                    if (profile.downloadUrl == "") {
                        Toast.makeText(
                            requireContext(),
                            "Unable to retrieve profile picture",
                            Toast.LENGTH_SHORT
                        ).show()
                        val res = resources.getDrawable(R.drawable.ic_child_face)
                        imageViewProfilePic.setImageDrawable(res)
                    } else {
                        Picasso.get().load(profile.downloadUrl)
                            .placeholder(R.drawable.ic_child_face).into(imageViewProfilePic)
                    }

                    val heightCm = profile.height
                    val weightKg = profile.weight
                    val bmi: Double
                    val heightM = (heightCm / 100.0)
                    val heightSquare = heightM.pow(2)
                    bmi = (weightKg / heightSquare)

                    //show BMI classification
                    when {
                        bmi < 16.0 -> {
                            textViewBMICount.text =
                                String.format("%.1f (%s)", bmi, getString(R.string.severe_thinness))
                            textViewBMICount.setTextColor(resources.getColor(R.color.red))
                        }
                        bmi >= 16.0 && bmi < 17.0 -> {
                            textViewBMICount.text =
                                String.format("%.1f (%s)", bmi, getString(R.string.moderate_thinness))
                            textViewBMICount.setTextColor(resources.getColor(R.color.amber))
                        }
                        bmi >= 17.0 && bmi < 18.5 -> {
                            textViewBMICount.text =
                                String.format("%.1f (%s)", bmi, getString(R.string.mild_thinness))
                            textViewBMICount.setTextColor(resources.getColor(R.color.amber))
                        }
                        bmi >= 18.5 && bmi < 25.0 -> {
                            textViewBMICount.text =
                                String.format("%.1f (%s)", bmi, getString(R.string.normal))
                            textViewBMICount.setTextColor(resources.getColor(R.color.green))
                        }
                        bmi >= 25.0 && bmi < 30.0 -> {
                            textViewBMICount.text =
                                String.format("%.1f (%s)", bmi, getString(R.string.overweight))
                            textViewBMICount.setTextColor(resources.getColor(R.color.amber))
                        }
                        bmi >= 30.0 && bmi < 35.0 -> {
                            textViewBMICount.text =
                                String.format("%.1f (%s)", bmi, getString(R.string.obese_class_i))
                            textViewBMICount.setTextColor(resources.getColor(R.color.red))
                        }
                        bmi in 35.0..40.0 -> {
                            textViewBMICount.text =
                                String.format("%.1f (%s)", bmi, getString(R.string.obese_class_ii))
                            textViewBMICount.setTextColor(resources.getColor(R.color.red))
                        }
                        bmi > 40.0 -> {
                            textViewBMICount.text =
                                String.format("%.1f (%s)", bmi, getString(R.string.obese_class_iii))
                            textViewBMICount.setTextColor(resources.getColor(R.color.red))
                        }
                    }

                }
            })
        textViewLevelNum.text = calcLevel().toString()
        textViewPointsNum.text = pref.getInt("total_points", 0).toString()
    }

    private fun calcLevel(): Int {
        val points = pref.getInt("total_points", 0)
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