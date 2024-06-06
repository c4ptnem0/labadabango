package com.example.labadabango

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
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
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class IroningLaundryActivity : AppCompatActivity() {

    // declaration
    private lateinit var tvPriceShirts: TextView
    private lateinit var tvPriceShorts: TextView
    private lateinit var tvPriceCoats: TextView
    private lateinit var tvPriceGowns: TextView
    private lateinit var tvPriceCurtains: TextView
    private lateinit var tvPriceHats: TextView
    private lateinit var tvPriceShoes: TextView
    private lateinit var tvPriceLeatherBags: TextView
    private lateinit var tvPriceBedComforters: TextView
    private lateinit var tvPriceStuffedToys: TextView
    private lateinit var tvPriceWoolSweaters: TextView
    private lateinit var tvPriceDenimJackets: TextView
    private lateinit var tvPriceDenimPants: TextView
    private lateinit var tvPriceLeatherJackets: TextView

    private lateinit var imageAdd1: ImageView
    private lateinit var imageAdd2: ImageView
    private lateinit var imageAdd3: ImageView
    private lateinit var imageAdd4: ImageView
    private lateinit var imageAdd5: ImageView
    private lateinit var imageAdd6: ImageView
    private lateinit var imageAdd7: ImageView
    private lateinit var imageAdd8: ImageView
    private lateinit var imageAdd9: ImageView
    private lateinit var imageAdd10: ImageView
    private lateinit var imageAdd11: ImageView
    private lateinit var imageAdd12: ImageView
    private lateinit var imageAdd13: ImageView
    private lateinit var imageAdd14: ImageView
    private lateinit var imageMinus1: ImageView
    private lateinit var imageMinus2: ImageView
    private lateinit var imageMinus3: ImageView
    private lateinit var imageMinus4: ImageView
    private lateinit var imageMinus5: ImageView
    private lateinit var imageMinus6: ImageView
    private lateinit var imageMinus7: ImageView
    private lateinit var imageMinus8: ImageView
    private lateinit var imageMinus9: ImageView
    private lateinit var imageMinus10: ImageView
    private lateinit var imageMinus11: ImageView
    private lateinit var imageMinus12: ImageView
    private lateinit var imageMinus13: ImageView
    private lateinit var imageMinus14: ImageView

    private lateinit var tvTotalPrice: TextView
    private lateinit var tvItemCount: TextView
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

    var priceShirts = 3.00
    var priceShorts = 3.00
    var priceGowns = 90.00
    var priceCoats = 50.00
    var priceCurtains = 40.00
    var priceHats = 5.00
    var priceShoes = 25.00
    var priceLeatherBags = 30.00
    var priceBedComforters = 30.00
    var priceStuffedToys = 10.00
    var priceWoolSweaters = 25.00
    var priceDenimJackets = 15.00
    var priceDenimPants = 25.00
    var priceLeatherJackets = 25.00

    var itemCountShirts = 0
    var itemCountShorts = 0
    var itemCountGowns = 0
    var itemCountCoats = 0
    var itemCountCurtains = 0
    var itemCountHats = 0
    var itemCountShoes = 0
    var itemCountLeatherBags = 0
    var itemCountBedComforters = 0
    var itemCountStuffedToys = 0
    var itemCountWoolSweaters = 0
    var itemCountDenimJackets = 0
    var itemCountDenimPants = 0
    var itemCountLeatherJackets = 0

    var totalPrice = 0.0
    var totalItems = 0

    var countShirts: Double = 0.0
    var countShorts: Double = 0.0
    var countGowns: Double = 0.0
    var countCoats: Double = 0.0
    var countCurtains: Double = 0.0
    var countHats: Double = 0.0
    var countShoes: Double = 0.0
    var countLeatherBags: Double = 0.0
    var countBedComforters: Double = 0.0
    var countStuffedToys: Double = 0.0
    var countWoolSweaters: Double = 0.0
    var countDenimJackets: Double = 0.0
    var countDenimPants: Double = 0.0
    var countLeatherJackets: Double = 0.0

    var selectedRadioButtonDeliveryOption: String = ""
    var selectedSpPaymentMethod: String = ""

    // get the current user's ID
    val uid = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ironing_laundry)

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
        setDataGowns()
        setDataCoats()
        setDataCurtains()
        setDataHats()
        setDataShoes()
        setDataLeatherBags()
        setDataBedComforters()
        setDataStuffedToys()
        setDataWoolSweaters()
        setDataDenimJackets()
        setDataDenimPants()
        setDataLeatherJackets()

        spPaymentMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Get the selected item from the spinner
                selectedSpPaymentMethod = parentView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do something when nothing is selected
            }
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
            // Check if a RadioButton is selected
            val deliverySelected = radioButtonDeliveryOption.checkedRadioButtonId != 1

            val isDelivery = selectedRadioButtonDeliveryOption.equals("Delivery")
            val isPickup = selectedRadioButtonDeliveryOption.equals("Pickup")

            if(totalItems == 0) {
                // show error message
                Toast.makeText(this@IroningLaundryActivity, "Please select at least one item(s)!", Toast.LENGTH_SHORT).show()
            } else {
                // checks if detergent and delivery option is selected
                if (deliverySelected) {
                    // checks if delivery or pickup is selected
                    if (isDelivery) {
                        if(inputAddressInformation.text.toString().isNotEmpty()) {

                            addToLaundry()
                        } else {
                            // show error message
                            Toast.makeText(this@IroningLaundryActivity, "Please provide your address information!", Toast.LENGTH_SHORT).show()
                        }
                    } else if (isPickup) {

                        addToLaundry()
                    } else {
                        // show error message
                        Toast.makeText(this@IroningLaundryActivity, "Please select your delivery option!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // show error message
                    Toast.makeText(this@IroningLaundryActivity, "Please complete all the fields!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setInitLayout() {
        // initializations
        tvPriceShirts = findViewById(R.id.tvPriceShirts)
        tvPriceShorts = findViewById(R.id.tvPriceShorts)
        tvPriceGowns = findViewById(R.id.tvPriceGowns)
        tvPriceCoats = findViewById(R.id.tvPriceCoats)
        tvPriceCurtains = findViewById(R.id.tvPriceCurtains)
        tvPriceHats = findViewById(R.id.tvPriceHats)
        tvPriceShoes = findViewById(R.id.tvPriceShoes)
        tvPriceLeatherBags = findViewById(R.id.tvPriceLeatherBags)
        tvPriceBedComforters = findViewById(R.id.tvPriceBedComforters)
        tvPriceStuffedToys = findViewById(R.id.tvPriceStuffedToys)
        tvPriceWoolSweaters = findViewById(R.id.tvPriceWoolSweaters)
        tvPriceDenimJackets = findViewById(R.id.tvPriceDenimJackets)
        tvPriceDenimPants = findViewById(R.id.tvPriceDenimPants)
        tvPriceLeatherJackets = findViewById(R.id.tvPriceLeatherJackets)

        imageAdd1 = findViewById(R.id.imageAdd1)
        imageAdd2 = findViewById(R.id.imageAdd2)
        imageAdd3 = findViewById(R.id.imageAdd3)
        imageAdd4 = findViewById(R.id.imageAdd4)
        imageAdd5 = findViewById(R.id.imageAdd5)
        imageAdd6 = findViewById(R.id.imageAdd6)
        imageAdd7 = findViewById(R.id.imageAdd7)
        imageAdd8 = findViewById(R.id.imageAdd8)
        imageAdd9 = findViewById(R.id.imageAdd9)
        imageAdd10 = findViewById(R.id.imageAdd10)
        imageAdd11 = findViewById(R.id.imageAdd11)
        imageAdd12 = findViewById(R.id.imageAdd12)
        imageAdd13 = findViewById(R.id.imageAdd13)
        imageAdd14 = findViewById(R.id.imageAdd14)
        imageMinus1 = findViewById(R.id.imageMinus1)
        imageMinus2 = findViewById(R.id.imageMinus2)
        imageMinus3 = findViewById(R.id.imageMinus3)
        imageMinus4 = findViewById(R.id.imageMinus4)
        imageMinus5 = findViewById(R.id.imageMinus5)
        imageMinus6 = findViewById(R.id.imageMinus6)
        imageMinus7 = findViewById(R.id.imageMinus7)
        imageMinus8 = findViewById(R.id.imageMinus8)
        imageMinus9 = findViewById(R.id.imageMinus9)
        imageMinus10 = findViewById(R.id.imageMinus10)
        imageMinus11 = findViewById(R.id.imageMinus11)
        imageMinus12 = findViewById(R.id.imageMinus12)
        imageMinus13 = findViewById(R.id.imageMinus13)
        imageMinus14 = findViewById(R.id.imageMinus14)

        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        tvItemCount = findViewById(R.id.tvItemCount)
        spPaymentMethod = findViewById(R.id.spPaymentMethod)
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

    private fun setDataGowns() {
        imageAdd3.setOnClickListener {
            itemCountGowns += 1
            tvPriceGowns.text = itemCountGowns.toString()
            countGowns = priceGowns * itemCountGowns
            setTotalPrice()
        }

        imageMinus3.setOnClickListener {
            if(itemCountGowns > 0) {
                itemCountGowns -= 1
                tvPriceGowns.text = itemCountGowns.toString()
            }
            countGowns = priceGowns * itemCountGowns
            setTotalPrice()
        }
    }

    private fun setDataCoats() {
        imageAdd4.setOnClickListener {
            itemCountCoats += 1
            tvPriceCoats.text = itemCountCoats.toString()
            countCoats = priceCoats * itemCountCoats
            setTotalPrice()
        }

        imageMinus4.setOnClickListener {
            if(itemCountCoats > 0) {
                itemCountCoats -= 1
                tvPriceCoats.text = itemCountCoats.toString()
            }
            countCoats = priceCoats * itemCountCoats
            setTotalPrice()
        }
    }

    private fun setDataCurtains() {
        imageAdd5.setOnClickListener {
            itemCountCurtains += 1
            tvPriceCurtains.text = itemCountCurtains.toString()
            countCurtains = priceCurtains * itemCountCurtains
            setTotalPrice()
        }

        imageMinus5.setOnClickListener {
            if(itemCountCurtains > 0) {
                itemCountCurtains -= 1
                tvPriceCurtains.text = itemCountCurtains.toString()
            }
            countCurtains = priceCurtains * itemCountCurtains
            setTotalPrice()
        }

    }

    private fun setDataHats() {
        imageAdd6.setOnClickListener {
            itemCountHats += 1
            tvPriceHats.text = itemCountHats.toString()
            countHats = priceHats * itemCountHats
            setTotalPrice()
        }

        imageMinus6.setOnClickListener {
            if(itemCountHats > 0) {
                itemCountHats -= 1
                tvPriceHats.text = itemCountHats.toString()
            }
            countHats = priceHats * itemCountHats
            setTotalPrice()
        }
    }

    private fun setDataShoes() {
        imageAdd7.setOnClickListener {
            itemCountShoes += 1
            tvPriceShoes.text = itemCountShoes.toString()
            countShoes = priceShoes * itemCountShoes
            setTotalPrice()
        }

        imageMinus7.setOnClickListener {
            if(itemCountShoes > 0) {
                itemCountShoes -= 1
                tvPriceShoes.text = itemCountShoes.toString()
            }
            countShoes = priceShoes * itemCountShoes
            setTotalPrice()
        }
    }

    private fun setDataLeatherBags() {
        imageAdd8.setOnClickListener {
            itemCountLeatherBags += 1
            tvPriceLeatherBags.text = itemCountLeatherBags.toString()
            countLeatherBags = priceLeatherBags * itemCountLeatherBags
            setTotalPrice()
        }

        imageMinus8.setOnClickListener {
            if(itemCountLeatherBags > 0) {
                itemCountLeatherBags -= 1
                tvPriceLeatherBags.text = itemCountLeatherBags.toString()
            }
            countLeatherBags = priceLeatherBags * itemCountLeatherBags
            setTotalPrice()
        }
    }

    private fun setDataBedComforters() {
        imageAdd9.setOnClickListener {
            itemCountBedComforters += 1
            tvPriceBedComforters.text = itemCountBedComforters.toString()
            countBedComforters = priceBedComforters * itemCountBedComforters
            setTotalPrice()
        }

        imageMinus9.setOnClickListener {
            if(itemCountBedComforters > 0) {
                itemCountBedComforters -= 1
                tvPriceBedComforters.text = itemCountBedComforters.toString()
            }
            countBedComforters = priceBedComforters * itemCountBedComforters
            setTotalPrice()
        }
    }

    private fun setDataStuffedToys() {
        imageAdd10.setOnClickListener {
            itemCountStuffedToys += 1
            tvPriceStuffedToys.text = itemCountStuffedToys.toString()
            countStuffedToys = priceStuffedToys * itemCountStuffedToys
            setTotalPrice()
        }

        imageMinus10.setOnClickListener {
            if(itemCountStuffedToys > 0) {
                itemCountStuffedToys -= 1
                tvPriceStuffedToys.text = itemCountStuffedToys.toString()
            }
            countStuffedToys = priceStuffedToys * itemCountStuffedToys
            setTotalPrice()
        }
    }

    private fun setDataWoolSweaters() {
        imageAdd11.setOnClickListener {
            itemCountWoolSweaters += 1
            tvPriceWoolSweaters.text = itemCountWoolSweaters.toString()
            countWoolSweaters = priceWoolSweaters * itemCountWoolSweaters
            setTotalPrice()
        }

        imageMinus11.setOnClickListener {
            if(itemCountWoolSweaters > 0) {
                itemCountWoolSweaters -= 1
                tvPriceWoolSweaters.text = itemCountWoolSweaters.toString()
            }
            countWoolSweaters = priceWoolSweaters * itemCountWoolSweaters
            setTotalPrice()
        }
    }

    private fun setDataDenimJackets() {
        imageAdd12.setOnClickListener {
            itemCountDenimJackets += 1
            tvPriceDenimJackets.text = itemCountDenimJackets.toString()
            countDenimJackets = priceDenimJackets * itemCountDenimJackets
            setTotalPrice()
        }

        imageMinus12.setOnClickListener {
            if(itemCountDenimJackets > 0) {
                itemCountDenimJackets -= 1
                tvPriceDenimJackets.text = itemCountDenimJackets.toString()
            }
            countDenimJackets = priceDenimJackets * itemCountDenimJackets
            setTotalPrice()
        }
    }

    private fun setDataDenimPants() {
        imageAdd13.setOnClickListener {
            itemCountDenimPants += 1
            tvPriceDenimPants.text = itemCountDenimPants.toString()
            countDenimPants = priceDenimPants * itemCountDenimPants
            setTotalPrice()
        }

        imageMinus13.setOnClickListener {
            if(itemCountDenimPants > 0) {
                itemCountDenimPants -= 1
                tvPriceDenimPants.text = itemCountDenimPants.toString()
            }
            countDenimPants = priceDenimPants * itemCountDenimPants
            setTotalPrice()
        }
    }

    private fun setDataLeatherJackets() {
        imageAdd14.setOnClickListener {
            itemCountLeatherJackets += 1
            tvPriceLeatherJackets.text = itemCountLeatherJackets.toString()
            countLeatherJackets = priceLeatherJackets * itemCountLeatherJackets
            setTotalPrice()
        }

        imageMinus14.setOnClickListener {
            if(itemCountLeatherJackets > 0) {
                itemCountLeatherJackets -= 1
                tvPriceLeatherJackets.text = itemCountLeatherJackets.toString()
            }
            countLeatherJackets = priceLeatherJackets * itemCountLeatherJackets
            setTotalPrice()
        }
    }

    // function for calculating the total items and price
    private fun setTotalPrice() {
        totalItems = itemCountShirts + itemCountShorts + itemCountGowns + itemCountCoats +
                itemCountCurtains + itemCountHats + itemCountShoes + itemCountLeatherBags +
                itemCountBedComforters + itemCountStuffedToys + itemCountWoolSweaters +
                itemCountDenimJackets + itemCountDenimPants + itemCountLeatherJackets

        totalPrice = countShirts + countShorts + countGowns + countCoats +
                countCurtains + countHats + countShoes + countLeatherBags +
                countBedComforters + countStuffedToys + countWoolSweaters + countDenimJackets +
                countDenimPants + countLeatherJackets

        val decimalFormat = DecimalFormat("₱ #,##0.00")
        val formattedTotalPrice = decimalFormat.format(totalPrice)

        tvItemCount.text = "$totalItems items"
        tvTotalPrice.text = formattedTotalPrice
    }

    private fun addToLaundry() {
        // Show progress dialog
        val progressDialog = showProgressDialog(this@IroningLaundryActivity)

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
        val laundryType = "Ironing"

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
                        // Create an instance for DataClassIronLaundry
                        val newBookingDetails = DataClassIronLaundry(
                            userID = uid,
                            laundryType = laundryType,
                            itemCountShirts = itemCountShirts.toString(),
                            itemCountShorts = itemCountShorts.toString(),
                            itemCountGowns = itemCountGowns.toString(),
                            itemCountCoats = itemCountCoats.toString(),
                            itemCountCurtains = itemCountCurtains.toString(),
                            itemCountHats = itemCountHats.toString(),
                            itemCountShoes = itemCountShoes.toString(),
                            itemCountLeatherBags = itemCountLeatherBags.toString(),
                            itemCountBedComforters = itemCountBedComforters.toString(),
                            itemCountStuffedToys = itemCountStuffedToys.toString(),
                            itemCountWoolSweaters = itemCountWoolSweaters.toString(),
                            itemCountDenimJackets = itemCountDenimJackets.toString(),
                            itemCountDenimPants = itemCountDenimPants.toString(),
                            itemCountLeatherJackets = itemCountLeatherJackets.toString(),
                            countShirts = countShirts.toString(),
                            countShorts = countShorts.toString(),
                            countGowns = countGowns.toString(),
                            countCoats = countCoats.toString(),
                            countCurtains = countCurtains.toString(),
                            countHats = countHats.toString(),
                            countShoes = countShoes.toString(),
                            countLeatherBags = countLeatherBags.toString(),
                            countBedComforters = countBedComforters.toString(),
                            countStuffedToys = countStuffedToys.toString(),
                            countWoolSweaters = countWoolSweaters.toString(),
                            countDenimJackets = countDenimJackets.toString(),
                            countDenimPants = countDenimPants.toString(),
                            countLeatherJackets = countLeatherJackets.toString(),
                            laundryReferenceNo = laundryReferenceNo,
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