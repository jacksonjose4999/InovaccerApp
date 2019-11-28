package com.example.innovaccerapplication

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_visitor_details.*


import android.R.attr.data

import android.util.Base64
import com.squareup.okhttp.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.http.*




class VisitorDetails : AppCompatActivity() {

    private val TAG = "PermissionDemo"
    var textMsg = ""

    val ACCOUNT_SID = "AC45ca690417d992beb2a3b1a899af9fc5"
    val AUTH_TOKEN = "be80f489f857304c74b9aa2a98ec7da5"


    private fun sendMessage( msg: String, phone: String) {
        val body = msg
        val from = "+17089985264"
        val to = "+91"+phone

        val base64EncodedCredentials = "Basic " + android.util.Base64.encodeToString(
            (ACCOUNT_SID + ":" + AUTH_TOKEN).toByteArray(), android.util.Base64.NO_WRAP
        )

        val smsData = hashMapOf<String, String>()
        smsData.put("From", from)
        smsData.put("To", to)
        smsData.put("Body", body)



        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.twilio.com/2010-04-01/")
            .build()
        val api = retrofit.create(TwilioApi::class.java)

        api.sendMessage(ACCOUNT_SID, base64EncodedCredentials, smsData)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>?, response: retrofit2.Response<ResponseBody>?
                ) {
                    if (response != null) {
                        if (response.isSuccessful())
                            Log.wtf("sdff", "onResponse->success")
                        else
                            Log.wtf("sdff", "onResponse->failure")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.wtf("sdff", "${t.localizedMessage}")
                }
            })

        Log.wtf("sdff", "Message Sent")
    }

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

                    sms.sendTextMessage("${HostDetails.hostDetails.phone}",null, textMsg, pi, null )

                }
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visitor_details)

        submit_button_visitor.setOnClickListener {
            if (name_text_view_visitor.text.isNullOrBlank() || email_text_visitor.text.isNullOrBlank() ||
                phone_text_visitor.text.isNullOrBlank()){
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else {
                var visitorInfo = VisitorInfo(name_text_view_visitor.text.toString(), email_text_visitor.text.toString(),
                    phone_text_visitor.text.toString(), Timestamp.now())

                var db = FirebaseFirestore.getInstance()

                var idVisitor = db.collection("Visitors").document().id
                db.collection("Visitors/").document(idVisitor)
                    .set(visitorInfo)

                var idHost = db.collection("Visitors/$idVisitor/Hosts").document().id
                db.collection("Visitors/${idVisitor}/Host").document(idHost)
                    .set(HostDetails.hostDetails)

                textMsg = "You have a new visitor\nName: ${visitorInfo.name}\nPhone Number: ${visitorInfo.phone}" +
                        "\nEmail: ${visitorInfo.email}"


                Thread(Runnable {
                    try{
                        var sender = GMailSender("snumess@gmail.com","Qwerty!@#$1234")
                        sender.sendMail("Visitor Details", "You have a new visitor\nName: ${visitorInfo.name}\nPhone Number: ${visitorInfo.phone}" +
                                "\nEmail: ${visitorInfo.email}", "snumess@gmail.com" , HostDetails.hostDetails.email)

                        Log.wtf("Email", "Email sent")
                    }
                    catch (e : Exception){
                        Log.wtf("Email",e.toString())
                    }
                }).start()


                sendMessage(textMsg, HostDetails.hostDetails.phone)

                finish()
                startActivity(Intent(this, CheckInCheckOut::class.java))

            }
        }

        cancel_button_visitor.setOnClickListener {
            finish()
            startActivity(Intent(this, CheckInCheckOut::class.java))
        }
    }

}



