package com.example.fithub

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    lateinit var imageUri: Uri
    var oldDownloadUrl = ""
    var newDownloadUrl = ""
    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        // my_child_toolbar is defined in the layout file
        setSupportActionBar(findViewById(R.id.my_child_toolbar))

        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra("exercise_name")


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

        buttonProfileSave.setOnClickListener {
            writeProfile()
        }

        buttonProfileCancel.setOnClickListener {
            finish()
        }

        imageViewEditProfilePic.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, SignUpActivity.PICK_IMAGE_REQUEST)
        }

        val database = FirebaseDatabase.getInstance().getReference("Profile")
        var profile: Profile

        database.child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText( context,p0.message,Toast.LENGTH_LONG).show()
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //update fields from firebase
                    profile = dataSnapshot.getValue(Profile::class.java)!!
                    editTextName.setText(profile.name.toString())
                    editTextHeight.setText(profile.height.toString())
                    editTextWeight.setText(profile.weight.toString())
                    oldDownloadUrl = profile.downloadUrl
                    if(profile.downloadUrl == ""){
                        Toast.makeText(context, "Unable to retrieve profile picture", Toast.LENGTH_SHORT).show()
                        val res = resources.getDrawable(R.drawable.ic_child_face)
                        imageViewEditProfilePic.setImageDrawable(res)
                    }else{
                        Picasso.get().load(profile.downloadUrl).placeholder(R.drawable.ic_child_face).into(imageViewEditProfilePic)
                    }

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
                newDownloadUrl = downloadUri.toString()
            } else {
                Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SignUpActivity.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            //Get back uri of the image picked
            imageUri = data.data!!
            //load picture to image view
            Picasso.get().load(imageUri).into(imageViewEditProfilePic)
            uploadImage()
        }
    }

    private fun writeProfile() {
        val database = FirebaseDatabase.getInstance().getReference("Profile")
        val name = editTextName.text.toString()
        val gender = spinnerEditGender.selectedItem.toString()
        val height = editTextHeight.text.toString().toDouble()
        val weight = editTextWeight.text.toString().toDouble()

        //get and store points
        val user = FirebaseAuth.getInstance().currentUser
        val pref = this.getSharedPreferences(user?.uid, 0) // 0 - for private mode
        val points = pref.getInt("total_points",0)

        val profile:Profile
        profile = if (newDownloadUrl.isNotEmpty()){
            Profile(name, gender, height,  weight, newDownloadUrl,points)
        } else {
            Profile(name, gender, height,  weight, oldDownloadUrl,points)
        }

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

