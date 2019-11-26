package com.example.innovaccerapplication

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_check_in_check_out.*
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class CheckInCheckOut : AppCompatActivity() {
    private val TAG = ""
    var textMsg = ""


    private val RECORD_REQUEST_CODE = 101
    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.SEND_SMS)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.SEND_SMS),
            RECORD_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                    val sms = SmsManager.getDefault()
                    val pi = PendingIntent.getActivity(
                        applicationContext, 0, intent, 0
                    )

                    sms.sendTextMessage("Hi Test",null, textMsg, pi, null )
                    Log.wtf("SMSSENT","message sent")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in_check_out)

        setupPermissions()

        check_in_button.setOnClickListener {
            startActivity(Intent(this, HostDetails::class.java))
        }

        check_out_button.setOnClickListener {
            startActivity(Intent(this, CheckOutActivity::class.java))
        }
    }
}


