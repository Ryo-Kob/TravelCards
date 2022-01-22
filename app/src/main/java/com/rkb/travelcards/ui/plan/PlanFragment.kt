package com.rkb.travelcards.ui.plan

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.rkb.travelCards.ui.plan.PlanViewModel
import com.rkb.travelCards.ui.plan.PlanViewModelFactory
import com.rkb.travelcards.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PlanFragment : Fragment() {

    lateinit var cards : List<Card>

    val planViewModel: PlanViewModel by viewModels {
        PlanViewModelFactory((activity?.application as TravelCardsApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_plan, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerView = view.findViewById<RecyclerView>(R.id.plan_recycler_view)
        val adapter = PlanAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)


        // card一覧(cards)を、データベースから拾ってくる。
        Single.fromCallable {
            planViewModel.getCardList()
        }.subscribeOn(Schedulers.io())
            .subscribe({
                cards = it
                Log.v("", cards[0].title)
                adapter.card = cards
            }, {})

        planViewModel.allCardSuites.observe(viewLifecycleOwner, Observer { cardSuites ->
            // Update the cached copy of the words in the adapter.
            cardSuites?.let { adapter.submitList(it) }
        })

        // クリックイベント等
        val mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val fromPos = viewHolder.adapterPosition
                    val toPos = target.adapterPosition
                    adapter.notifyItemMoved(fromPos, toPos)

                    Toast.makeText(
                        activity,
                        "Click from:$fromPos, to:$toPos",
                        Toast.LENGTH_LONG
                    ).show()
                    return true // true if moved, false otherwise
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                }
            })
        mIth.attachToRecyclerView(recyclerView)
//        val itemTouchHelper = ItemTouchHelper(
//            object : ItemTouchHelper.SimpleCallback(
//                ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT
//            ) {
//                override fun onItemClick(holder: PlanAdapter.ViewHolder) {
//                    //
//                    // ここにクリックイベント時の処理を記述
//                    //
//                    // 例：Toastを表示
//                    val _position = holder.adapterPosition  // アイテムのポジションを取得
//                    val _mesg = holder.textViewName.text    // mesgはエレメントのView(TextView)
//                    Toast.makeText(
//                        activity,
//                        "Click Pos=${_position} Mesg=\"${_mesg}\"",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//
//                override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
//
//                    val fromPosition = viewHolder?.adapterPosition ?: 0
//                    val toPosition = target?.adapterPosition ?: 0
//
//                    recyclerView!!.adapter!!.notifyItemMoved(fromPosition, toPosition)
//
//                    return true
//                }
//
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
//                    viewHolder?.let {
//                        recyclerView.adapter!!.notifyItemRemoved(viewHolder.adapterPosition)
//                    }
//                }
//            })
//        itemTouchHelper.attachToRecyclerView(recyclerView)

        // ドロワー
        val navHostFragment = activity!!.supportFragmentManager.fragments[0] as NavHostFragment
//        val navHostFragment = activity!!.supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        view.findViewById<NavigationView>(R.id.my_nav_view).setupWithNavController(navController)

    }
}