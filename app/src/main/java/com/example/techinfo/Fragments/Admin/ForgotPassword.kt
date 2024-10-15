package com.example.techinfo.Fragments.Admin

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.techinfo.R
import com.example.techinfo.api_connector.ApiService
import com.example.techinfo.api_connector.OTPRequest
import com.example.techinfo.api_connector.PasswordResetRequest
import retrofit2.*
import android.util.Log
import com.example.techinfo.api_connector.RetrofitInstance
import java.util.regex.Pattern

class ForgotPassword : Fragment() {

    private lateinit var inputEmail: EditText
    private lateinit var otpInput: EditText
    private lateinit var newPasswordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var getOtpButton: Button
    private lateinit var submitButton: Button
    private lateinit var passwordChecklist: TextView

    private val otpTimeout: Long = 300000 // 5 minutes timer

    // Retrofit instance from RetrofitInstance
    private val apiService: ApiService = RetrofitInstance.getApiService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot__password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        inputEmail = view.findViewById(R.id.emailInput)
        otpInput = view.findViewById(R.id.otpInput)
        newPasswordInput = view.findViewById(R.id.newPasswordInput)
        confirmPasswordInput = view.findViewById(R.id.confirmPasswordInput)
        getOtpButton = view.findViewById(R.id.getOtpButton)
        submitButton = view.findViewById(R.id.submitButton)
        passwordChecklist = view.findViewById(R.id.passwordChecklist)

        // Initially hide the password fields and OTP input
        newPasswordInput.visibility = View.GONE
        confirmPasswordInput.visibility = View.GONE
        passwordChecklist.visibility = View.GONE
        otpInput.visibility = View.GONE

        // Handle "Get OTP" button click
        getOtpButton.setOnClickListener {
            requestOTP()
        }

        // Handle "Submit" button click
        submitButton.setOnClickListener {
            resetPassword()
        }

        // Listen for password input changes for live validation
        newPasswordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(newPasswordInput.text.toString())
            }
            override fun afterTextChanged(editable: Editable?) {}
        })

        // Listen for confirm password input to check if both passwords match
        confirmPasswordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                validateConfirmPassword()
            }
            override fun afterTextChanged(editable: Editable?) {}
        })
    }

    // Function to request OTP
    private fun requestOTP() {
        val email = inputEmail.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter your email", Toast.LENGTH_SHORT).show()
            return
        }

        // Log the email for debugging
        Log.d("ForgotPassword", "Requesting OTP for email: $email")

        // API call to request OTP
        val call = apiService.requestOTP(OTPRequest(email))
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Start OTP timer
                    startOtpTimer()

                    // Show OTP input and password fields after OTP is sent
                    otpInput.visibility = View.VISIBLE
                    newPasswordInput.visibility = View.VISIBLE
                    confirmPasswordInput.visibility = View.VISIBLE
                    passwordChecklist.visibility = View.VISIBLE
                } else {
                    Toast.makeText(requireContext(), "Failed to send OTP", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to validate new password (checks for strength)
    private fun validatePassword(password: String) {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        val checklist = StringBuilder()
        checklist.append("• At least one uppercase letter: ").append(if (hasUpperCase) "✔" else "✘").append("\n")
        checklist.append("• At least one lowercase letter: ").append(if (hasLowerCase) "✔" else "✘").append("\n")
        checklist.append("• At least one number: ").append(if (hasDigit) "✔" else "✘").append("\n")
        checklist.append("• At least one special character: ").append(if (hasSpecialChar) "✔" else "✘")
        passwordChecklist.text = checklist.toString()
    }

    // Function to validate if the new password matches the confirm password
    private fun validateConfirmPassword() {
        val newPassword = newPasswordInput.text.toString()
        val confirmPassword = confirmPasswordInput.text.toString()

        if (newPassword != confirmPassword) {
            submitButton.isEnabled = false
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
        } else {
            submitButton.isEnabled = true
        }
    }

    // Function to reset the password using the OTP
    private fun resetPassword() {
        val email = inputEmail.text.toString()
        val otp = otpInput.text.toString()
        val newPassword = newPasswordInput.text.toString()

        val resetPasswordRequest = PasswordResetRequest(email, otp, newPassword)

        val call = apiService.resetPassword(resetPasswordRequest)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Password reset successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Password reset failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to start OTP timer for 5 minutes
    private fun startOtpTimer() {
        getOtpButton.isEnabled = false
        object : CountDownTimer(otpTimeout, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                getOtpButton.text = "Resend OTP (${millisUntilFinished / 1000}s)"
            }

            override fun onFinish() {
                getOtpButton.text = "Resend OTP"
                getOtpButton.isEnabled = true

                // Hide OTP input, password fields, and checklist when the timer finishes
                otpInput.visibility = View.GONE
                newPasswordInput.visibility = View.GONE
                confirmPasswordInput.visibility = View.GONE
                passwordChecklist.visibility = View.GONE
            }
        }.start()
    }
}
