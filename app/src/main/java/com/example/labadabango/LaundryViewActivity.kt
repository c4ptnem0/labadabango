package com.example.labadabango

import android.app.ProgressDialog
import android.content.Context
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.ceylonlabs.imageviewpopup.ImagePopup
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.play.integrity.internal.c
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.w3c.dom.Text
import java.text.DecimalFormat


class LaundryViewActivity : AppCompatActivity() {

    private var modeInt: String? = null
    private var laundryTypeInt: String? = null
    private var laundryReferenceNoInt: String? = null
    private var laundryStatusInt: String? = null
    private var tvDeliveryAddressInt: String? = null
    private var qrCodeInt: String? = null
    private var totalPriceInt: String? = null
    private var laundryDateInt: String? = null

    private var itemCountShirtsInt: String? = null
    private var itemCountShortsInt: String? = null
    private var itemCountUnderwearsInt: String? = null
    private var itemCountPantsInt: String? = null
    private var itemCountJacketsInt: String? = null
    private var itemCountBedCoversInt: String? = null
    private var itemCountCarpetsInt: String? = null
    private var itemCountGownsInt: String? = null
    private var itemCountCoatsInt: String? = null
    private var itemCountCurtainsInt: String? = null
    private var itemCountHatsInt: String? = null
    private var itemCountShoesInt: String? = null
    private var itemCountLeatherBagsInt: String? = null
    private var itemCountBedComfortersInt: String? = null
    private var itemCountStuffedToysInt: String? = null
    private var itemCountWoolSweatersInt: String? = null
    private var itemCountDenimJacketsInt: String? = null
    private var itemCountDenimPantsInt: String? = null
    private var itemCountLeatherJacketsInt: String? = null

