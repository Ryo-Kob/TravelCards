package com.rkb.travelcards.ui.journey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rkb.travelcards.R

class JourneyFragment : Fragment() {

    private lateinit var journeyViewModel: JourneyViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        journeyViewModel =
                ViewModelProvider(this).get(JourneyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_journey, container, false)
        val textView: TextView = root.findViewById(R.id.text_journey)
        journeyViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}