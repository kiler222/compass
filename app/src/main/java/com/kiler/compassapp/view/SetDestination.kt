package com.kiler.compassapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.RxView
import com.kiler.compassapp.R
import com.kiler.compassapp.data.LocationData
import kotlinx.android.synthetic.main.activity_set_destination.*
import java.util.concurrent.TimeUnit


class SetDestination : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_destination)


        submitButton.setSafeOnClickListener {

            hideKeyboard()

            val lat = latField.text.toString().takeUnless { it.isEmpty() } ?: ""
            val long = longField.text.toString().takeUnless { it.isEmpty() } ?: ""

            when (validateCoordinates(lat, long)) {
                0 -> {
                    Toast.makeText(this, getString(R.string.input_error), Toast.LENGTH_LONG).show()
                }
                1 -> {
                    Toast.makeText(this, getString(R.string.lat_error), Toast.LENGTH_LONG).show()
                }
                2 -> {
                    Toast.makeText(this, getString(R.string.long_error), Toast.LENGTH_LONG).show()
                }
                3 -> {
                    val resultIntent = Intent()

                    val destinationData = LocationData(long.toDouble(), lat.toDouble())
                    val bundle = Bundle()
                    bundle.putParcelable("destinationData", destinationData)
                    resultIntent.putExtra("destinationData", bundle)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            }

        }

    }


    fun validateCoordinates(lat: String, long: String): Int {

        var isCorrectLat = false
        var isCorrectLong = false
        var result = 0
        val latitude: Float
        val longitude: Float

        if (lat == "" || long == "") {
            result = 0
        } else if (lat != "" && long != "") {

            latitude = lat.toFloat()
            longitude = long.toFloat()

            if ( latitude < 90.0f && -90.0f < latitude ) {
                isCorrectLat = true
            } else {
                isCorrectLat = false
                result = 1

            }

            if ( -180.0f < longitude && longitude < 180.0f) {
                isCorrectLong = true
            } else {
                result = 2
                isCorrectLong = false
            }

        }

        if (isCorrectLat && isCorrectLong) result = 3

        return result

    }



    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

    }


    @SuppressLint("CheckResult")
    fun View.setSafeOnClickListener(onClick: (View) -> Unit) {
        RxView.clicks(this).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe {
            onClick(this)
        }
    }

    override fun onBackPressed() {
        finish()
    }

}