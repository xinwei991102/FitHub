package com.example.fithub

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {
    lateinit var imageUri: Uri
    var downloadUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSupportActionBar(findViewById(R.id.my_child_toolbar))
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val genderSelection = arrayOf("Male", "Female")
        spinnerGender.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderSelection)

        btnSave.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val confirmPassword = editTextConfirmPassword.text.toString().trim()
            val height = editTextHeight.text.toString().trim()
            val weight = editTextWeight.text.toString().trim()
            var validDetails = true
            val errorMsg = "This field must no be empty!"

            //Validation
            if(TextUtils.isEmpty(email)) {
                validDetails = false
                editTextEmail.error = errorMsg
            }
            if(TextUtils.isEmpty(username)) {
                validDetails = false
                editTextUsername.error = errorMsg
            }
            if(TextUtils.isEmpty(password) || password.length <= 5) {
                validDetails = false
                editTextPassword.error = "Password must not less than 6 characters"
            }

            if(TextUtils.isEmpty(confirmPassword) || password.length <= 5) {
                validDetails = false
                editTextConfirmPassword.error = "Password must not less than 6 characters"
             }else if(!editTextConfirmPassword.text.toString().equals(editTextPassword.text.toString())){
                validDetails = false
                editTextConfirmPassword.error = "Password does not match!"
            }
            if (TextUtils.isEmpty(height)) {
                validDetails = false
                editTextHeight.error = errorMsg
            }
            if (TextUtils.isEmpty(weight)) {
                validDetails = false
                editTextWeight.error = errorMsg
            }


            spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    spinnerGender.setSelection(0)
                    //Force spinner to select an item
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                }
            }

            //Create a new user with email & password
            if (validDetails) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) {
                            Toast.makeText(this, "Sign Up Failure", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Sign Up Successfully", Toast.LENGTH_SHORT).show()
                            saveDataToFirebase()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            //finish()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
            }
        }

        btnNewProfilePic.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return (super.onOptionsItemSelected(item))
    }

    private fun uploadImage() {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val file = imageUri
        val imagesRef = storageRef.child("images/${file.lastPathSegment}")
        val uploadTask = imagesRef.putFile(file)
        var downloadUri: Uri

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_SHORT)
                    .show()
            }
            imagesRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                downloadUri = task.result!!
                downloadUrl = downloadUri.toString()
            } else {
                Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            //Get back uri of the image picked
            imageUri = data.data!!
            //load picture to image view
            Picasso.get().load(imageUri).into(imageViewProfilePic)
            uploadImage()
        }
    }

    private fun saveDataToFirebase() {
        //Get reference from data table Profile
        val ref = FirebaseDatabase.getInstance().getReference("Profile")

        //Assign variable
        val name = editTextUsername.text.toString()
        val gender = spinnerGender.selectedItem.toString()
        val height = editTextHeight.text.toString().toDouble()
        val weight = editTextWeight.text.toString().toDouble()
        val points = 0

        //val id =ref.push().key
        val user = Profile(name, gender, height, weight, downloadUrl, points)
        ref.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user)
            .addOnCompleteListener {
                Toast.makeText(
                    applicationContext,
                    "User details saved successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

}






