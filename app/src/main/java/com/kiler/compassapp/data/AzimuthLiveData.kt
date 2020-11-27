package com.kiler.compassapp.data

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData


class AzimuthLiveData(context: Context) : LiveData<AzimuthData>(), SensorEventListener {



    private val mSensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magneticField: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val mAccelerometerReading = FloatArray(3)
    private val mMagnetometerReading = FloatArray(3)

    private val mRotationMatrix = FloatArray(9)
    private val mOrientationAngles = FloatArray(3)

    private var rotationMatrix = FloatArray(9)
    private val orientation = FloatArray(3)
    private var azimuth: Float = 0.0f
    private var lastAccelerometer = FloatArray(3)
    private var lastAccelerometerSet = false
    private var lastMagnetometer = FloatArray(3)
    private var lastMagnetometerSet = false


    override fun onActive() {
        super.onActive()

        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        mSensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_UI)

    }


    override fun onInactive() {
        super.onInactive()
        mSensorManager.unregisterListener(this)

    }

    override fun onSensorChanged(event: SensorEvent?) {

    //    Log.e("PJ eventy", "ev = ${event!!.sensor.name} - ${event!!.values[0]}, ${event!!.values[1]}, ${event!!.values.size}")

        if (event!!.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            azimuth = ((Math.toDegrees(
                SensorManager.getOrientation(
                    rotationMatrix,
                    orientation
                )[0].toDouble()
            )+360).toInt()%360).toFloat()
        }

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER){
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.size)
            lastAccelerometerSet = true

        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD){
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.size)
            lastMagnetometerSet = true

        }

   //     Log.e("PJ eventy", "acc = $lastAccelerometerSet, mag = $lastMagnetometerSet")

        if (lastAccelerometerSet && lastMagnetometerSet){
            SensorManager.getRotationMatrix(
                rotationMatrix,
                null,
                lastAccelerometer,
                lastMagnetometer
            )



            SensorManager.getOrientation(rotationMatrix, orientation)
            azimuth = ((Math.toDegrees(
                SensorManager.getOrientation(
                    rotationMatrix,
                    orientation
                )[0].toDouble()
            )+360).toInt()%360).toFloat()
//            val x = SensorManager.getOrientation(
//                rotationMatrix,
//                orientation
//            )
//            println("orientation")
//            x.forEach { print("$it, ") }
//            println()


//            Log.e("PJ azymuty", "azymut = $azimuth")

        }

//        azimuth = Math.round(azimuth!!.toFloat())


        value = AzimuthData(azimuth)

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    private fun updateOrientationAngles() {
        SensorManager.getRotationMatrix(mRotationMatrix, null, mAccelerometerReading, mMagnetometerReading)
        SensorManager.getOrientation(mRotationMatrix, mOrientationAngles)
    }


}