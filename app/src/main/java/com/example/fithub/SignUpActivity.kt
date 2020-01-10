package com.example.fithub

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
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

        val genderSelection = arrayOf("Male", "Female")
        spinnerGender.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderSelection)

        spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        imageViewProfilePic.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnSave.setOnClickListener {
            saveDataToFirebase()
        }
    }

    private fun uploadImage() {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val file = imageUri
        val imagesRef = storageRef.child("images/${file.lastPathSegment}")
        val uploadTask = imagesRef.putFile(file)
        var downloadUri:Uri

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
            imagesRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                downloadUri = task.result!!
                downloadUrl = downloadUri.toString()
            } else {
                Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }

        // Register observers to listen for when the download is done or if it fails
//        uploadTask.addOnFailureListener {
//            Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
//        }.addOnSuccessListener {
//            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
//            // ...
//        }.addOnCompleteListener {
//            if (uploadTask.isSuccessful) {
//                downloadUrl = imagesRef.downloadUrl.toString()
//            }
//        }
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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
    }

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

}






