package com.cs501.a5_activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_west)

        val activityNameText: TextView = findViewById(R.id.activityNameText)
        val activityImage: ImageView = findViewById(R.id.activityImage)

        activityNameText.text = "Current Activity: West"
        activityImage.setImageResource(R.drawable.west_image)
    }
}