package com.example.fithub

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        btnSignUp.setOnClickListener {

            val animation = AnimationUtils.loadAnimation(this, R.anim.bounce_anim)
            btnSignUp.startAnimation(animation)

            val email = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //Create a new user with email & password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Log.d("Log In", "Sign Up failure")
                    } else {
                        Toast.makeText(this, "Sign Up Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SignUpActivity::class.java)
                        startActivity(intent)
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }

        btnLogIn.setOnClickListener {
            val email = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            val animation = AnimationUtils.loadAnimation(this, R.anim.bounce_anim)
            btnLogIn.startAnimation(animation)

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Log in", "Sign In with Email:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Wrong username or password.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
        }

    }
}
