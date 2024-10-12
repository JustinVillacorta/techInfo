package com.example.techinfo.Fragments.Admin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.techinfo.R

class ForgotPassword : AppCompatActivity() {

    lateinit var InputEmail : EditText
    lateinit var SubmitBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)

        InputEmail = findViewById(R.id.emailInput)
        SubmitBtn = findViewById(R.id.submitButton)


        SubmitBtn.setOnClickListener{
            // ikw na bahala mag lagay nung function neto par
        }


    }
}