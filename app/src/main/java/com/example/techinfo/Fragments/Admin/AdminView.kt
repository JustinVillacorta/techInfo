package com.example.techinfo.Fragments.Admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.techinfo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminView : Fragment() {

    // Flag to track whether the FABs are expanded or not
    private var isFabExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_view, container, false)

        // Find FABs
        val addFab: FloatingActionButton = view.findViewById(R.id.Add)
        val deleteFab: FloatingActionButton = view.findViewById(R.id.delete)
        val updateFab: FloatingActionButton = view.findViewById(R.id.update)
        val troubleshootFab: FloatingActionButton = view.findViewById(R.id.troubleshoot)

        // Load animations
        val fabOpen: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        val fabClose: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)
        val rotateForward: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_forward)
        val rotateBackward: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_backward)

        // Set click listener for Add FAB
        addFab.setOnClickListener {
            if (isFabExpanded) {
                // Hide other FABs with animation
                deleteFab.startAnimation(fabClose)
                updateFab.startAnimation(fabClose)
                troubleshootFab.startAnimation(fabClose)
                addFab.startAnimation(rotateBackward)

                deleteFab.visibility = View.GONE
                updateFab.visibility = View.GONE
                troubleshootFab.visibility = View.GONE

                isFabExpanded = false
            } else {
                // Show other FABs with animation
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

        return view
    }
}
