package com.example.fithub

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
    /*
    lateinit var Gender: Spinner
    lateinit var Height: Number
    lateinit var Weight: Number*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val gender = arrayOf("Male", "Female")
        spinnerGender.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, gender)

        spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }
        }

        username = findViewById<EditText>(R.id.editTextUsername)
        /*var selectedGender = findViewById<Spinner>(R.id.spinnerGender)
        val height = findViewById<EditText>(R.id.editTextHeight)
        val weight = findViewById<EditText>(R.id.editTextWeight)
*/

        btnSave.setOnClickListener{
            saveDataToFirebase()
        }
    }

    private fun saveDataToFirebase() {

        //Get reference from data table Profile
        val ref = FirebaseDatabase.getInstance().getReference("Profile")
        val name = username.text.toString()

        val id =ref.push().key
        val user = User(name)
        ref.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user).addOnCompleteListener{
            Toast.makeText(applicationContext, "User details saved successfully", Toast.LENGTH_SHORT).show()
        }
    }


}

class User(val name: String)





