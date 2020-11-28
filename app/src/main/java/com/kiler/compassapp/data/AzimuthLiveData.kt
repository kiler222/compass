package com.kiler.compassapp.data


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.LiveData


class AzimuthLiveData(context: Context) : LiveData<AzimuthData>(), SensorEventListener {

    private val mSensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magneticField: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

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
        }
        value = AzimuthData(azimuth)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }


}