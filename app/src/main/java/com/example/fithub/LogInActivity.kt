package com.example.fithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        val email = editTextUsername.text.toString()
        val password = editTextPassword.text.toString()

        btnSignUp.setOnClickListener {
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //Create a new user with email & password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Log.d("Main", "Fail")
                        return@addOnCompleteListener
                    }
                    //else if successful
                    Log.d("Main", "Successfully created user with uid: ${it.result?.user?.uid}")
                }

        }

        btnLogIn.setOnClickListener{
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Log in", "signInWithEmail:success")
                        val user = FirebaseAuth.getInstance().currentUser
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Log in", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }

                }
        }

    }
}
