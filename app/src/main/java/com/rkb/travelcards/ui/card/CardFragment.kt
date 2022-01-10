package com.rkb.travelcards.ui.card

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rkb.travelcards.Card
import com.rkb.travelcards.newCard.NewCardActivity
import com.rkb.travelcards.R
import com.rkb.travelcards.TravelCardsApplication

class CardFragment : Fragment() {

//    private lateinit var cardViewModel: CardViewModel
    private val cardViewModel: CardViewModel by viewModels {
        CardViewModelFactory((activity?.application as TravelCardsApplication).repository)
    }
    private lateinit var fab: FloatingActionButton
    private val newCardActivityRequestCode = 1

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        cardViewModel =
//                ViewModelProvider(this).get(CardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_card, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerView = view.findViewById<RecyclerView>(R.id.card_list_recycler_view)
        val adapter = CardListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        cardViewModel.allCards.observe(viewLifecycleOwner, Observer { cards ->
            // Update the cached copy of the words in the adapter.
            cards?.let { adapter.submitList(it) }
        })

        this.fab = view.findViewById<FloatingActionButton>(R.id.fragment_card_fab)
        this.fab.setOnClickListener {
            val intent = Intent(activity, NewCardActivity::class.java)
            startActivityForResult(intent, newCardActivityRequestCode)
//            Toast.makeText(getActivity(), "Hello World!!!!!!!!!!!!!!", 1).show()
        }

        adapter.itemClickListener = object : CardListAdapter.OnItemClickListener {
            override fun onItemClick(holder: CardListAdapter.ViewHolder) {
                //
                // ここにクリックイベント時の処理を記述
                //
                // 例：Toastを表示
                val _position = holder.adapterPosition  // アイテムのポジションを取得
                val _mesg = holder.textView.text    // mesgはエレメントのView(TextView)
                Toast.makeText(
                    activity,
                    "Click Pos=${_position} Mesg=\"${_mesg}\"",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newCardActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewCardActivity.EXTRA_REPLY)?.let {
                val card = Card(0)
                card.title = it

                cardViewModel.insert(card)
            }
            Toast.makeText(
                activity,
                "天才！！",
                Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                activity,
                "名前が登録されてないよーーー",
                Toast.LENGTH_LONG).show()
        }
    }
}