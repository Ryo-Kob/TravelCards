package com.rkb.travelcards.ui.card

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
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

        val mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val fromPos = viewHolder.adapterPosition
                    val toPos = target.adapterPosition
                    adapter.notifyItemMoved(fromPos, toPos)

                    Log.v("", "Click from:$fromPos, to:$toPos")

                    return true // true if moved, false otherwise
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    adapter.notifyItemRemoved(viewHolder.adapterPosition)
                    Log.v("", "Click direction:$direction")
                }

            })
        mIth.attachToRecyclerView(recyclerView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newCardActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val card = Card()

            data?.getStringExtra(NewCardActivity.card_name)?.let { it ->
                card.title = it
                data.getStringExtra(NewCardActivity.card_comment)?.let {
                    card.description = it
                }
                data.getStringExtra(NewCardActivity.card_startDate)?.let {
                    if (data.getBooleanExtra(NewCardActivity.card_isStartDateSet, false)) {
                        card.strStartDate = it
                        card.isStartDateSet = true
                    }else{
                        card.isStartDateSet = false
                    }
                }
                data.getStringExtra(NewCardActivity.card_startTime)?.let {
                    if (data.getBooleanExtra(NewCardActivity.card_isStartTimeSet, false)) {
                        card.strStartTime = it
                        card.isStartTimeSet = true
                    }else{
                        card.isStartTimeSet = false
                    }
                }
                data.getStringExtra(NewCardActivity.card_strDateTime)?.let {
                    card.strDateTime =
//                        if (card.isStartDateSet || card.isStartTimeSet) it
                        if (it != "") it
                        else "(スケジュール未設定)"
                }
                data.getStringExtra(NewCardActivity.card_timeHour)?.let {
                    Log.v("", it)
                    card.timerHour = it.toInt()
                }
                data.getStringExtra(NewCardActivity.card_timeMinute)?.let {
                    card.timerMinute = it.toInt()
                }
                cardViewModel.insert(card)
            }

            Toast.makeText(
                activity,
                "カードを作成しました",
                Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                activity,
                "名前が登録されていません. 登録を中止します. \n(TODO: 画面戻す必要はない, 戻さないようにしたい! )",
                Toast.LENGTH_LONG).show()
        }
    }
}