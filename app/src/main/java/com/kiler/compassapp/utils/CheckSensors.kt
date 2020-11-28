package com.kiler.compassapp.utils

import android.hardware.Sensor
import android.hardware.SensorManager

fun CheckSensors(sensorManager: SensorManager): Boolean {

    val sensorsValid: Boolean


    if (sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null){
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null
            || sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null){
            sensorsValid = false
        } else {
            sensorsValid = true
        }
    } else {

        sensorsValid = true

    }

    return sensorsValid

}


