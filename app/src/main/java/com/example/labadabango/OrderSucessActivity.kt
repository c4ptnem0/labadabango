package com.example.labadabango

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class OrderSucessActivity : AppCompatActivity() {

    private lateinit var ivQRCodeSuccess: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_sucess)

        ivQRCodeSuccess = findViewById(R.id.ivQRCodeSuccess)

        val byteArray = intent.getByteArrayExtra("QR_CODE")
        if (byteArray != null) {
            val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            ivQRCodeSuccess.setImageBitmap(bmp)
        }

    }
}