package com.rkb.travelcards.ui.plan

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rkb.travelcards.R
import com.rkb.travelcards.TravelCardsApplication
import com.rkb.travelcards.ui.card.CardListAdapter


class DrawerPlanFragment : Fragment() {
    private val cardViewModel: DrawerPlanViewModel by viewModels {
        DrawerPlanViewModelFactory((activity?.application as TravelCardsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v("", "ああああああああああああああああ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_drawer_plan_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.plan_list_recycler_view)
        val adapter = CardListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        cardViewModel.allCards.observe(viewLifecycleOwner, Observer { cards ->
            // Update the cached copy of the words in the adapter.
            cards?.let { adapter.submitList(it) }
        })
    }
}