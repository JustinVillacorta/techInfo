package com.example.techinfo.Fragments.Admin

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.techinfo.R

class ForgotPassword : Fragment() {

    private lateinit var inputEmail: EditText
    private lateinit var otpInput: EditText
    private lateinit var getOtpButton: Button
    private lateinit var submitButton: Button

    // Timer duration: 5 minutes in milliseconds (5 * 60 * 1000 = 300000 milliseconds)
    private val otpTimeout: Long = 300000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot__password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        inputEmail = view.findViewById(R.id.emailInput)
        otpInput = view.findViewById(R.id.otpInput)
        getOtpButton = view.findViewById(R.id.getOtpButton)
        submitButton = view.findViewById(R.id.submitButton)

        // Handle "Get OTP" button click
        getOtpButton.setOnClickListener {
            // Disable the button and start the countdown timer
            startOtpTimer()
            // Handle your OTP generation logic here
        }

        // Handle "Submit" button click
        submitButton.setOnClickListener {
            // Handle your submit logic here
        }
    }

    // Function to start the OTP timer
    private fun startOtpTimer() {
        // Disable the "Get OTP" button
        getOtpButton.isEnabled = false

        // Create a countdown timer for 5 minutes
        object : CountDownTimer(otpTimeout, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Calculate the remaining minutes and seconds
                val minutesRemaining = millisUntilFinished / 1000 / 60
                val secondsRemaining = millisUntilFinished / 1000 % 60

                // Update the button text with the remaining time in "mm:ss" format
                getOtpButton.text = "Resend OTP in $minutesRemaining:$secondsRemaining"
            }

            override fun onFinish() {
                // Re-enable the "Get OTP" button after the countdown finishes
                getOtpButton.isEnabled = true
                getOtpButton.text = "Get OTP"
            }
        }.start()
    }
}
