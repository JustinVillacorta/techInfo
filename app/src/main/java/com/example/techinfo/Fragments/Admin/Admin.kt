package com.example.techinfo.Fragments.Admin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.techinfo.R
import com.example.techinfo.api_connector.ApiService
import com.example.techinfo.api_connector.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class Admin : Fragment() {

    private lateinit var datapass: PassInt

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameInput = view.findViewById<EditText>(R.id.usernameInput)
        val passwordInput = view.findViewById<EditText>(R.id.passwordInput)
        val loginButton = view.findViewById<Button>(R.id.loginButton)

        // Handle login button click
        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                authenticateUser(username, password)
            } else {
                usernameInput.error = "Input username"
                passwordInput.error = "Input password"
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun authenticateUser(username: String, password: String) {
        // Create a custom OkHttpClient with timeout settings
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Write timeout
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.74:8000/api/") // Update with your base URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Fetch users from the API
        apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful && response.body() != null) {
                    val users = response.body()!!

                    // Check if username and password match
                    val user = users.find { it.username == username && it.password == password }
                    if (user != null) {
                        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()

                        // Navigate to AdminView after successful login
                        navigateToAdminView()

                        sendData()
                    } else {
                        Toast.makeText(requireContext(), "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error fetching users", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToAdminView() {
        // Create an instance of AdminView fragment
        val adminViewFragment = AdminView()

        // Replace the current Admin fragment with AdminView
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, adminViewFragment) // Use your actual container ID
            .addToBackStack(null) // Add to back stack for navigation
            .commit()
    }

    interface PassInt {
        fun PassInt(data: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        datapass = context as PassInt
    }

    private fun sendData() {
        val datasending = 0
        datapass.PassInt(datasending)
    }
}
