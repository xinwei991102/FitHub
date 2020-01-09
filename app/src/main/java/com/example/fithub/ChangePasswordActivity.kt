package com.example.fithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        buttonConfirm.setOnClickListener {
            changePassword()
        }

        buttonCancel.setOnClickListener {
            finish()
        }
    }

    private fun changePassword() {
        if (editTextNewPassword.text.toString().equals(editTextConfirmNewPassword.text.toString())) {
            val user = FirebaseAuth.getInstance().currentUser
            val txtNewPass = editTextConfirmNewPassword.text.toString()

            user!!.updatePassword(txtNewPass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Password changed successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        task.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
        }


    }
}
