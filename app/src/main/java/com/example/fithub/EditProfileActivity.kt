package com.example.fithub

import android.app.Activity
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
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.editTextHeight
import kotlinx.android.synthetic.main.activity_edit_profile.editTextWeight
import kotlinx.android.synthetic.main.activity_sign_up.*

class EditProfileActivity : AppCompatActivity() {

    lateinit var name: EditText
    lateinit var gender: Spinner
    lateinit var height: EditText
    lateinit var weight: EditText
    lateinit var imageUri:Uri
    var downloadUrl = ""

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

        imageViewEditProfilePic.setOnClickListener{
            uploadImage()
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
                    editTextName.setText(profile.name)
                    editTextHeight.setText(profile.height.toString())
                    editTextWeight.setText(profile.weight.toString())
                    var storageRef = FirebaseStorage.getInstance().getReferenceFromUrl()

                    //var ref = database.child(FirebaseAuth.getInstance().currentUser!!.uid).child("imageUri")

                    Picasso.get().load(downloadUrl).into(imageViewProfilePic)
                    val genderDb = profile.gender
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

        val profile = Profile(gender, height, name, weight, downloadUrl)
        database.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(profile)
            .addOnCompleteListener {
                Toast.makeText(
                    applicationContext,
                    "Profile details edit successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun uploadImage() {
        val storage = FirebaseStorage.getInstance()
        var storageRef = storage.reference
        var file = imageUri
        val imagesRef = storageRef.child("images/${file.lastPathSegment}")
        var uploadTask = imagesRef.putFile(file)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }.addOnCompleteListener{
            if(uploadTask.isSuccessful){
                downloadUrl = imagesRef.metadata.toString()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SignUpActivity.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            //Get back uri of the image picked
            imageUri = data.data!!
            //load picture to image view
            Picasso.get().load(imageUri).into(imageViewProfilePic)
            uploadImage()
        }
    }

    companion object{
        const val PICK_IMAGE_REQUEST = 1
    }
}

/*
data class Profile(
    var gender: String? = "",
    var height: Double = 0.0,
    var name: String? = "",
    var weight: Double = 0.0,
    val imageUri: String
)
*/

