    package com.example.techinfo.Fragments.Bottleneck

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Button
    import androidx.fragment.app.Fragment
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.example.techinfo.R

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
                val selectedCpu = selectedOptions[0]
                val selectedGpu = selectedOptions[1]
                val selectedResolution = selectedOptions[2]

                // Calculate the overall bottleneck percentage
                val overallBottleneckPercentage = calculateOverallBottleneck(selectedCpu, selectedGpu, selectedResolution)

                // Pass the overall bottleneck percentage to the next fragment
                val fragment = bottleneck_calculator.newInstance(overallBottleneckPercentage)
                val transaction = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragment_container, fragment)?.commit()
            }

            return view
        }

        private fun calculateOverallBottleneck(cpu: String?, gpu: String?, resolution: String?): Int {
            val cpuPerformance = when (cpu) {
                "Intel Core i9-13900K" -> 10
                "AMD Ryzen 9 7950X" -> 10
                "Intel Core i7-13700K" -> 8
                "AMD Ryzen 7 7800X" -> 8
                "Intel Core i5-13600K" -> 6
                else -> 0
            }

            val gpuPerformance = when (gpu) {
                "NVIDIA GeForce RTX 4090" -> 10
                "AMD Radeon RX 7900 XTX" -> 10
                "NVIDIA GeForce RTX 4080" -> 9
                "AMD Radeon RX 6800 XT" -> 8
                "NVIDIA GeForce RTX 3070" -> 6
                else -> 0
            }

            val resolutionImpact = when (resolution) {
                "4K" -> 3
                "1440p" -> 2
                "1080p" -> 1
                else -> 0
            }

            val totalPerformance = cpuPerformance + gpuPerformance - resolutionImpact
            return (100 * (totalPerformance.coerceIn(0, 10))) / 10 // Scale to 0-100
        }
    }
