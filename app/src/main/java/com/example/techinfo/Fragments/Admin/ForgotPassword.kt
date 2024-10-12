package com.example.techinfo.Fragments.Admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.techinfo.R

class ForgotPassword : Fragment() {

    lateinit var inputEmail: EditText
    lateinit var submitBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        inputEmail = view.findViewById(R.id.emailInput)
        submitBtn = view.findViewById(R.id.submitButton)

        // Handle the submit button click
        submitBtn.setOnClickListener {
            // Logic for submitting the email goes here
            // You can call a function to reset the password or show a toast
        }
    }
}
