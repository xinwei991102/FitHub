package com.example.fithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        // my_child_toolbar is defined in the layout file
        setSupportActionBar(findViewById(R.id.my_child_toolbar))

        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        buttonConfirm.setOnClickListener {
            changePassword()
        }

        buttonCancel.setOnClickListener {
            finish()
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

    private fun changePassword() {
        var newPassword = editTextNewPassword.text.toString()
        var confirmNewPassword = editTextConfirmNewPassword.text.toString()

        if (TextUtils.isEmpty((newPassword))) {
            editTextNewPassword.error = "This field cannot be empty"
        } else {
            if (TextUtils.isEmpty(confirmNewPassword)) {
                editTextConfirmNewPassword.error = "This field cannot be empty"
            } else {
                if (newPassword.equals(confirmNewPassword)) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val txtNewPass = editTextConfirmNewPassword.text.toString()

                    user!!.updatePassword(txtNewPass).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "Password changed successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                task.exception?.message,
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    }
                } else {
                    editTextConfirmNewPassword.error =
                        ("The confirm new password should be same with new password")
                }
            }
        }


    }
}
