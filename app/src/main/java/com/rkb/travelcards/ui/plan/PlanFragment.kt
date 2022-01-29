package com.rkb.travelcards.ui.plan

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import java.util.*

class PlanFragment : Fragment() {

    lateinit var cards : List<Card>
    var cs : MutableList<CardSuite> = mutableListOf()

    val planViewModel: PlanViewModel by viewModels {
        PlanViewModelFactory((activity?.application as TravelCardsApplication).repository)
    }

    init {
        initializeCardSuite()
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

        // adapter接続
        adapter.submitList(cs)

        // クリックイベント等
        val mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // 強制onSwipe封じ(正しい封じ方を知らない)
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val fromPos = viewHolder.adapterPosition
                    val toPos = target.adapterPosition

                    // 入れ替えられたカードのデータを操作する
                    Collections.swap(cs, fromPos, toPos)
                    adapter.notifyItemMoved(fromPos, toPos)

                    Log.v("", "Click from:$fromPos, to:$toPos")
                    return true // true if moved, false otherwise
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val pos = viewHolder.adapterPosition
                    if (cs[pos].isBlank) {
                        cs.removeAt(pos)
                        var ncs = CardSuite()
                        ncs.cardId = 0
                        ncs.text = "NewBlank"
                        ncs.isBlank = true
                        ncs.type = CardSuite.VIEW_TYPE_EMPTY
                        ncs.startDate = 0 // 日付をどうにかして数値にしたいが……
                        ncs.startTime = pos*4 // 1分=1として数値化
                        ncs.isStartDateFixed = false
                        ncs.isStartTimeFixed = false
                        ncs.timer = 15
                        cs.add(pos, ncs)
                        adapter.notifyDataSetChanged()
                    }else{
                        cs.removeAt(pos)
                        adapter.notifyItemRemoved(pos)
                        for(i in 0..cs[pos].timer/15-1) {
                            var ncs = CardSuite()
                            ncs.cardId = 0
                            ncs.text = "NewBlank"
                            ncs.isBlank = true
                            ncs.type = CardSuite.VIEW_TYPE_EMPTY
                            ncs.startDate = 0 // 日付をどうにかして数値にしたいが……
                            ncs.startTime = 0 // 1分=1として数値化
                            ncs.isStartDateFixed = false
                            ncs.isStartTimeFixed = false
                            ncs.timer = 15
                            cs.add(pos+i, ncs)
                            adapter.notifyItemInserted(pos+i)
                        }
                    }
                }
            })
        mIth.attachToRecyclerView(recyclerView)

        // ドロワー
        val navHostFragment = activity!!.supportFragmentManager.fragments[0] as NavHostFragment
        val navController = navHostFragment.navController
        view.findViewById<NavigationView>(R.id.my_nav_view).setupWithNavController(navController)

        val mDrawerLayout = view.findViewById<DrawerLayout>(R.id.fragment_plan)

        // ドロワーの結果取得
        childFragmentManager.setFragmentResultListener("pickingCardFromDrawer", this) { key, data ->
            val cardId = data.getInt("cardId", 0)

            if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                //drawer is open -> 閉じる
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }

            // 結果を使った処理
            Log.v("", "Good: $cardId")
//            setStartDateTime(year, month, day)

            var ncs = CardSuite()
            ncs.cardId = cardId
            ncs.text = cards[cardId].title
            ncs.isBlank = false
            ncs.type = CardSuite.VIEW_TYPE_CARD
            ncs.startDate = 0 // 日付をどうにかして数値にしたいが……
            ncs.startTime = 0 // 1分=1として数値化
            ncs.isStartDateFixed = false
            ncs.isStartTimeFixed = false
            ncs.timer = cards[cardId].timerHour*60 + cards[cardId].timerMinute
            cs.add(0, ncs)
            adapter.notifyDataSetChanged()
        }
    }

    private fun initializeCardSuite() {
        var ncs : CardSuite
        for(i in 1..4*24) {
            ncs = CardSuite()
            ncs.cardId = 0
            ncs.text = "$i"
            ncs.isBlank = true
            ncs.type = CardSuite.VIEW_TYPE_EMPTY
            ncs.isStartDateFixed = false
            ncs.startDate = 0
            ncs.startTime = 15*i
            ncs.timer = 15
            cs.add(ncs)
        }
    }
}