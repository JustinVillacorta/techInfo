package com.example.techinfo.Fragments.Admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.techinfo.R
import com.example.techinfo.api_connector.RetrofitInstance
import com.example.techinfo.api_connector.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Admin : Fragment() {

    private lateinit var datapass: PassInt
    private lateinit var sharedPreferences: android.content.SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)

        // Check if the admin is already logged in
        val isAdminLoggedIn = sharedPreferences.getBoolean("isAdminLoggedIn", false)
        if (isAdminLoggedIn) {
            // Admin is already logged in, directly navigate to AdminView
            navigateToAdminView()
            return
        }

        val usernameInput = view.findViewById<EditText>(R.id.usernameInput)
        val passwordInput = view.findViewById<EditText>(R.id.passwordInput)
        val loginButton = view.findViewById<Button>(R.id.loginButton)

        // Handle login button click
        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                authenticateUser(username, password)
            } else {
                if (username.isEmpty()) usernameInput.error = "Input username"
                if (password.isEmpty()) passwordInput.error = "Input password"
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
        val forgotpassword = view.findViewById<TextView>(R.id.ForgotPassword)

        forgotpassword.setOnClickListener {
            // Replace with the ForgotPassword fragment using FragmentManager
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, Forgot_Password())
            fragmentTransaction.addToBackStack(null) // Optional: if you want to add to back stack
            fragmentTransaction.commit()
        }

    }



    // Moved the authentication logic to use RetrofitInstance
    private fun authenticateUser(username: String, password: String) {
        Log.d("Admin", "Authenticating user...")

        // Fetch API service from RetrofitInstance
        val apiService = RetrofitInstance.getApiService()

        // Fetch users from the API
        apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                Log.d("Admin", "Received API response")

                if (response.isSuccessful) {
                    val users = response.body()

                    if (users != null) {
                        Log.d("Admin", "User list: $users")

                        // Check if username and password match
                        val user = users.find { it.username == username && it.password == password }
                        if (user != null) {
                            Log.d("Admin", "Login successful for user: $username")
                            if (isAdded) {
                                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                            }

                            // Save login state in SharedPreferences
                            sharedPreferences.edit().putBoolean("isAdminLoggedIn", true).apply()

                            // Navigate to AdminView after successful login
                            navigateToAdminView()

                            sendData()
                        } else {
                            Log.d("Admin", "Invalid username or password")
                            if (isAdded) {
                                Toast.makeText(requireContext(), "Invalid username or password", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Log.e("Admin", "Response body is null")
                        if (isAdded) {
                            Toast.makeText(requireContext(), "Error fetching users", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("Admin", "Error response: ${response.code()}")
                    if (isAdded) {
                        Toast.makeText(requireContext(), "Error fetching users", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("Admin", "API call failed: ${t.message}")
                if (isAdded) {
                    Toast.makeText(requireContext(), "Network error: No Connection or Server Error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    // Navigate to the AdminView fragment after login success
    private fun navigateToAdminView() {
        // Create an instance of AdminView fragment
        val adminViewFragment = AdminView()

        // Replace the current Admin fragment with AdminView without adding to the back stack
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, adminViewFragment) // Use your actual container ID
            .commit() // No addToBackStack(null), so pressing back won't return to Admin login
    }

    interface PassInt {
        fun PassInt(data: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            datapass = context as PassInt
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement PassInt")
        }
    }

    private fun sendData() {
        val datasending = 1
        datapass.PassInt(datasending)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Clear login state when the activity is destroyed (not just when the fragment is destroyed)
        sharedPreferences.edit().putBoolean("isAdminLoggedIn", false).apply()
    }
}