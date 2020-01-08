package com.example.fithub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    lateinit var username: EditText
    lateinit var gender: Spinner
    lateinit var height: EditText
    lateinit var weight: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val genderSelection = arrayOf("Male", "Female")
        spinnerGender.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderSelection)

        spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO: hello
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }
        }

        username = findViewById(R.id.editTextUsername)
        gender = findViewById(R.id.spinnerGender)
        height = findViewById(R.id.editTextHeight)
        weight = findViewById(R.id.editTextWeight)

        btnSave.setOnClickListener{
            saveDataToFirebase()
        }
    }

    private fun saveDataToFirebase() {
        //Get reference from data table Profile
        val ref = FirebaseDatabase.getInstance().getReference("Profile")

        //Assign variable
        val name = username.text.toString()
        val gender= spinnerGender.selectedItem.toString()
        val height = height.text.toString().toDouble()
        val weight = weight.text.toString().toDouble()

        val id =ref.push().key
        val user = User(name, gender, height, weight)
        ref.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user).addOnCompleteListener{
            Toast.makeText(applicationContext, "User details saved successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}

class User(val name: String, val gender: String, val height: Double, val weight: Double)





