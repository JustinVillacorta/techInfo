package com.example.techinfo.Fragments.Admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.techinfo.R

class Forgot_Password : Fragment() {

    lateinit var InputEmail: EditText
    lateinit var SubmitBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot__password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        InputEmail = view.findViewById(R.id.emailInput)
        SubmitBtn = view.findViewById(R.id.submitButton)

        // Set onClickListener for the submit button
        SubmitBtn.setOnClickListener {
            // Add functionality here
        }
    }
}
