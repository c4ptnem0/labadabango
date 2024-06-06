package com.example.labadabango

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TimeFormatException
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.labadabango.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentManager: FragmentManager
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Use the custom action bar
        supportActionBar?.setDisplayShowTitleEnabled(false)  // Hide the default title
        // Set the action bar background color
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.color.white))

        val customActionBar = layoutInflater.inflate(R.layout.custom_action_bar, null)
        val shopLocationBtn = customActionBar.findViewById<ImageView>(R.id.shopLocationBtn)
        var username = ""

        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.customView = customActionBar

        // Get the Firebase Authentication instance
        val firebaseAuth = FirebaseAuth.getInstance()
        // Check if the user is logged in and verified
        val currentUser = firebaseAuth.currentUser
        // Get a reference to the Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()

        val userRef = database.getReference("Users")
        val ratingsRef = database.getReference("Ratings")

        // if the user is not logged in and not verified, redirect to Login Activity
        if (currentUser == null || !currentUser.isEmailVerified) {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
            return
        }

        // function for calling the LaundryNotificationService to show notification
        val intent = Intent(this, LaundryNotificationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            startForegroundService(intent)
        } else
        {
            startService(intent)
        }

        ratingsRef.child(currentUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    
                } else {
                    // Delay in milliseconds (5 minutes)
                    val delayMillis = 2 * 60 * 1000
                    handler.postDelayed({
                        // Show the rating dialog
                        val showRating = PopupRatingFragment()
                        showRating.show(supportFragmentManager, "showRating")
                    }, delayMillis.toLong())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



        // function for making the user register its valid information
        userRef.child(currentUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // do nothing
                } else {
                    // else, proceed to show Popup Fragment
                    val showPopUp = PopupUserRegisterFragment()

                    showPopUp.isCancelable = false
                    showPopUp.show(supportFragmentManager, "showPopUp")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that may occur during the database query
                Log.e(TAG, "Error querying database: ${databaseError.message}")
            }
        })

        shopLocationBtn.setOnClickListener {
            openGoogleMaps()
        }


        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.dashboard -> openFragment(DashboardFragment())
                R.id.history -> openFragment(HistoryListFragment())
                R.id.booking_list -> openFragment(LaundryListFragment())
                R.id.menu -> openFragment(MenuFragment())
            }
            true
        }
        fragmentManager = supportFragmentManager
        openFragment(DashboardFragment())
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.dashboard -> openFragment(DashboardFragment())
            R.id.history -> openFragment(HistoryListFragment())
            R.id.booking_list -> openFragment(LaundryListFragment())
            R.id.menu -> openFragment(MenuFragment())

        }
        return true
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
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
        if (mapIntent.resolveActivity(this.packageManager) != null) {
            startActivity(mapIntent)
        } else {
            // If Google Maps app is not installed, open Google Maps in the browser
            val mapsUrl = "http://maps.google.com/maps?q=$latitude,$longitude($label)"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl))
            startActivity(browserIntent)
        }
    }
}
