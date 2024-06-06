package com.example.labadabango

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        supportActionBar?.hide()

        val emailET = findViewById<EditText>(R.id.emailET)
        val forgotPasswordBtn = findViewById<Button>(R.id.forgotPasswordBtn)
        val signInBtn = findViewById<TextView>(R.id.signInBtn)

        val auth = FirebaseAuth.getInstance()

        forgotPasswordBtn.setOnClickListener {
            val emailReset = emailET.text.toString()

            auth.sendPasswordResetEmail(emailReset)
                .addOnSuccessListener {
                    Toast.makeText(this, "Please Check your Email", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Email not found!", Toast.LENGTH_SHORT).show()
                }
        }

        signInBtn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }
}