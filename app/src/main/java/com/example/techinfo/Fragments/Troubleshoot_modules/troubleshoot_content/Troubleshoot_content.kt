package com.example.techinfo.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.techinfo.R

class Troubleshoot_content : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_troubleshoot_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("ITEM_TITLE")
        val content = arguments?.getString("ITEM_CONTENT")

        val titleTextView: TextView = view.findViewById(R.id.itemTitleTextView)
        val contentTextView: TextView = view.findViewById(R.id.itemContentTextView)

        titleTextView.text = title
        contentTextView.text = content

        val backButton: View = view.findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack() // Navigate back to the previous fragment
        }
    }

    companion object {
        fun newInstance(title: String, content: String): Troubleshoot_content {
            val fragment = Troubleshoot_content()
            val args = Bundle()
            args.putString("ITEM_TITLE", title)
            args.putString("ITEM_CONTENT", content)
            fragment.arguments = args
            return fragment
        }
    }
}
