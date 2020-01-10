package com.example.fithub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.view.*
import android.widget.ArrayAdapter
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class EditProfileActivity : AppCompatActivity() {

    lateinit var name: EditText
    lateinit var gender: Spinner
    lateinit var height: EditText
    lateinit var weight: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val genderArr = arrayOf("Male", "Female")
        spinnerEditGender.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderArr)

        spinnerEditGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        name = findViewById(R.id.editTextName)
        gender = findViewById(R.id.spinnerEditGender)
        height = findViewById(R.id.editTextHeight)
        weight = findViewById(R.id.editTextWeight)

        buttonProfileSave.setOnClickListener {
            writeProfile()
        }

        buttonProfileCancel.setOnClickListener {
            finish()
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
                    editTextName.setText(profile.name.toString())
                    editTextHeight.setText(profile.height.toString())
                    editTextWeight.setText(profile.weight.toString())

                    val genderDb = profile.gender.toString()
                    var genderSelect: Int? = 0
                    genderSelect = if (genderDb == "Male") {
                        0
                    } else {
                        1
                    }
                    spinnerEditGender.setSelection(genderSelect)
                }

            })

    }

    private fun writeProfile() {

        val database = FirebaseDatabase.getInstance().getReference("Profile")
        val name = name.text.toString()
        val gender = gender.spinnerEditGender.selectedItem.toString()
        val height = height.text.toString().toDouble()
        val weight = weight.text.toString().toDouble()

        val profile = Profile(gender, height, name, weight)
        database.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(profile)
            .addOnCompleteListener {
                Toast.makeText(
                    applicationContext,
                    "Profile details edit successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}

data class Profile(
    var gender: String? = "",
    var height: Double = 0.0,
    var name: String? = "",
    var weight: Double = 0.0
)

