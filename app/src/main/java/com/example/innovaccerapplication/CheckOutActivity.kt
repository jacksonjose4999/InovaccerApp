package com.example.innovaccerapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_check_out.*
import java.util.*
import android.text.format.DateFormat

class CheckOutActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)
        var deleteId = ""

        checkout_submit.setOnClickListener {
            if (name_checkout.text.isNullOrBlank() || phone_checkout.text.isNullOrBlank() ){
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else {
                //These two fields act as a key value pair to identify a visitor uniquely
                var visitorName = name_checkout.text.toString() //To get the name of the visitor and store it in database
                var visitorPhone = phone_checkout.text.toString() //to store the phone numner of the visitors in the database


                var db = FirebaseFirestore.getInstance()

                db.collection("Visitors")
                    .get()
                    .addOnSuccessListener { result->
                        for (document in result) {
                            if (document.getString("name").toString() == visitorName){
                                deleteId = document.id

                                Log.wtf("Email", "string found equal")


                                var email  = document.getString("email").toString()
                                Thread(Runnable {
                                    try{
                                        var timestamp = document.getTimestamp("timestamp")

                                        var cal = Calendar.getInstance(Locale.ENGLISH)
                                        if (timestamp != null) {
                                            cal.setTimeInMillis(timestamp.seconds*1000)
                                        }
                                        val date =
                                            DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString()

                                        var timestampNow = Timestamp.now()
                                        cal.setTimeInMillis(timestampNow.seconds*1000)
                                        var dateNow = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString()

                                        var hostName = ""
                                        var dbHost = FirebaseFirestore.getInstance()
                                        dbHost.collection("Visitors/${document.id}/Host").get().addOnSuccessListener {
                                                result->for (doc in result){
                                            hostName = doc.getString("name").toString()
                                            Log.wtf("Email",hostName)

                                            Thread(Runnable {
                                                var sender = GMailSender("snumess@gmail.com","Qwerty!@#$1234")
                                                sender.sendMail("Your visit details", "Thank you for visiting.\nName: ${visitorName} \nHost Name:${hostName}\nPhone Number: ${visitorPhone}" +
                                                        "\nEmail: ${document.getString("email")}\nCheck In Time: ${date}" +
                                                        "\nCheck Out Time:${dateNow}" +
                                                        " ", "snumess@gmail.com" , email)

                                                Log.wtf("Email", "Email sent from CheckOut")

                                            }).start()
                                        }
                                        }



                                    }
                                    catch (e : Exception){
                                        Log.wtf("Email",e.toString())
                                    }
                                    finally {                db.collection("Visitors").document(deleteId).delete()

                                    }
                                }).start()
                            }
                        }

                    }



                startActivity(Intent(this, CheckInCheckOut::class.java))
                Toast.makeText(this, "Thank You For Your Visit", Toast.LENGTH_LONG).show()
            }
        }

        cancel_button_checkout.setOnClickListener {
            finish()
        }
    }
}
