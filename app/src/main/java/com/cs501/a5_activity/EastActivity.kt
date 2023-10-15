package com.cs501.a5_activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EastActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_east)

        val activityNameText: TextView = findViewById(R.id.activityNameText)
        val activityImage: ImageView = findViewById(R.id.activityImage)

        activityNameText.text = "Current Activity: East"
        activityImage.setImageResource(R.drawable.east_image)
    }
}