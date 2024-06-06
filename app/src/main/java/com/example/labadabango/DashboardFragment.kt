package com.example.labadabango

import android.content.ContentValues
import android.content.Intent
import android.graphics.*
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
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
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

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val imageList = ArrayList<SlideModel>() // Create image list

        val cvLaundryWetWashBtn = view.findViewById<CardView>(R.id.cvLaundryWetWashBtn)
        val cvLaundryDryCleanBtn = view.findViewById<CardView>(R.id.cvLaundryDryCleanBtn)
        val cvLaundryPremiumWashBtn = view.findViewById<CardView>(R.id.cvLaundryPremiumWashBtn)
        val cvLaundryIronBtn = view.findViewById<CardView>(R.id.cvLaundryIronBtn)
        val laundryUserNameTV = view.findViewById<TextView>(R.id.laundryUserNameTV)
        val imageView = view.findViewById<ImageView>(R.id.imageView)

        // Get the Firebase Authentication instance
        val firebaseAuth = FirebaseAuth.getInstance()
        // Check if the user is logged in and verified
        val currentUser = firebaseAuth.currentUser
        // Get a reference to the Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()

        val uid = currentUser?.uid

        val userRef = database.getReference("Users").child(uid!!)

        imageList.add(SlideModel(R.drawable.ads1))
        imageList.add(SlideModel(R.drawable.ads2))
        imageList.add(SlideModel(R.drawable.ads3))
        imageList.add(SlideModel(R.drawable.ads4))

        val imageSlider = view.findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("username").value.toString()

                val account = GoogleSignIn.getLastSignedInAccount(requireContext())

                var personUserName: String? = null

                if (account != null) {
                    personUserName = "$username!"
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
                laundryUserNameTV.text = personUserName ?: username
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        cvLaundryWetWashBtn.setOnClickListener {
            // Open intent activity
            val intent = Intent(activity, WetLaundryActivity::class.java)
            startActivity(intent)
        }

        cvLaundryDryCleanBtn.setOnClickListener {
            // Open intent activity
            val intent = Intent(activity, DryLaundryActivity::class.java)
            startActivity(intent)
        }

        cvLaundryPremiumWashBtn.setOnClickListener {
            // Open intent activity
            val intent = Intent(activity, PremiumLaundryActivity::class.java)
            startActivity(intent)
        }

        cvLaundryIronBtn.setOnClickListener {
            // Open intent activity
            val intent = Intent(activity, IroningLaundryActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun openGoogleMaps() {
        // coordinates of the shop location
        val latitude = 12.068406700192135
        val longitude = 124.59533166243585
        val label = "Labadabango: Laundry Made Effortless" // Label for the location marker

        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($label)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        // Check if Google Maps app is installed
        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        } else {
            // If Google Maps app is not installed, open Google Maps in the browser
            val mapsUrl = "http://maps.google.com/maps?q=$latitude,$longitude($label)"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl))
            startActivity(browserIntent)
        }
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

}