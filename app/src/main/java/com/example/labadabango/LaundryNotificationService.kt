package com.example.labadabango

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class LaundryNotificationService : Service() {
    private val CHANNEL_ID = "labadabango_laundry_channel"
    private val NOTIFICATION_ID = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        startTask("Wet Washing")
        startTask("Dry Cleaning")
        startTask("Premium Washing")
        startTask("Ironing")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Expired Product Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // create notiication
    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent: PendingIntent

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Labadabango Laundry Service")
            .setContentText("Running in the background")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(pendingIntent)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startTask(laundryType: String) {
        val userID = FirebaseAuth.getInstance().currentUser?.uid

        val userBookingsRef = getUserBookingsRef(laundryType)

        userBookingsRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                checkForStatusUpdate(snapshot) { statusUpdates ->
                    if (statusUpdates != null) {
                        showStatusUpdateNotifications(statusUpdates)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }

    private fun getUserBookingsRef(laundryType: String): DatabaseReference? {
        val baseRef = FirebaseDatabase.getInstance().getReference("Bookings")

        return when (laundryType) {
            "Wet Washing" -> baseRef.child("Wet Washing")
            "Dry Cleaning" -> baseRef.child("Dry Cleaning")
            "Premium Washing" -> baseRef.child("Premium Washing")
            "Ironing" -> baseRef.child("Ironing")
            // Add cases for other laundry types as needed
            else -> null
        }
    }

    private fun checkForStatusUpdate(snapshot: DataSnapshot, callback: (LaundryData?) -> Unit) {
        val statusUpdates = mutableListOf<LaundryData>()

        for (itemSnapshot in snapshot.children) {
            val laundryType = itemSnapshot.child("laundryType").getValue(String::class.java)

            // Choose the appropriate implementation based on laundryType
            val dataClass: LaundryData? = when (laundryType) {
                "Wet Washing" -> itemSnapshot.getValue(DataClassWetLaundry::class.java)
                "Dry Cleaning" -> itemSnapshot.getValue(DataClassDryLaundry::class.java)
                "Premium Washing" -> itemSnapshot.getValue(DataClassPremiumLaundry::class.java)
                "Ironing" -> itemSnapshot.getValue(DataClassIronLaundry::class.java)
                // Add cases for other laundry types as needed
                else -> null
            }

            if (dataClass != null) {
                val etStatus = dataClass.etStatus

                // Check if the etStatus has changed from the previous status
                val previousStatus = itemSnapshot.child("previousStatus").getValue(String::class.java)
                if (etStatus == "Collected" || etStatus == "In Progress" || etStatus == "Completed") {
                    if (etStatus != previousStatus) {
                        dataClass.laundryReferenceNo = itemSnapshot.key
                        statusUpdates.add(dataClass)
                    }
                }

                // Save the current status as previousStatus for the next comparison
                itemSnapshot.child("previousStatus").ref.setValue(etStatus)
            }
        }

        // If there are updates, notify the callback
        if (statusUpdates.isNotEmpty()) {
            // If you want to handle multiple updates, you might want to modify the callback to receive a list
            callback(statusUpdates.firstOrNull())
        } else {
            // If there are no updates, callback with null
            callback(null)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showStatusUpdateNotifications(statusUpdate: LaundryData) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_UPDATE_CURRENT
        )

        val statusText = when (statusUpdate.etStatus) {
            "Collected" -> "Your ${statusUpdate.laundryType} (${statusUpdate.laundryReferenceNo}) has been collected. Thank you!"
            "In Progress" -> "Your ${statusUpdate.laundryType} (${statusUpdate.laundryReferenceNo}) is in progress. We'll notify you once it's done."
            "Completed" -> "Your ${statusUpdate.laundryType} (${statusUpdate.laundryReferenceNo}) is completed and ready. Thank you!"
            else -> ""
        }

        if (statusText.isNotEmpty()) {
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Laundry Status Update")
                .setContentText(statusText)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
            // Use a unique notification ID for each status update to avoid overwriting previous notifications
            notificationManager.notify(statusUpdate.laundryReferenceNo.hashCode(), notification)
        }
    }
    
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}
