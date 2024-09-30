package com.example.techinfo.Fragments.Admin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.techinfo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminView : Fragment() {

    // Flag to track whether the FABs are expanded or not
    private var isFabExpanded = false

    // Back press management variables
    private var doubleBackToExitPressedOnce = false
    private val exitHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true // Retain the fragment during configuration changes
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_view, container, false)

        // Find FABs
        val addFab: FloatingActionButton = view.findViewById(R.id.Add)
        val deleteFab: FloatingActionButton = view.findViewById(R.id.delete)
        val updateFab: FloatingActionButton = view.findViewById(R.id.update)
        val troubleshootFab: FloatingActionButton = view.findViewById(R.id.troubleshoot)

        // Set up FAB animations
        setupFabAnimations(addFab, deleteFab, updateFab, troubleshootFab)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle the back press behavior after the fragment's view is created
        handleBackPress()
    }

    private fun setupFabAnimations(
        addFab: FloatingActionButton,
        deleteFab: FloatingActionButton,
        updateFab: FloatingActionButton,
        troubleshootFab: FloatingActionButton
    ) {
        val fabOpen = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        val fabClose = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)
        val rotateForward = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_forward)
        val rotateBackward = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_backward)

        addFab.setOnClickListener {
            if (isFabExpanded) {
                // Hide other FABs
                deleteFab.startAnimation(fabClose)
                updateFab.startAnimation(fabClose)
                troubleshootFab.startAnimation(fabClose)
                addFab.startAnimation(rotateBackward)

                deleteFab.visibility = View.GONE
                updateFab.visibility = View.GONE
                troubleshootFab.visibility = View.GONE

                isFabExpanded = false
            } else {
                // Show other FABs
                deleteFab.visibility = View.VISIBLE
                updateFab.visibility = View.VISIBLE
                troubleshootFab.visibility = View.VISIBLE

                deleteFab.startAnimation(fabOpen)
                updateFab.startAnimation(fabOpen)
                troubleshootFab.startAnimation(fabOpen)
                addFab.startAnimation(rotateForward)

                isFabExpanded = true
            }
        }
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle double back press to exit
                if (doubleBackToExitPressedOnce) {
                    requireActivity().finish() // Exits the app or closes the activity
                } else {
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(requireContext(), "Press back again to exit", Toast.LENGTH_SHORT).show()

                    // Reset the flag after 2 seconds
                    exitHandler.postDelayed({
                        doubleBackToExitPressedOnce = false
                    }, 2000) // 2-second window
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove any pending callbacks when the view is destroyed to avoid memory leaks
        exitHandler.removeCallbacksAndMessages(null)
    }
}
