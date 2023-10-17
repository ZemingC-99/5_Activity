/**
 * References:
 * 1.https://stackoverflow.com/questions/48344247/how-can-i-shake-an-android-emulator-device-in-android-studio-3-0
 * 2.https://www.geeksforgeeks.org/how-to-detect-shake-event-in-android/
 * 3.https://stackoverflow.com/questions/49862357/how-do-i-get-the-current-time-as-a-timestamp-in-kotlin
 * 4.https://developer.android.com/reference/android/hardware/SensorEventListener
 * 5.https://www.callicoder.com/kotlin-abstract-classes/
 * 6.https://stackoverflow.com/questions/9448732/shaking-wobble-view-animation-in-android
 * 7.https://stackoverflow.com/questions/14680338/how-can-i-make-vibrate-animation-for-imageview
 */

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
import android.widget.Toast
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
    private var lastShakeTimestamp: Long = 0
    private val shakeCooldown = 3000
    private var toastShown = false

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
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (e1 == null) return false

        val diffX = e2.x - e1.x
        val diffY = e2.y - e1.y

        if (abs(diffX) > abs(diffY)) {
            if (diffX > 0) {
                // Right
                startActivity(Intent(this, EastActivity::class.java))
            } else {
                // Left
                startActivity(Intent(this, WestActivity::class.java))
            }
        } else {
            if (diffY < 0) {
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
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastShakeTimestamp > shakeCooldown) {
            toastShown = false
        }
        if (!toastShown) {
            Toast.makeText(this, "Please shake in each activity", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "Not Here !!", Toast.LENGTH_SHORT).show()
            toastShown = true
        }

        lastShakeTimestamp = currentTime
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