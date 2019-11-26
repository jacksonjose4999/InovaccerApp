package com.example.innovaccerapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_visitor_details.*

class HostDetails : AppCompatActivity() {

    companion object{
        var hostDetails = HostInfo("", "","")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        submit_button.setOnClickListener {
            if (name_text_view.text.isNullOrBlank() || email_text.text.isNullOrBlank() ||
                phone_text.text.isNullOrBlank()
            ) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                hostDetails.name = name_text_view.text.toString()
                hostDetails.email = email_text.text.toString()
                hostDetails.phone = phone_text.text.toString()

                finish()
                startActivity(Intent(this, VisitorDetails::class.java))
            }
        }
        cancel_button_host.setOnClickListener {
            finish()
        }
    }
}
