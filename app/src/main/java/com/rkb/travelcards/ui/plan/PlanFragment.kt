package com.rkb.travelcards.ui.plan

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
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
import java.util.*

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

        var cnt = 0

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
            cardSuites?.let {
                cnt = 0
                for (item in it) {
                    cnt += 1
//                    Log.d("Observer", "id ${item.id}, cardId ${item.cardId}")
                }
                Log.d("Observer", "card count: $cnt")
                if (cnt == 0) {
                    Log.v("", "Initialize!!")
                    this.initializeCardSuite()
                }
                adapter.submitList(it)
            }
        })

        // クリックイベント等
        val mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.UP // 強制onSwipe封じ(正しい封じ方を知らない)
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    val fromPos = viewHolder.adapterPosition
                    val toPos = target.adapterPosition
                    adapter.notifyItemMoved(fromPos, toPos)
                    Single.fromCallable {
                        planViewModel.allCardSuites
                    }.subscribeOn(Schedulers.io())
                        .subscribe({
                            Collections.swap(it.value!!, fromPos, toPos)
                        }, {})

                    Log.v("", "Click from:$fromPos, to:$toPos")
                    return true // true if moved, false otherwise
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {}
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
                //drawer is open
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }

            // 結果を使った処理
            Log.v("", "Good: $cardId")
//            setStartDateTime(year, month, day)
        }

        // CardSuiteがすっからかんなら、空白充填で初期化する
//        val s : Int = adapter.currentList.size
//        Log.v("", "size: $s")
//        if (s == 0) {
//            Log.v("", "Initialize!!")
//            this.initializeCardSuite()
//        }
    }

    private fun initializeCardSuite() {
        var cs : CardSuite
        for(i in 1..4*24) {
            cs = CardSuite()
            cs.cardId = 0
            cs.isBlank = true
            cs.isStartDateFixed = false
            cs.startDate = 0
            cs.startTime = 15*i
            cs.timer = 15
            cs.text = "$i"
            planViewModel.insert(cs)
        }
    }
}