package com.example.innovaccerapplication

import com.google.firebase.Timestamp
import java.time.LocalTime

data class HostInfo(var name: String, var email: String, var phone: String)

class VisitorInfo(var name: String, var email: String, var phone: String, var timestamp: Timestamp)