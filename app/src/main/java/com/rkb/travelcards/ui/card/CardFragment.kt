package com.rkb.travelcards.ui.card

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rkb.travelcards.NewCardActivity
import com.rkb.travelcards.R

class CardFragment : Fragment() {

    private lateinit var cardViewModel: CardViewModel
    private lateinit var fab: FloatingActionButton
    private val newCardActivityRequestCode = 1

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        cardViewModel =
                ViewModelProvider(this).get(CardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_card, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.fab = view.findViewById<FloatingActionButton>(R.id.fragment_card_fab)
        this.fab.setOnClickListener {
            val intent = Intent(activity, NewCardActivity::class.java)
//            startActivity(intent)
            startActivityForResult(intent, newCardActivityRequestCode)
//            Toast.makeText(getActivity(), "Hello World!!!!!!!!!!!!!!", 1).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newCardActivityRequestCode && resultCode == Activity.RESULT_OK) {
//            data?.getStringExtra(NewCardActivity.EXTRA_REPLY)?.let {
//                val word = Word(it)
//                wordViewModel.insert(word)
//            }
        } else {
            Toast.makeText(
                activity,
                "aaaaaaaaaaaaaa",
                Toast.LENGTH_LONG).show()
        }
    }
}