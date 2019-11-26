package com.example.innovaccerapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_check_in_check_out.*


class CheckInCheckOut : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in_check_out)

        check_in_button.setOnClickListener {
            startActivity(Intent(this, HostDetails::class.java))
        }

        check_out_button.setOnClickListener {
            startActivity(Intent(this, CheckOutActivity::class.java))
        }
    }
}


