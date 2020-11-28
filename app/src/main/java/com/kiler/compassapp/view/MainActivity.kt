package com.kiler.compassapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxbinding2.view.RxView
import com.kiler.compassapp.R
import com.kiler.compassapp.data.LocationData
import com.kiler.compassapp.utils.CheckSensors
import com.kiler.compassapp.viewmodel.LocationViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.destinationButton
import kotlinx.android.synthetic.main.activity_main.textView
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var locationViewModel: LocationViewModel
    private val REQUEST_LOCATION = 200
    private var destinationLatitude: Double? = null
    private var destinationLongitude: Double? = null
    private var azimuth: Float? = null
    private var sensorManager: SensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if (CheckSensors(sensorManager!!)) {
            locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
            checkPermisionsAndUpdateLocation()
        } else {
            noSensorAlert()
        }


        destinationButton.setSafeOnClickListener {

            val intent = Intent(this, SetDestination::class.java)
            startActivityForResult(intent, 0)

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> {
                if (resultCode == RESULT_OK) {

                    val bundle = data?.getBundleExtra("destinationData")
                    val destinationData = bundle?.getParcelable<LocationData>("destinationData")
                    destinationLatitude = destinationData?.latitude ?: 0.0
                    destinationLongitude = destinationData?.longitude ?: 0.0

                }
            }
        }
    }


    @SuppressLint("CheckResult")
    fun View.setSafeOnClickListener(onClick: (View) -> Unit) {
        RxView.clicks(this).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe {
            onClick(this)
        }
    }



    private fun startLocationUpdate() {
        locationViewModel.getLocationData().observe(this, Observer {

            if (destinationLatitude == null || destinationLongitude == null) {
                textView.text = getString(R.string.destination_not_set)
            } else {
                updateDistance(it.latitude, it.longitude)
                imageArrow.visibility = View.VISIBLE
            }
        })

        locationViewModel.getAzimuthData().observe( this, {

            imageCompass.rotation = -(it.value)
            if (azimuth != null) {
                imageArrow.rotation = -(it.value) + azimuth!!
            }
        })

    }

    private fun updateDistance(latitude: Double, longitude: Double) {
        val result = FloatArray(3)

        Location.distanceBetween(
            latitude,
            longitude,
            destinationLatitude!!,
            destinationLongitude!!,
            result
        )

        val d = result[0].toInt()

        val distance = if (d < 10000 ){
            " $d m"
        } else {
            " ${d/1000} km"
        }

        textView.text = getString(R.string.distance_text, distance)
        azimuth = result[1]

    }


    private fun checkPermisionsAndUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_LOCATION
            )

        } else {

            startLocationUpdate()

        }


    }

    private fun noSensorAlert() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage(getString(R.string.device_no_compass))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.close)){ _, _ -> finish()}
        alertDialog.show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION) checkPermisionsAndUpdateLocation()
    }

}