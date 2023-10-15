package com.cs501.a5_activity

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

class HomeActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var gestureDetector: GestureDetector
    private lateinit var sensorManager: SensorManager

    // Shake detection
    private val shakeThreshold = 600.0f
    private var lastUpdate: Long = 0
    private var lastX = 0.0f
    private var lastY = 0.0f
    private var lastZ = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        gestureDetector = GestureDetector(this, this)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorListener)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { gestureDetector.onTouchEvent(it) }
        return super.onTouchEvent(event)
    }

    override fun onFling(
        e1: MotionEvent?,
        p1: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        val diffX = p1.x ?: (0f - e1?.x!!) ?: 0f
        val diffY = e1?.y ?: (0f - p1.y) ?: 0f

        if (abs(diffX) > abs(diffY)) {
            if (diffX > 0) {
                // Right
                startActivity(Intent(this, EastActivity::class.java))
            } else {
                // Left
                startActivity(Intent(this, WestActivity::class.java))
            }
        } else {
            if (diffY > 0) {
                // Upwards
                startActivity(Intent(this, NorthActivity::class.java))
            } else {
                // Downwards
                startActivity(Intent(this, SouthActivity::class.java))
            }
        }
        return true
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
        // Apply a shake animation to your ImageView here, this will last for 2 seconds
    }

    override fun onDown(p0: MotionEvent): Boolean = false
    override fun onShowPress(p0: MotionEvent) {}
    override fun onSingleTapUp(p0: MotionEvent): Boolean = false
    override fun onScroll(
        e1: MotionEvent?,
        p1: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean = false
    override fun onLongPress(p0: MotionEvent) {}
}