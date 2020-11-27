package com.kiler.compassapp.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AlertDialog

fun CheckSensors(sensorManager: SensorManager): Boolean {

    var sensorsValid: Boolean

    if (sensorManager == null) return false

    if (sensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null){
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null
            || sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null){
            sensorsValid = false
        } else {
            sensorsValid = true
        }
    } else {

        sensorsValid = true

    }

    return sensorsValid

}


