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


class Admin : Fragment() {

    private lateinit var datapass : PassInt

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find views
        val usernameInput = view.findViewById<EditText>(R.id.usernameInput)
        val passwordInput = view.findViewById<EditText>(R.id.passwordInput)
        val loginButton = view.findViewById<Button>(R.id.loginButton)

        // Handle login button click
        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                val adminViewFragment = AdminView()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, adminViewFragment)
                    .addToBackStack(null)
                    .commit()
                sendData()
            } else {
                usernameInput.error = "Input username"
                passwordInput.error = "Input password"
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
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