    private var countShirtsInt: String? = null
    private var countShortsInt: String? = null
    private var countUnderwearsInt: String? = null
    private var countPantsInt: String? = null
    private var countJacketsInt: String? = null
    private var countBedCoversInt: String? = null
    private var countCarpetsInt: String? = null
    private var countGownsInt: String? = null
    private var countCoatsInt: String? = null
    private var countCurtainsInt: String? = null
    private var countHatsInt: String? = null
    private var countShoesInt: String? = null
    private var countLeatherBagsInt: String? = null
    private var countBedComfortersInt: String? = null
    private var countStuffedToysInt: String? = null
    private var countWoolSweatersInt: String? = null
    private var countDenimJacketsInt: String? = null
    private var countDenimPantsInt: String? = null
    private var countLeatherJacketsInt: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_view)

        // Use the custom action bar
        supportActionBar?.setDisplayShowTitleEnabled(false)  // Hide the default title

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val btnLaundry = findViewById<Button>(R.id.btnLaundry)
        val tvLaundryType = findViewById<TextView>(R.id.tvLaundryType)
        val tvlaundryReferenceNo = findViewById<TextView>(R.id.tvlaundryReferenceNo)
        val laundryStatus = findViewById<TextView>(R.id.laundryStatus)
        val tvDeliveryAddress = findViewById<TextView>(R.id.tvDeliveryAddress)
        val totalPrice = findViewById<TextView>(R.id.totalPrice)
        val laundryDate = findViewById<TextView>(R.id.laundryDate)

        val itemCountShirts = findViewById<TextView>(R.id.itemCountShirts)
        val itemCountShorts = findViewById<TextView>(R.id.itemCountShorts)
        val itemCountUnderwears = findViewById<TextView>(R.id.itemCountUnderwears)
        val itemCountPants = findViewById<TextView>(R.id.itemCountPants)
        val itemCountJackets = findViewById<TextView>(R.id.itemCountJackets)
        val itemCountBedCovers = findViewById<TextView>(R.id.itemCountBedCovers)
        val itemCountCarpets = findViewById<TextView>(R.id.itemCountCarpets)
        val itemCountGowns = findViewById<TextView>(R.id.itemCountGowns)
        val itemCountCoats = findViewById<TextView>(R.id.itemCountCoats)
        val itemCountCurtains = findViewById<TextView>(R.id.itemCountCurtains)
        val itemCountHats = findViewById<TextView>(R.id.itemCountHats)
        val itemCountShoes = findViewById<TextView>(R.id.itemCountShoes)
        val itemCountLeatherBags = findViewById<TextView>(R.id.itemCountLeatherBags)
        val itemCountBedComforters = findViewById<TextView>(R.id.itemCountBedComforters)
        val itemCountStuffedToys = findViewById<TextView>(R.id.itemCountStuffedToys)
        val itemCountWoolSweaters = findViewById<TextView>(R.id.itemCountWoolSweaters)
        val itemCountDenimJackets = findViewById<TextView>(R.id.itemCountDenimJackets)
        val itemCountDenimPants = findViewById<TextView>(R.id.itemCountDenimPants)
        val itemCountLeatherJackets = findViewById<TextView>(R.id.itemCountLeatherJackets)

        val countShirts = findViewById<TextView>(R.id.countShirts)
        val countShorts = findViewById<TextView>(R.id.countShorts)
        val countUnderwears = findViewById<TextView>(R.id.countUnderwears)
        val countPants = findViewById<TextView>(R.id.countPants)
        val countJackets = findViewById<TextView>(R.id.countJackets)
        val countBedCovers = findViewById<TextView>(R.id.countBedCovers)
        val countCarpets = findViewById<TextView>(R.id.countCarpets)
        val countGowns = findViewById<TextView>(R.id.countGowns)
        val countCoats = findViewById<TextView>(R.id.countCoats)
        val countCurtains = findViewById<TextView>(R.id.countCurtains)
        val countHats = findViewById<TextView>(R.id.countHats)
        val countShoes = findViewById<TextView>(R.id.countShoes)
        val countLeatherBags = findViewById<TextView>(R.id.countLeatherBags)
        val countBedComforters = findViewById<TextView>(R.id.countBedComforters)
        val countStuffedToys = findViewById<TextView>(R.id.countStuffedToys)
        val countWoolSweaters = findViewById<TextView>(R.id.countWoolSweaters)
        val countDenimJackets = findViewById<TextView>(R.id.countDenimJackets)
        val countDenimPants = findViewById<TextView>(R.id.countDenimPants)
        val countLeatherJackets = findViewById<TextView>(R.id.countLeatherJackets)

        val laundryStatusImageIV = findViewById<ImageView>(R.id.laundryStatusImageIV)
        val qrCode = findViewById<ImageView>(R.id.qrCode)

        val itemShirts = findViewById<LinearLayout>(R.id.itemShirts)
        val itemShorts = findViewById<LinearLayout>(R.id.itemShorts)
        val itemUnderwears = findViewById<LinearLayout>(R.id.itemUnderwears)
        val itemPants = findViewById<LinearLayout>(R.id.itemPants)
        val itemJackets = findViewById<LinearLayout>(R.id.itemJackets)
        val itemBedCovers = findViewById<LinearLayout>(R.id.itemBedCovers)
        val itemCarpets = findViewById<LinearLayout>(R.id.itemCarpets)
        val itemGowns = findViewById<LinearLayout>(R.id.itemGowns)
        val itemCoats = findViewById<LinearLayout>(R.id.itemCoats)
        val itemCurtains = findViewById<LinearLayout>(R.id.itemCurtains)
        val itemHats = findViewById<LinearLayout>(R.id.itemHats)
        val itemShoes = findViewById<LinearLayout>(R.id.itemShoes)
        val itemLeatherBags = findViewById<LinearLayout>(R.id.itemLeatherBags)
        val itemBedComforters = findViewById<LinearLayout>(R.id.itemBedComforters)
        val itemStuffedToys = findViewById<LinearLayout>(R.id.itemStuffedToys)
        val itemWoolSweaters = findViewById<LinearLayout>(R.id.itemWoolSweaters)
        val itemDenimJackets = findViewById<LinearLayout>(R.id.itemDenimJackets)
        val itemDenimPants = findViewById<LinearLayout>(R.id.itemDenimPants)
        val itemLeatherJackets = findViewById<LinearLayout>(R.id.itemLeatherJackets)


        // get the passed extra values from the previous activity
        modeInt = intent.getStringExtra("mode")
        laundryTypeInt = intent.getStringExtra("laundryType")
        laundryReferenceNoInt = intent.getStringExtra("laundryReferenceNo")
        laundryStatusInt = intent.getStringExtra("laundryStatus")
        tvDeliveryAddressInt = intent.getStringExtra("deliveryAddress")
        qrCodeInt = intent.getStringExtra("qrCode")
        totalPriceInt = intent.getStringExtra("totalPrice")
        laundryDateInt = intent.getStringExtra("laundryDate")

        itemCountShirtsInt = intent.getStringExtra("itemCountShirts")
        itemCountShortsInt = intent.getStringExtra("itemCountShorts")
        itemCountUnderwearsInt = intent.getStringExtra("itemCountUnderwears")
        itemCountPantsInt = intent.getStringExtra("itemCountPants")
        itemCountJacketsInt = intent.getStringExtra("itemCountJackets")
        itemCountBedCoversInt = intent.getStringExtra("itemCountBedCovers")
        itemCountCarpetsInt = intent.getStringExtra("itemCountCarpets")
        itemCountGownsInt = intent.getStringExtra("itemCountGowns")
        itemCountCoatsInt = intent.getStringExtra("itemCountCoats")
        itemCountCurtainsInt = intent.getStringExtra("itemCountCurtains")
        itemCountHatsInt = intent.getStringExtra("itemCountHats")
        itemCountShoesInt = intent.getStringExtra("itemCountShoes")
        itemCountLeatherBagsInt = intent.getStringExtra("itemCountLeatherBags")
        itemCountBedComfortersInt = intent.getStringExtra("itemCountBedComforters")
        itemCountStuffedToysInt = intent.getStringExtra("itemCountStuffedToys")
        itemCountWoolSweatersInt = intent.getStringExtra("itemCountWoolSweaters")
        itemCountDenimJacketsInt = intent.getStringExtra("itemCountDenimJackets")
        itemCountDenimPantsInt = intent.getStringExtra("itemCountDenimPants")
        itemCountLeatherJacketsInt = intent.getStringExtra("itemCountLeatherJackets")

        countShirtsInt = intent.getStringExtra("countShirts")
        countShortsInt = intent.getStringExtra("countShorts")
        countUnderwearsInt = intent.getStringExtra("countUnderwears")
        countPantsInt = intent.getStringExtra("countPants")
        countJacketsInt = intent.getStringExtra("countJackets")
        countBedCoversInt = intent.getStringExtra("countBedCovers")
        countCarpetsInt = intent.getStringExtra("countCarpets")
        countGownsInt = intent.getStringExtra("countGowns")
        countCoatsInt = intent.getStringExtra("countCoats")
        countCurtainsInt = intent.getStringExtra("countCurtains")
        countHatsInt = intent.getStringExtra("countHats")
        countShoesInt = intent.getStringExtra("countShoes")
        countLeatherBagsInt = intent.getStringExtra("countLeatherBags")
        countBedComfortersInt = intent.getStringExtra("countBedComforters")
        countStuffedToysInt = intent.getStringExtra("countStuffedToys")
        countWoolSweatersInt = intent.getStringExtra("countWoolSweaters")
        countDenimJacketsInt = intent.getStringExtra("countDenimJackets")
        countDenimPantsInt = intent.getStringExtra("countDenimPants")
        countLeatherJacketsInt = intent.getStringExtra("countLeatherJackets")

        // Convert the string to a Double
        val countShirtsDouble = countShirtsInt?.toDoubleOrNull() ?: 0.0
        val countShortsDouble = countShortsInt?.toDoubleOrNull() ?: 0.0
        val countUnderwearsDouble = countUnderwearsInt?.toDoubleOrNull() ?: 0.0
        val countPantsDouble = countPantsInt?.toDoubleOrNull() ?: 0.0
        val countJacketsDouble = countJacketsInt?.toDoubleOrNull() ?: 0.0
        val countBedCoversDouble = countBedCoversInt?.toDoubleOrNull() ?: 0.0
        val countCarpetsDouble = countCarpetsInt?.toDoubleOrNull() ?: 0.0
        val countGownsDouble = countGownsInt?.toDoubleOrNull() ?: 0.0
        val countCoatsDouble = countCoatsInt?.toDoubleOrNull() ?: 0.0
        val countCurtainsDouble = countCurtainsInt?.toDoubleOrNull() ?: 0.0
        val countHatsDouble = countHatsInt?.toDoubleOrNull() ?: 0.0
        val countShoesDouble = countShoesInt?.toDoubleOrNull() ?: 0.0
        val countLeatherBagsDouble = countLeatherBagsInt?.toDoubleOrNull() ?: 0.0
        val countBedComfortersDouble = countBedComfortersInt?.toDoubleOrNull() ?: 0.0
        val countStuffedToysDouble = countStuffedToysInt?.toDoubleOrNull() ?: 0.0
        val countWoolSweatersDouble = countWoolSweatersInt?.toDoubleOrNull() ?: 0.0
        val countDenimJacketsDouble = countDenimJacketsInt?.toDoubleOrNull() ?: 0.0
        val countDenimPantsDouble = countDenimPantsInt?.toDoubleOrNull() ?: 0.0
        val countLeatherJacketsDouble = countLeatherJacketsInt?.toDoubleOrNull() ?: 0.0

        val decimalFormat = DecimalFormat("₱ #,##0.00")
        val formattedCountShirts = decimalFormat.format(countShirtsDouble)
        val formattedCountShorts = decimalFormat.format(countShortsDouble)
        val formattedCountUnderwears = decimalFormat.format(countUnderwearsDouble)
        val formattedCountPants = decimalFormat.format(countPantsDouble)
        val formattedCountJackets = decimalFormat.format(countJacketsDouble)
        val formattedCountBedCovers = decimalFormat.format(countBedCoversDouble)
        val formattedCountCarpets = decimalFormat.format(countCarpetsDouble)
        val formattedCountGowns = decimalFormat.format(countGownsDouble)
        val formattedCountCoats = decimalFormat.format(countCoatsDouble)
        val formattedCountCurtains = decimalFormat.format(countCurtainsDouble)
        val formattedCountHats = decimalFormat.format(countHatsDouble)
        val formattedCountShoes = decimalFormat.format(countShoesDouble)
        val formattedCountLeatherBags = decimalFormat.format(countLeatherBagsDouble)
        val formattedCountBedComforters = decimalFormat.format(countBedComfortersDouble)
        val formattedCountStuffedToys = decimalFormat.format(countStuffedToysDouble)
        val formattedCountWoolSweaters = decimalFormat.format(countWoolSweatersDouble)
        val formattedCountDenimJackets = decimalFormat.format(countDenimJacketsDouble)
        val formattedCountDenimPants = decimalFormat.format(countDenimPantsDouble)
        val formattedCountLeatherJackets = decimalFormat.format(countLeatherJacketsDouble)


        val imageUrl = qrCodeInt

        Glide.with(this)
            .load(imageUrl)
            .into(qrCode)

        val imagePopup = ImagePopup(this)
        imagePopup.initiatePopupWithGlide(imageUrl)

        // Disable close button
        imagePopup.isHideCloseIcon = true
        imagePopup.isImageOnClickClose = true
        imagePopup.windowHeight = 600
        imagePopup.windowWidth = 600

        // set the textviews and imageviews in the view booking
        tvLaundryType.setText(laundryTypeInt)
        tvlaundryReferenceNo.setText(laundryReferenceNoInt)
        laundryStatus.setText(laundryStatusInt)
        totalPrice.setText(totalPriceInt)
        laundryDate.setText(laundryDateInt)

        itemCountShirts.setText(itemCountShirtsInt)
        itemCountShorts.setText(itemCountShortsInt)
        itemCountUnderwears.setText(itemCountUnderwearsInt)
        itemCountPants.setText(itemCountPantsInt)
        itemCountJackets.setText(itemCountJacketsInt)
        itemCountBedCovers.setText(itemCountBedCoversInt)
        itemCountCarpets.setText(itemCountCarpetsInt)
        itemCountGowns.setText(itemCountGownsInt)
        itemCountCoats.setText(itemCountCoatsInt)
        itemCountCurtains.setText(itemCountCurtainsInt)
        itemCountHats.setText(itemCountHatsInt)
        itemCountShoes.setText(itemCountShoesInt)
        itemCountLeatherBags.setText(itemCountLeatherBagsInt)
        itemCountBedComforters.setText(itemCountBedComfortersInt)
        itemCountStuffedToys.setText(itemCountStuffedToysInt)
        itemCountWoolSweaters.setText(itemCountWoolSweatersInt)
        itemCountDenimJackets.setText(itemCountDenimJacketsInt)
        itemCountDenimPants.setText(itemCountDenimPantsInt)
        itemCountLeatherJackets.setText(itemCountLeatherJacketsInt)

        countShirts.setText(formattedCountShirts)
        countShorts.setText(formattedCountShorts)
        countUnderwears.setText(formattedCountUnderwears)
        countPants.setText(formattedCountPants)
        countJackets.setText(formattedCountJackets)
        countBedCovers.setText(formattedCountBedCovers)
        countCarpets.setText(formattedCountCarpets)
        countGowns.setText(formattedCountGowns)
        countCoats.setText(formattedCountCoats)
        countCurtains.setText(formattedCountCurtains)
        countHats.setText(formattedCountHats)
        countShoes.setText(formattedCountShoes)
        countLeatherBags.setText(formattedCountLeatherBags)
        countBedComforters.setText(formattedCountBedComforters)
        countStuffedToys.setText(formattedCountStuffedToys)
        countWoolSweaters.setText(formattedCountWoolSweaters)
        countDenimJackets.setText(formattedCountDenimJackets)
        countDenimPants.setText(formattedCountDenimPants)
        countLeatherJackets.setText(formattedCountLeatherJackets)

        if(tvDeliveryAddressInt == "") {
            tvDeliveryAddress.setText("None (Pickup)")
        } else {
            tvDeliveryAddress.setText(tvDeliveryAddressInt)
        }

        if(modeInt == "Laundry List") {
            btnLaundry.text = "Cancel Booking"
        } else {
            btnLaundry.text = "Delete Booking"
        }

        if(laundryStatusInt == "In Progress") {
            btnLaundry.setBackgroundColor(resources.getColor(R.color.gray))
        }

        // Cancel laundry function
        btnLaundry.setOnClickListener {
            if(laundryStatusInt == "In Progress") {
                Toast.makeText(this@LaundryViewActivity, "Sorry, you can't cancel this booking.", Toast.LENGTH_SHORT).show()
            } else {
                if(modeInt == "Laundry List") {
                    AlertDialog.Builder(this)
                        .setTitle("Cancel Booking")
                        .setMessage("Are you sure you want to cancel it?")
                        .setPositiveButton("Yes"){ _, _ ->
                            // Show the progress dialog
                            val progressDialog = showProgressDialog(this@LaundryViewActivity)

                            // Delete the product with a seconds delay to display progress dialog
                            Handler(Looper.getMainLooper()).postDelayed({
                                cancelOrDeleteProduct()
                                // Hide the progress dialog
                                progressDialog.dismiss()
                            }, 1000)
                        }
                        .setNegativeButton("No", null)
                        .show()
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("Delete Laundry History")
                        .setMessage("Are you sure you want to delete it?")
                        .setPositiveButton("Yes"){ _, _ ->
                            // Show the progress dialog
                            val progressDialog = showProgressDialog(this@LaundryViewActivity)

                            // Delete the product with a seconds delay to display progress dialog
                            Handler(Looper.getMainLooper()).postDelayed({
                                cancelOrDeleteProduct()
                                // Hide the progress dialog
                                progressDialog.dismiss()
                            }, 1000)
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
        }

        setVisibility(itemShirts, countShirtsDouble)
        setVisibility(itemShorts, countShortsDouble)
        setVisibility(itemUnderwears, countUnderwearsDouble)
        setVisibility(itemPants, countPantsDouble)
        setVisibility(itemJackets, countJacketsDouble)
        setVisibility(itemBedCovers, countBedCoversDouble)
        setVisibility(itemCarpets, countCarpetsDouble)
        setVisibility(itemGowns, countGownsDouble)
        setVisibility(itemCoats, countCoatsDouble)
        setVisibility(itemCurtains, countCurtainsDouble)
        setVisibility(itemHats, countHatsDouble)
        setVisibility(itemShoes, countShoesDouble)
        setVisibility(itemLeatherBags, countLeatherBagsDouble)
        setVisibility(itemBedComforters, countBedComfortersDouble)
        setVisibility(itemStuffedToys, countStuffedToysDouble)
        setVisibility(itemWoolSweaters, countWoolSweatersDouble)
        setVisibility(itemDenimJackets, countDenimJacketsDouble)
        setVisibility(itemDenimPants, countDenimPantsDouble)
        setVisibility(itemLeatherJackets, countLeatherJacketsDouble)

        when (laundryStatusInt?.trim()) {
            "Collected" -> {
                laundryStatusImageIV.setImageResource(R.drawable.laundry_collected)
            }
            "In Progress" -> {
                laundryStatusImageIV.setImageResource(R.drawable.laundry_inprogress)
            }
            "Completed" -> {
                laundryStatusImageIV.setImageResource(R.drawable.laundry_completed)

            }
            else -> {
                laundryStatusImageIV.setImageResource(R.drawable.laundry_placed)
            }
        }

        // Set up click listener for your ImageView
        qrCode.setOnClickListener {
            imagePopup.viewPopup()
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun showProgressDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Please wait while we process your request")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        return progressDialog
    }

    private fun setVisibility(view: View, count: Double) {
        view.visibility = if (count == 0.0) View.GONE else View.VISIBLE
    }

    // function for cancelling the product
    private fun cancelOrDeleteProduct() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Bookings")

        // Constructing the reference to the specific entry based on the laundry type and reference number
        val laundryTypeRef = databaseReference.child(laundryTypeInt!!)
        val laundryEntryRef = laundryTypeRef.child(laundryReferenceNoInt!!)

        // Set keepSynced to false to ensure immediate removal
        laundryEntryRef.keepSynced(false)

        // Remove the entry from the Realtime Database
        laundryEntryRef.removeValue().addOnSuccessListener {
            // Get the QR code image URL
            val imageUrl = qrCodeInt

            // Check if the image URL is not null or empty
            if (!imageUrl.isNullOrEmpty()) {
                // Get a reference to the storage location of the image
                val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)

                // Delete the image from Firebase Storage
                storageReference.delete().addOnSuccessListener {
                    finish()
                    Log.d("LaundryViewActivity", "QR code image deleted successfully")
                }.addOnFailureListener {
                    Log.e("LaundryViewActivity", "Error deleting QR code image: $it")
                    // Handle the error as needed
                }
            }

            Log.d("LaundryViewActivity", "Laundry entry deleted successfully")
        }.addOnFailureListener {
            Log.e("LaundryViewActivity", "Error deleting laundry entry: $it")
            // Handle the error as needed
        }
    }


}