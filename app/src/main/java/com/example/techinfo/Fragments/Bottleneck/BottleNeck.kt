package com.example.techinfo.Fragments.Bottleneck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.R
import com.example.techinfo.api_connector.BottleneckRequest
import com.example.techinfo.api_connector.BottleneckResponse
import com.example.techinfo.api_connector.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottleNeck : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: BottleneckAdaptor
    private lateinit var bottleList: List<BottleneckData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottle_neck, container, false)

        recyclerView = view.findViewById(R.id.recyclerView_bottleneck)

        // Sample data list
        bottleList = listOf(
            BottleneckData("Central Processing Unit"),
            BottleneckData("Graphics Processing Unit"),
            BottleneckData("Resolution")
        )

        // Initialize the adapter
        itemAdapter = BottleneckAdaptor(bottleList)

        // Set LayoutManager for the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = itemAdapter

        val ButtonIn: Button = view.findViewById(R.id.button)
        ButtonIn.setOnClickListener {
            // Gather selected options using the new method
            val selectedOptions = itemAdapter.getSelectedOptions()

            if (selectedOptions.size < 3 || selectedOptions.contains(null)) {
                Toast.makeText(requireContext(), "Please select CPU, GPU, and Resolution", Toast.LENGTH_SHORT).show()
            } else {
                val selectedCpu = selectedOptions[0] ?: ""
                val selectedGpu = selectedOptions[1] ?: ""
                val selectedResolution = selectedOptions[2] ?: ""

                // Create the data object to send via POST
                val bottleneckRequest = BottleneckRequest(
                    processor_name = selectedCpu,
                    gpu_name = selectedGpu,
                    resolutions_name = selectedResolution
                )

                // Call the POST API
                postBottleneckData(bottleneckRequest)
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showExitConfirmationDialog()
        }
    }

    private fun showExitConfirmationDialog() {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit the app?")
            .setPositiveButton("Yes") { dialog, _ ->
                requireActivity().finish() // Finish the activity to exit the app
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog
            }
            .create()
            .show()
    }

    // Send the POST request with the selected data
    private fun postBottleneckData(bottleneckRequest: BottleneckRequest) {
        // Using RetrofitInstance to call the API
        RetrofitInstance.getApiService().postBottleneckData(bottleneckRequest)
            .enqueue(object : Callback<BottleneckResponse> {
                override fun onResponse(call: Call<BottleneckResponse>, response: Response<BottleneckResponse>) {
                    if (response.isSuccessful) {
                        val bottleneckResponse = response.body()

                        // Update the UI with the response data
                        if (bottleneckResponse != null) {
                            // Set the message in the bottleneckMessageTextView
                            val bottleneckMessageTextView = view?.findViewById<TextView>(R.id.bottleneckMessageTextView)
                            bottleneckMessageTextView?.text = bottleneckResponse.message

                            // Set the percentage difference in the bottleneckPercentageTextView
                            val bottleneckPercentageTextView = view?.findViewById<TextView>(R.id.bottleneckPercentageTextView)
                            bottleneckPercentageTextView?.text = "Percentage Difference: ${bottleneckResponse.percentage_difference}%"

                            // Show the AlertDialog with the bottleneck information
                            showBottleneckDialog(bottleneckResponse)
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to get bottleneck data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BottleneckResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Show the AlertDialog with the bottleneck information
    private fun showBottleneckDialog(bottleneckResponse: BottleneckResponse) {
        // Inflate the custom layout for the AlertDialog
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.bottleneck_calculator_dialog, null)

        // Initialize the views in the custom layout
        val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressbar)
        val bottleneckMessageTextView = dialogView.findViewById<TextView>(R.id.bottleneckMessageTextView)
        val bottleneckPercentageTextView = dialogView.findViewById<TextView>(R.id.bottleneckPercentageTextView)

        // Set the bottleneck message and percentage difference in the TextViews
        val formattedMessage = """
        SCORE:
        
        CPU Score: ${bottleneckResponse.cpuScore}
        GPU Score: ${bottleneckResponse.gpuScore}
        
        MESSAGE:
        
        ${bottleneckResponse.message}
    """.trimIndent()

        bottleneckMessageTextView.text = formattedMessage
        bottleneckPercentageTextView.text = "Bottleneck Percentage: ${bottleneckResponse.percentage_difference}%"

        // Convert the percentage to an integer by rounding to the nearest whole number
        val roundedPercentage = Math.round(bottleneckResponse.percentage_difference).toInt()

        // Set the rounded percentage to the ProgressBar
        progressBar.progress = roundedPercentage

        // Create and show the AlertDialog
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        // Show the dialog
        dialog.show()

        // Set the background of the dialog to transparent
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
