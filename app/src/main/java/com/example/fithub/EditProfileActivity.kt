package com.example.fithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        buttonProfileSave.setOnClickListener {
            var database = FirebaseDatabase.getInstance().getReference("profile")

            data class Profile(
                var gender: String? = "",
                var height: Double = 0.0,
                var name: String? = "",
                var weight: Double = 0.0
            )

            fun writeProfile(gender: String?, height: Double, name: String?, weight:Double){
                val profile = Profile(gender, height, name,  weight)
                val key = database.child("hohk1999@gmail.com").push().key
                database.child("hohk1999@gmail.com").setValue(profile)
            }

            writeProfile(editTextGender.text.toString(), editTextHeight.text.toString().toDouble(), editTextName.text.toString(), editTextWeight.text.toString().toDouble())
        }
    }
}
