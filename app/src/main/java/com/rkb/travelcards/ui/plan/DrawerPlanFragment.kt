package com.rkb.travelcards.ui.plan

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rkb.travelcards.R
import com.rkb.travelcards.TravelCardsApplication
import com.rkb.travelcards.ui.card.CardListAdapter
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*


class DrawerPlanFragment : Fragment() {
    private val drawerViewModel: DrawerPlanViewModel by viewModels {
        DrawerPlanViewModelFactory((activity?.application as TravelCardsApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_drawer_plan_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.plan_card_list_recycler_view)
        val adapter = DrawerPlanAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        drawerViewModel.allCards.observe(viewLifecycleOwner, Observer { cards ->
            // Update the cached copy of the words in the adapter.
            cards?.let { adapter.submitList(it) }
        })


    }

    // クリックイベント等
    override fun onItemLongClick(arg0 : AdapterView<*>, arg1:View, pos:Int, id:Int) : Boolean {
        //TODO Auto-generated method stub
        Log.v("long clicked")
        return true
    }


//        val mIth = ItemTouchHelper(
//            object : ItemTouchHelper.SimpleCallback(
//                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
//                ItemTouchHelper.ACTION_STATE_IDLE
//            ) {
//                override fun onMove(
//                    recyclerView: RecyclerView,
//                    viewHolder: RecyclerView.ViewHolder,
//                    target: RecyclerView.ViewHolder
//                ): Boolean {
//                    val fromPos = viewHolder.adapterPosition
//                    val toPos = target.adapterPosition
//                    Log.v("", "長押しはされましたよ")
////                    adapter.notifyItemMoved(fromPos, toPos)
////                    Single.fromCallable {
////                        drawerViewModel.allCards
////                    }.subscribeOn(Schedulers.io())
////                        .subscribe({
////                            Collections.swap(it.value!!, fromPos, toPos)
////                        }, {})
//
//                    Log.v("", "Click from:$fromPos, to:$toPos")
//                    return true // true if moved, false otherwise
//                }
//
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
//
//                override fun isLongPressDragEnabled(): Boolean {
//                    return false
//                }
//            })
//        mIth.attachToRecyclerView(recyclerView)
    }
}