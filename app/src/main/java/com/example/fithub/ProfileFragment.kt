package com.example.fithub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlin.math.pow


class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        buttonLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LogInActivity::class.java)
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

        val database = FirebaseDatabase.getInstance().getReference("Profile")
        var profile: Profile

        database.child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    profile = dataSnapshot.getValue(Profile::class.java)!!
                    textViewProfileName.text = profile.name.toString()
                    textViewHeight_cm.text = profile.height.toString()
                    textViewWeight_kg.text = profile.weight.toString()
                    textView1UserGender.text = profile.gender.toString()

                    var heightCm = profile.height
                    var weightKg = profile.weight
                    var bmi: Double
                    var heightM = (heightCm/100.0)
                    var heightSquare = heightM.pow(2)

                    bmi = (weightKg / heightSquare)

                    textViewBMI_count.text = String.format("%.1f", bmi)
                }

            })

    }

    data class Profile(
        var gender: String? = "",
        var height: Double = 0.0,
        var name: String? = "",
        var weight: Double = 0.0
    )


}


