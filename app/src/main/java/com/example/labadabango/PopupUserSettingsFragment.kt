package com.example.labadabango

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PopupUserSettingsFragment : DialogFragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_popup_user_settings, container, false)
        val laundryUserFirstNameTV = view.findViewById<TextView>(R.id.laundryUserFirstNameTV)
        val laundryUserSecondNameTV = view.findViewById<TextView>(R.id.laundryUserSecondNameTV)
        val laundryUserNameTV = view.findViewById<TextView>(R.id.laundryUserNameTV)
        val saveBtn = view.findViewById<Button>(R.id.saveBtn)

        firebaseAuth = FirebaseAuth.getInstance()

        // create a new entry in the "Users" node with uid as key
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("Users")

        val user = firebaseAuth.currentUser
        val email = firebaseAuth.currentUser?.email
        val uid = user?.uid

        saveBtn.setOnClickListener {
            val userName = laundryUserNameTV.text.toString()
            val firstName = laundryUserFirstNameTV.text.toString()
            val lastName = laundryUserSecondNameTV.text.toString()

            if(userName.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty()) {


                val userValues = mapOf("email" to email,
                    "username" to userName,
                    "firstname" to firstName,
                    "lastname" to lastName)

                usersRef.child(uid!!).setValue(userValues)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Update Sucess!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Update Failed!", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Complete all the fields!", Toast.LENGTH_SHORT).show()
            }
        }


        usersRef.child(uid!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild("firstname")) {
                    val firstName = snapshot.child("firstname").getValue(String::class.java)
                    laundryUserFirstNameTV.text = firstName
                }

                if (snapshot.hasChild("lastname")) {
                    val lastName = snapshot.child("lastname").getValue(String::class.java)
                    laundryUserSecondNameTV.text = lastName
                }

                if (snapshot.hasChild("username")) {
                    val userName = snapshot.child("username").getValue(String::class.java)
                    laundryUserNameTV.text = userName
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        return view
    }


}