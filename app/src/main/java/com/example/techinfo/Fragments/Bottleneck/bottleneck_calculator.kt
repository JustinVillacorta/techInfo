package com.example.techinfo.Fragments.Bottleneck

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.techinfo.R

class bottleneck_calculator : Fragment() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var cardView: CardView
    private lateinit var overallBottleneckProgressBar: ProgressBar
    private lateinit var bottleneckResultTextView: TextView

    companion object {
        private const val ARG_BOTTLENECK_PERCENTAGE = "bottleneck_percentage"

        fun newInstance(bottleneckPercentage: Int): bottleneck_calculator {
            val fragment = bottleneck_calculator()
            val args = Bundle().apply {
                putInt(ARG_BOTTLENECK_PERCENTAGE, bottleneckPercentage)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottleneck_calculator, container, false)

        // Initialize views
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        cardView = view.findViewById(R.id.cardView)
        overallBottleneckProgressBar = view.findViewById(R.id.progressbar)
        bottleneckResultTextView = view.findViewById(R.id.bottleneckResultTextView)

        // Get the overall bottleneck percentage from arguments
        arguments?.let {
            val overallBottleneckPercentage = it.getInt(ARG_BOTTLENECK_PERCENTAGE, 0)

            // Set the progress bar based on the overall bottleneck calculation
            overallBottleneckProgressBar.progress = overallBottleneckPercentage

            // Set the bottleneck result text
            bottleneckResultTextView.text = "Bottleneck Percentage: $overallBottleneckPercentage%"
        }

        return view
    }
}
