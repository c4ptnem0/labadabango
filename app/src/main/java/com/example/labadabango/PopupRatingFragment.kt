package com.example.labadabango

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class PopupRatingFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_popup_rating, container, false)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val closeBtn = view.findViewById<ImageView>(R.id.closeBtn)
        val submitRatingBtn = view.findViewById<Button>(R.id.submitRatingBtn)

        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        var ratingValue = ""

        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            // set rating value to a variable
            ratingValue = rating.toString()
        }

        submitRatingBtn.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val ratingsRef = database.getReference("Ratings")

            val ratingValues = mapOf(
                "rating" to ratingValue
            )

            ratingsRef.child(uid).setValue(ratingValues)
                .addOnSuccessListener {
                    dismiss()
                    Toast.makeText(requireContext(), "Rating Submitted!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    dismiss()
                    Toast.makeText(requireContext(), "Rating failed: $e", Toast.LENGTH_SHORT).show()
                }
        }

        closeBtn.setOnClickListener {
            dismiss()
        }

        return view
    }

}