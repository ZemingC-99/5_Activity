package com.cs501.a5_activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NorthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_north)

        val activityNameText: TextView = findViewById(R.id.activityNameText)
        val activityImage: ImageView = findViewById(R.id.activityImage)

        activityNameText.text = "Current Activity: North"
        activityImage.setImageResource(R.drawable.north_image)
    }
}