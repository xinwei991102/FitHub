package com.example.fithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        buttonProfileSave.setOnClickListener {
            var database = FirebaseDatabase.getInstance().reference

            data class Profile(
                var name: String? = "",
                var gender: String? = "",
                var height: Double = 0.0,
                var weight: Double = 0.0
            )

            fun writeProfile(name: String?, gender: String?, height: Double, weight:Double){
                val profile = Profile(name, gender, height, weight)
                database.child("profile").child("hohk1999@gmail.com").child("name").setValue(name)
            }
        }
    }
}
