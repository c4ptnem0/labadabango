package com.example.labadabango

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PopupUserRegisterFragment : DialogFragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_popup_user_register, container, false)

        val userNameET = view.findViewById<EditText>(R.id.userNameET)
        val firstNameET = view.findViewById<EditText>(R.id.firstNameET)
        val lastNameET = view.findViewById<EditText>(R.id.lastNameET)
        val submitBtn = view.findViewById<AppCompatButton>(R.id.submitBtn)
        firebaseAuth = FirebaseAuth.getInstance()

        submitBtn.setOnClickListener{
            val userName = userNameET.text.toString()
            val firstName = firstNameET.text.toString()
            val lastName = lastNameET.text.toString()

            if(userName.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty()) {
                val user = firebaseAuth.currentUser
                val email = firebaseAuth.currentUser?.email
                val uid = user?.uid

                // create a new entry in the "Users" node with uid as key
                val database = FirebaseDatabase.getInstance()
                val usersRef = database.getReference("Users")

                val userValues = mapOf("email" to email,
                    "username" to userName,
                    "firstname" to firstName,
                    "lastname" to lastName)
                uid?.let { usersRef.child(it).setValue(userValues) }

                Toast.makeText(requireContext(), "Registration Success! Thank you for using the app.", Toast.LENGTH_SHORT).show()

                dismiss()
            } else {
                Toast.makeText(requireContext(), "Complete all the fields!", Toast.LENGTH_SHORT).show()
            }

        }

        return view
    }


}