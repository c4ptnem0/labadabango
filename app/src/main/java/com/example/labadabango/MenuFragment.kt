package com.example.labadabango

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.*
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlin.math.min

class MenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        // Find the CardView by ID
        val cvLogoutBtn = view.findViewById<CardView>(R.id.cvLogoutBtn)
        val cvChatBtn = view.findViewById<CardView>(R.id.cvChatBtn)
        val cvRateUsBtn = view.findViewById<CardView>(R.id.cvRateUsBtn)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val laundryUserNameTV = view.findViewById<TextView>(R.id.laundryUserNameTV)
        val laundryUserEmailTV = view.findViewById<TextView>(R.id.laundryUserEmailTV)
        val laundrySecondUserNameTV = view.findViewById<TextView>(R.id.laundrySecondUserNameTV)
        val userSettingsBtn = view.findViewById<ImageView>(R.id.userSettingsBtn)

        // Get the Firebase Authentication instance
        val firebaseAuth = FirebaseAuth.getInstance()
        // Check if the user is logged in and verified
        val currentUser = firebaseAuth.currentUser
        // Get a reference to the Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()

        val uid = currentUser?.uid

        val userRef = database.getReference("Users").child(uid!!)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").value.toString()
                val email = snapshot.child("email").value.toString()
                val firstName = snapshot.child("firstname").value.toString()
                val lastName = snapshot.child("lastname").value.toString()
                val laundrySecondUserName = snapshot.child("username").value.toString()
                val laundryUserName = "$firstName $lastName"

                val account = GoogleSignIn.getLastSignedInAccount(requireContext())

                var personEmail: String? = null
                var personDisplayName: String? = null
                var personSecondName: String? = null

                if (account != null) {
                    personEmail = email
                    personDisplayName = laundryUserName
                    personSecondName = laundrySecondUserName
                    val personPhotoUrl = account?.photoUrl?.toString()

                    if (!personPhotoUrl.isNullOrEmpty())
                    {
                        Picasso.get()
                            .load(personPhotoUrl)
                            .transform(transformation)
                            .error(R.mipmap.labadabango_icon_round)
                            .into(imageView, object : Callback {
                                override fun onSuccess() {
                                    Log.d(ContentValues.TAG, "Image loaded successfully")
                                }

                                override fun onError(e: Exception?) {
                                    Log.e(ContentValues.TAG, "Failed to load image", e)
                                }
                            })
                    }
                    else
                    {
                        // Load a placeholder image if the user has no profile photo
                        Picasso.get()
                            .load(R.mipmap.labadabango_icon_round)
                            .into(imageView)
                    }
                }
                // Update the TextViews with the user's display name and email
                laundryUserNameTV.text = personDisplayName ?: username
                laundryUserEmailTV.text = personEmail ?: email
                laundrySecondUserNameTV.text = personSecondName ?: laundrySecondUserName
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        // open user settings
        userSettingsBtn.setOnClickListener {
            val showUserSettings = PopupUserSettingsFragment()

            showUserSettings.show(requireActivity().supportFragmentManager, "showUserSettings")
        }

        cvLogoutBtn.setOnClickListener {
            // Handle logout action
            showLogoutConfirmationDialog()
        }

        cvChatBtn.setOnClickListener {
            // open labadabango's page chat
            val messengerPageID = "127895450409800"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://m.me/$messengerPageID")

            startActivity(intent)
        }

        cvRateUsBtn.setOnClickListener {
            val showRating = PopupRatingFragment()

            showRating.isCancelable = false
            showRating.show(requireActivity().supportFragmentManager, "showRating")
        }

        return view
    }

    // function for making the fetched profile to be circle
    val transformation = object : Transformation {
        override fun key(): String = "circle"

        override fun transform(source: Bitmap): Bitmap {
            val size = min(source.width, source.height)
            val x = (source.width - size) / 2
            val y = (source.height - size) / 2
            val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
            if (squaredBitmap != source) {
                source.recycle()
            }
            val bitmap = Bitmap.createBitmap(size, size, source.config)
            val canvas = Canvas(bitmap)
            val paint = Paint()
            val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.shader = shader
            paint.isAntiAlias = true
            val r = size / 2f
            canvas.drawCircle(r, r, r, paint)
            squaredBitmap.recycle()
            return bitmap
        }
    }


    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                // Handle logout action
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(requireContext(), Login::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}