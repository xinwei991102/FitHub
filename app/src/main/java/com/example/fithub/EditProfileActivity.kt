package com.example.fithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    lateinit var name: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        name = findViewById<EditText>(R.id.editTextName)

        buttonProfileSave.setOnClickListener {

            writeProfile()
        }
    }

    private fun writeProfile() {

        val database = FirebaseDatabase.getInstance().getReference("Profile")
        val name = name.text.toString()

        val profile = Profile(name)
        database.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(profile)
            .addOnCompleteListener {
                Toast.makeText(
                    applicationContext,
                    "Profile details edit successfuly",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}

data class Profile(
    //var gender: String? = "",
    //var height: Double = 0.0,
    var name: String? = ""
    //var weight: Double = 0.0
)
