package com.example.labadabango

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isNotEmpty
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class WetLaundryActivity : AppCompatActivity() {

    // declarations
    private lateinit var tvPriceShirts: TextView
    private lateinit var tvPriceShorts: TextView
    private lateinit var tvPriceUnderwears: TextView
    private lateinit var tvPricePants: TextView
    private lateinit var tvPriceJackets: TextView
    private lateinit var tvPriceBedcovers: TextView
    private lateinit var tvPriceCarpets: TextView

    private lateinit var imageAdd1: ImageView
    private lateinit var imageAdd2: ImageView
    private lateinit var imageAdd3: ImageView
    private lateinit var imageAdd4: ImageView
    private lateinit var imageAdd5: ImageView
    private lateinit var imageAdd6: ImageView
    private lateinit var imageAdd7: ImageView
    private lateinit var imageMinus1: ImageView
    private lateinit var imageMinus2: ImageView
    private lateinit var imageMinus3: ImageView
    private lateinit var imageMinus4: ImageView
    private lateinit var imageMinus5: ImageView
    private lateinit var imageMinus6: ImageView
    private lateinit var imageMinus7: ImageView

    private lateinit var tvTotalPrice: TextView
    private lateinit var tvItemCount: TextView
    private lateinit var spWashInstruction: Spinner
    private lateinit var spDryInstruction: Spinner
    private lateinit var radioButtonDetergent: RadioGroup
    private lateinit var inputAdditionalDescription: TextInputEditText
    private lateinit var radioButtonDeliveryOption: RadioGroup
    private lateinit var inputAddressInformation: TextInputEditText
    private lateinit var inputAdditionalAddressInformation: TextInputEditText
    private lateinit var spPaymentMethod: Spinner
    private lateinit var tvAddressInfo1: TextView
    private lateinit var tvAddressInfo2: TextView

    private lateinit var btnAddLaundry: Button
    private lateinit var ivQRCode: ImageView
    private lateinit var etStatus: EditText
    private lateinit var etLaundryDate: EditText

    // Get the Firebase Storage reference
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    // get the current user's ID
    val uid = FirebaseAuth.getInstance().currentUser!!.uid

    var priceShirts = 4.00
    var priceShorts = 3.00
    var priceUnderwears = 1.00
    var pricePants = 8.00
    var priceJackets = 10.00
    var priceBedscovers = 15.00
    var priceCarpets = 30.00

    var itemCountShirts = 0
    var itemCountShorts = 0
    var itemCountUnderWears = 0
    var itemCountPants = 0
    var itemCountJackets = 0
    var itemCountBedCovers = 0
    var itemCountCarpets = 0

    var totalPrice = 0.0
    var totalItems = 0

    var countShirts: Double = 0.0
    var countShorts: Double = 0.0
    var countUnderwears: Double = 0.0
    var countPants: Double = 0.0
    var countJackets: Double = 0.0
    var countBedcovers: Double = 0.0
    var countCarpets: Double = 0.0

    var selectedSpWashInstruction: String = ""
    var selectedSpDryInstruction: String = ""
    var selectedRadioButtonDetergent: String = ""
    var selectedRadioButtonDeliveryOption: String = ""
    var selectedSpPaymentMethod: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wet_laundry)

        // Hide the title bar
        supportActionBar?.hide()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // function call
        setInitLayout()
        setDataShirts()
        setDataShorts()
        setDataUnderwears()
        setDataPants()
        setDataJackets()
        setDataBedcovers()
        setDataCarpets()

        // spinner selection
        spWashInstruction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Get the selected item from the spinner
                selectedSpWashInstruction = parentView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do something when nothing is selected
            }
        }

        spDryInstruction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Get the selected item from the spinner
                selectedSpDryInstruction = parentView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do something when nothing is selected
            }
        }

        spPaymentMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Get the selected item from the spinner
                selectedSpPaymentMethod = parentView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do something when nothing is selected
            }
        }

        radioButtonDetergent.setOnCheckedChangeListener { group, checkedId ->
            // Get the selected radio button
            val selectedRadioButton: RadioButton = findViewById(checkedId)

            // Get the text of the selected radio button
            selectedRadioButtonDetergent = selectedRadioButton.text.toString()
        }

        radioButtonDeliveryOption.setOnCheckedChangeListener { group, checkedId ->
            // Get the selected radio button
            val selectedRadioButton: RadioButton = findViewById(checkedId)

            // Get the text of the selected radio button
            selectedRadioButtonDeliveryOption = selectedRadioButton.text.toString()

            // Check if "pickup" is selected
            if (selectedRadioButtonDeliveryOption.equals("pickup", ignoreCase = true)) {
                // Disable the address information fields
                inputAddressInformation.isEnabled = false
                inputAdditionalAddressInformation.isEnabled = false
                // Set hints to indicate the disabled state
                inputAddressInformation.hint = "Brgy/Street Name/City (Disabled)"
                inputAdditionalAddressInformation.hint = "Additional Address Information (Disabled)"
            } else {
                // Enable the address information fields
                inputAddressInformation.isEnabled = true
                inputAdditionalAddressInformation.isEnabled = true
                inputAddressInformation.hint = "Brgy/Street Name/City"
                inputAdditionalAddressInformation.hint = "Additional Address Information (Optional)"
            }
        }

        // open OrderSuccessActivity and automatically generate QR Code
        btnAddLaundry.setOnClickListener {
            if(totalItems == 0 ) {
                // show error message
                Toast.makeText(this@WetLaundryActivity, "Please select at least one item(s)!", Toast.LENGTH_SHORT).show()
            } else {
                // Check if a RadioButton is selected
                val detergentSelected = radioButtonDetergent.checkedRadioButtonId != -1
                val deliverySelected = radioButtonDeliveryOption.checkedRadioButtonId != 1

                val isDelivery = selectedRadioButtonDeliveryOption.equals("Delivery")
                val isPickup = selectedRadioButtonDeliveryOption.equals("Pickup")

                // checks if detergent and delivery option is selected
                if (detergentSelected && deliverySelected) {
                    // checks if delivery or pickup is selected
                    if (isDelivery) {
                        if(inputAddressInformation.text.toString().isNotEmpty()) {

                            addToLaundry()
                        } else {
                            // show error message
                            Toast.makeText(this@WetLaundryActivity, "Please provide your address information!", Toast.LENGTH_SHORT).show()
                        }
                    } else if (isPickup) {

                        addToLaundry()
                    } else {
                        // show error message
                        Toast.makeText(this@WetLaundryActivity, "Please select your delivery option!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // show error message
                    Toast.makeText(this@WetLaundryActivity, "Please complete all the fields!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun setInitLayout() {
        // initializations
        tvPriceShirts = findViewById(R.id.tvPriceShirts)
        tvPriceShorts = findViewById(R.id.tvPriceShorts)
        tvPriceUnderwears = findViewById(R.id.tvPriceUnderwears)
        tvPricePants = findViewById(R.id.tvPricePants)
        tvPriceJackets = findViewById(R.id.tvPriceJackets)
        tvPriceBedcovers = findViewById(R.id.tvPriceBedcovers)
        tvPriceCarpets = findViewById(R.id.tvPriceCarpets)

        imageAdd1 = findViewById(R.id.imageAdd1)
        imageAdd2 = findViewById(R.id.imageAdd2)
        imageAdd3 = findViewById(R.id.imageAdd3)
        imageAdd4 = findViewById(R.id.imageAdd4)
        imageAdd5 = findViewById(R.id.imageAdd5)
        imageAdd6 = findViewById(R.id.imageAdd6)
        imageAdd7 = findViewById(R.id.imageAdd7)
        imageMinus1 = findViewById(R.id.imageMinus1)
        imageMinus2 = findViewById(R.id.imageMinus2)
        imageMinus3 = findViewById(R.id.imageMinus3)
        imageMinus4 = findViewById(R.id.imageMinus4)
        imageMinus5 = findViewById(R.id.imageMinus5)
        imageMinus6 = findViewById(R.id.imageMinus6)
        imageMinus7 = findViewById(R.id.imageMinus7)

        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        tvItemCount = findViewById(R.id.tvItemCount)
        spWashInstruction = findViewById(R.id.spWashInstruction)
        spDryInstruction = findViewById(R.id.spDryInstruction)
        spPaymentMethod = findViewById(R.id.spPaymentMethod)
        radioButtonDetergent = findViewById(R.id.radioButtonDetergent)
        inputAdditionalDescription = findViewById(R.id.inputAdditionalDescription)
        radioButtonDeliveryOption = findViewById(R.id.radioButtonDeliveryOption)
        inputAddressInformation = findViewById(R.id.inputAddressInformation)
        inputAdditionalAddressInformation = findViewById(R.id.inputAdditionalAddressInformation)
        tvAddressInfo1 = findViewById(R.id.tvAddressInfo1)
        tvAddressInfo2 = findViewById(R.id.tvAddressInfo2)

        btnAddLaundry = findViewById(R.id.btnAddLaundry)
        ivQRCode = findViewById(R.id.ivQRCode)
        etStatus = findViewById(R.id.etStatus)
        etLaundryDate = findViewById(R.id.etLaundryDate)

        val calendar = Calendar.getInstance()
        // Define the format you want
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        // Format the date
        val formattedDate = dateFormat.format(calendar.time)


        tvItemCount.setText("O items")
        tvTotalPrice.setText("₱ 0.00")
        etStatus.setText("Pending")
        etLaundryDate.setText(formattedDate)
    }

    private fun setDataShirts() {
        imageAdd1.setOnClickListener {
            itemCountShirts += 1
            tvPriceShirts.text = itemCountShirts.toString()
            countShirts = priceShirts * itemCountShirts
            setTotalPrice()
        }

        imageMinus1.setOnClickListener {
            if(itemCountShirts > 0) {
                itemCountShirts -= 1
                tvPriceShirts.text = itemCountShirts.toString()
            }
            countShirts = priceShirts * itemCountShirts
            setTotalPrice()
        }
    }

    private fun setDataShorts() {
        imageAdd2.setOnClickListener {
            itemCountShorts += 1
            tvPriceShorts.text = itemCountShorts.toString()
            countShorts = priceShorts * itemCountShorts
            setTotalPrice()
        }

        imageMinus2.setOnClickListener {
            if(itemCountShorts > 0) {
                itemCountShorts -= 1
                tvPriceShorts.text = itemCountShorts.toString()
            }
            countShorts = priceShorts * itemCountShorts
            setTotalPrice()
        }
    }

    private fun setDataUnderwears() {
        imageAdd3.setOnClickListener {
            itemCountUnderWears += 1
            tvPriceUnderwears.text = itemCountUnderWears.toString()
            countUnderwears = priceUnderwears * itemCountUnderWears
            setTotalPrice()
        }

        imageMinus3.setOnClickListener {
            if(itemCountUnderWears > 0) {
                itemCountUnderWears -= 1
                tvPriceUnderwears.text = itemCountUnderWears.toString()
            }
            countUnderwears = priceUnderwears * itemCountUnderWears
            setTotalPrice()
        }
    }

    private fun setDataPants() {
        imageAdd4.setOnClickListener {
            itemCountPants += 1
            tvPricePants.text = itemCountPants.toString()
            countPants = pricePants * itemCountPants
            setTotalPrice()
        }

        imageMinus4.setOnClickListener {
            if(itemCountPants > 0) {
                itemCountPants -= 1
                tvPricePants.text = itemCountPants.toString()
            }
            countPants = pricePants * itemCountPants
            setTotalPrice()
        }
    }

    private fun setDataJackets() {
        imageAdd5.setOnClickListener {
            itemCountJackets += 1
            tvPriceJackets.text = itemCountJackets.toString()
            countJackets = priceJackets * itemCountJackets
            setTotalPrice()
        }

        imageMinus5.setOnClickListener {
            if(itemCountJackets > 0) {
                itemCountJackets -= 1
                tvPriceJackets.text = itemCountJackets.toString()
            }
            countJackets = priceJackets * itemCountJackets
            setTotalPrice()
        }
    }

    private fun setDataBedcovers() {
        imageAdd6.setOnClickListener {
            itemCountBedCovers += 1
            tvPriceBedcovers.text = itemCountBedCovers.toString()
            countBedcovers = priceBedscovers * itemCountBedCovers
            setTotalPrice()
        }

        imageMinus6.setOnClickListener {
            if(itemCountBedCovers > 0) {
                itemCountBedCovers -= 1
                tvPriceBedcovers.text = itemCountBedCovers.toString()
            }
            countBedcovers = priceBedscovers * itemCountBedCovers
            setTotalPrice()
        }
    }

    private fun setDataCarpets() {
        imageAdd7.setOnClickListener {
            itemCountCarpets += 1
            tvPriceCarpets.text = itemCountCarpets.toString()
            countCarpets = priceCarpets * itemCountCarpets
            setTotalPrice()
        }

        imageMinus7.setOnClickListener {
            if(itemCountCarpets > 0) {
                itemCountCarpets -= 1
                tvPriceCarpets.text = itemCountCarpets.toString()
            }
            countCarpets = priceCarpets * itemCountCarpets
            setTotalPrice()
        }
    }

    // function for calculating the total items and price
    private fun setTotalPrice() {
        totalItems = itemCountShirts + itemCountShorts + itemCountUnderWears + itemCountPants +
                itemCountJackets + itemCountBedCovers + itemCountCarpets

        totalPrice = countShirts + countShorts + countUnderwears + countPants +
                countJackets + countBedcovers + countCarpets

        val decimalFormat = DecimalFormat("₱ #,##0.00")
        val formattedTotalPrice = decimalFormat.format(totalPrice)

        tvItemCount.text = "$totalItems items"
        tvTotalPrice.text = formattedTotalPrice
    }

    private fun addToLaundry() {
        // Show progress dialog
        val progressDialog = showProgressDialog(this@WetLaundryActivity)

        val writer = QRCodeWriter()
        var laundryUserName = ""

        val database = FirebaseDatabase.getInstance()

        // get a reference to the "Bookings" node
        val bookingsRef = database.getReference("Bookings")
        val usersRef = database.getReference("Users").child(uid)

        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val firstName = snapshot.child("firstname").getValue(String::class.java)
                    val lastName = snapshot.child("lastname").getValue(String::class.java)

                    laundryUserName = "$firstName $lastName"
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle errors here
                println("Database error: $error")
            }
        })

        // Specify the type of wash (e.g., "WetWash")
        val laundryType = "Wet Washing"

        // creates unique laundry reference number
        val laundryReferenceNo = "LABADA-" + (1000000000000..9999999999999).random().toString()

        try {
            // Encode the QR code
            val bitMatrix = writer.encode(laundryReferenceNo, BarcodeFormat.QR_CODE, 512, 512)

            // Create a Bitmap
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            // Create a Canvas to draw on the Bitmap
            val canvas = Canvas(bmp)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                }
            }

            // Draw text on top of the QR code
            val paint = Paint().apply {
                color = Color.BLACK
                textSize = 30f // Adjust the text size as needed
            }

            canvas.drawText(laundryReferenceNo, 80f, 50f, paint)

            ivQRCode.setImageBitmap(bmp)

            // Convert Bitmap to ByteArray
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            // Upload the image to Firebase Storage
            val qrCodeRef = storageRef.child("qr_codes/$laundryReferenceNo.png")
            val decimalFormat = DecimalFormat("₱ #,##0.00")

            qrCodeRef.putBytes(byteArray)
                .addOnSuccessListener {

                    qrCodeRef.downloadUrl.addOnSuccessListener { uri ->
                        // Create an instance for DataClassWetLaundry
                        val newBookingDetails = DataClassWetLaundry(
                            userID = uid,
                            laundryType = laundryType,
                            itemCountShirts = itemCountShirts.toString(),
                            itemCountShorts = itemCountShorts.toString(),
                            itemCountUnderwears = itemCountUnderWears.toString(),
                            itemCountPants = itemCountPants.toString(),
                            itemCountJackets = itemCountJackets.toString(),
                            itemCountBedCovers = itemCountBedCovers.toString(),
                            itemCountCarpets = itemCountCarpets.toString(),
                            countShirts = countShirts.toString(),
                            countShorts = countShorts.toString(),
                            countUnderwears = countUnderwears.toString(),
                            countPants = countPants.toString(),
                            countJackets = countJackets.toString(),
                            countBedCovers = countBedcovers.toString(),
                            countCarpets = countCarpets.toString(),
                            laundryReferenceNo = laundryReferenceNo,
                            spWashInstruction = selectedSpWashInstruction,
                            spDryInstruction = selectedSpDryInstruction,
                            radioButtonDetergent = selectedRadioButtonDetergent,
                            inputAdditionalDescription = inputAdditionalDescription.text.toString(),
                            radioButtonDeliveryOption = selectedRadioButtonDeliveryOption,
                            inputAddressInformation = inputAddressInformation.text.toString(),
                            inputAdditionalAddressInformation = inputAdditionalAddressInformation.text.toString(),
                            spPaymentMethod = selectedSpPaymentMethod,
                            totalPrice = decimalFormat.format(totalPrice).toString(),
                            totalItems = totalItems.toString(),
                            etStatus = etStatus.text.toString(),
                            etLaundryDate = etLaundryDate.text.toString(),
                            qrCode = uri.toString(),
                            laundryUserName = laundryUserName
                        )

                        // Set the value using your DataClassWetLaundry instance and the laundryReferenceNo as the key
                        bookingsRef.child(laundryType).child(laundryReferenceNo).setValue(newBookingDetails)
                            .addOnSuccessListener {
                                // Data successfully written
                                progressDialog.dismiss()

                            }
                            .addOnFailureListener { e ->
                                // Handle any errors
                                progressDialog.dismiss()
                                Toast.makeText(this, "Booking failed: $e", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    // Handle unsuccessful uploads
                    progressDialog.dismiss()
                    Toast.makeText(this, "Upload failed: $e", Toast.LENGTH_SHORT).show()
                }

        } catch (e: WriterException) {
            e.printStackTrace()
        }

        // Start OrderSuccessActivity with the QR code data
        val intent = Intent(this, LaundrySuccessActivity::class.java)
        startActivity(intent)
    }

    fun showProgressDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Please wait while we process your request")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        return progressDialog
    }
}
