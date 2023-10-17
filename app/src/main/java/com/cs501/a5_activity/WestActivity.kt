package com.cs501.a5_activity

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WestActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager
    private val shakeThreshold = 600.0f
    private var lastUpdate: Long = 0
    private var lastX = 0.0f
    private var lastY = 0.0f
    private var lastZ = 0.0f
    private var lastShakeTimestamp: Long = 0
    private val shakeCooldown = 3000
    private var toastShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_west)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val activityNameText: TextView = findViewById(R.id.activityNameText)
        val activityImage: ImageView = findViewById(R.id.activityImage)

        activityNameText.text = "Current Activity: West"
        activityImage.setImageResource(R.drawable.west_image)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorListener)
    }

    private val sensorListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val currentTime = System.currentTimeMillis()
                if ((currentTime - lastUpdate) > 100) {
                    val timeDiff = currentTime - lastUpdate
                    lastUpdate = currentTime
                    val shakeSpeed = Math.abs(x + y + z - lastX - lastY - lastZ) / timeDiff * 10000
                    if (shakeSpeed > shakeThreshold) {
                        // Shake detected
                        shakeImage()
                    }
                    lastX = x
                    lastY = y
                    lastZ = z
                }
            }
        }
    }

    private fun shakeImage() {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastShakeTimestamp > shakeCooldown) {
            toastShown = false
        }

        if (!toastShown) {
            showToast("Ohhhh, it's shaking!!!!")
            toastShown = true
        }

        lastShakeTimestamp = currentTime

        animateImage(R.id.activityImage, R.anim.shake_animation)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun animateImage(viewId: Int, animationId: Int) {
        val shake: Animation = AnimationUtils.loadAnimation(this, animationId)
        findViewById<ImageView>(viewId).startAnimation(shake)
    }
}