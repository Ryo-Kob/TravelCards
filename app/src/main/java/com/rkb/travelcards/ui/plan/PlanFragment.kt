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
import kotlin.math.abs

class PlanFragment : Fragment() {
    lateinit var cards : List<Card>
    var cs : MutableList<CardSuite> = mutableListOf()
    var tl : MutableList<Int> = mutableListOf()

    val planViewModel: PlanViewModel by viewModels {
        PlanViewModelFactory((activity?.application as TravelCardsApplication).repository)
    }

    init {
        initializeTimeLine()
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

        // recyclerview初期化
        val recyclerView = view.findViewById<RecyclerView>(R.id.plan_recycler_view)
        val adapter = PlanAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // timelineも同様に初期化
        val recyclerViewT = view.findViewById<RecyclerView>(R.id.plan_recycler_view_timeline)
        val adapterT = PlanAdapterT()
        recyclerViewT.adapter = adapterT
        recyclerViewT.layoutManager = LinearLayoutManager(context)

        // card一覧(cards)を、データベースから拾ってくる。
        Single.fromCallable {
            planViewModel.getCardList()
        }.subscribeOn(Schedulers.io())
            .subscribe({
                cards = it
                Log.v("", cards[0].title)
                adapter.card = cards
            }, {})

        Single.fromCallable {
            planViewModel.getCardSuiteList()
        }.subscribeOn(Schedulers.io())
            .subscribe({
                if (it.isEmpty()) {
                    initializeCardSuite()
                }else {
                    for(i in it as MutableList<CardSuite>) {
                        cs.add(i)
                    }
                    cs.sortBy { i -> i.startTime }
                    adapter.notifyDataSetChanged()
                }
           }, {})

        // adapter接続
        adapter.submitList(cs)
        adapterT.submitList(tl)

        // スクロールイベント
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            // https://android.suzu-sd.com/2021/05/recyclerview_item_scroll/#OnScrollListener
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            }
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                recyclerViewT.scrollBy(0, dy)
            }
        })
        recyclerViewT.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            // https://qiita.com/Horie1024/items/72742f76485d02bf1b90
            override fun onInterceptTouchEvent(recyclerView: RecyclerView, e: MotionEvent): Boolean {
                return true
            }
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })


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

                    // 空白カードは入れ替えさせない
                    if (cs[fromPos].isBlank) return false

                    // 入れ替えられたカードのデータを操作する
                    for(i in 0..abs(toPos-fromPos) -1) {
                        var fromPos2=0; var toPos2=0
                        if (fromPos < toPos) {
                            fromPos2 = fromPos + i
                            toPos2 = fromPos2 + 1
                            cs[fromPos2].startTime += cs[toPos2].timer
                            cs[toPos2].startTime -= cs[fromPos2].timer
                        }else{
                            fromPos2 = fromPos - i
                            toPos2 = fromPos2 - 1
                            cs[fromPos2].startTime -= cs[toPos2].timer
                            cs[toPos2].startTime += cs[fromPos2].timer
                        }
                        Collections.swap(cs, fromPos, toPos)
                        adapter.notifyItemMoved(fromPos, toPos)
//                    adapter.notifyDataSetChanged()
                    }

                    Log.v("", "Click from:$fromPos, to:$toPos")
                    return true // true if moved, false otherwise
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val pos = viewHolder.adapterPosition
                    if (cs[pos].isBlank) {
                        // 空白を消された。カウンターアタック!
                        cs.removeAt(pos)
                        val ncs = CardSuite()
                        ncs.cardId = 0
                        ncs.text = "NewBlank"
                        ncs.isBlank = true
                        ncs.type = CardSuite.VIEW_TYPE_EMPTY
                        ncs.startDate = 0 // 日付をどうにかして数値にしたいが……
                        ncs.startTime = cs[pos-1].startTime + cs[pos-1].timer
                        ncs.isStartDateFixed = false
                        ncs.isStartTimeFixed = false
                        ncs.timer = 15
                        cs.add(pos, ncs)
                        adapter.notifyDataSetChanged()
                    }else{
                        // カードが消されたので、その分空白を充填する
                        val num = cs[pos].timer/15
                        cs.removeAt(pos)
                        for(i in 0..num-1) {
                            val ncs = CardSuite()
                            ncs.cardId = 0
                            ncs.text = "NewBlank-${i}"
                            ncs.isBlank = true
                            ncs.type = CardSuite.VIEW_TYPE_EMPTY
                            ncs.startDate = 0 // 日付をどうにかして数値にしたいが……
                            ncs.isStartDateFixed = false
                            ncs.isStartTimeFixed = false
                            ncs.timer = 15
                            ncs.startTime = cs[pos - 1 + i].startTime + cs[pos - 1 + i].timer
                            cs.add(pos+i, ncs)
                        }
                        adapter.notifyDataSetChanged()
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
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }

            // 新しいカードを追加する！
            val ncs = CardSuite()
            ncs.cardId = cardId
            ncs.text = cards[cardId].title
            ncs.isBlank = false
            ncs.type = CardSuite.VIEW_TYPE_CARD
            ncs.startDate = 0 // 日付をどうにかして数値にしたいが……
            ncs.isStartDateFixed = false
            ncs.isStartTimeFixed = false
            ncs.timer = cards[cardId].timerHour*60 + cards[cardId].timerMinute
            val manager = recyclerView.layoutManager as LinearLayoutManager
            val index = manager.findFirstVisibleItemPosition()+1
            ncs.startTime = index*15 // 1分=1として数値化
            Log.v("", "idx=${index}")
            cs.add(index, ncs)

            // 要らなくなった空白は捨てる
            for(i in 0..ncs.timer/15-1) {
                cs.removeAt(index+1)
            }
            adapter.notifyDataSetChanged()
        }

        Log.v("", "はよせんかい！")
    }

    override fun onStop() {
        super.onStop()
        Log.v("", "onStop!")

        // 編集したrecyclerviewのデータを永続化
        Single.fromCallable {
            planViewModel
        }.subscribeOn(Schedulers.io())
            .subscribe({
                it.deleteAllCardSuites()
                for(i in cs) {
//                    Log.v("", "${i.id}, ${i.isBlank}, ${i.text}")
                    it.insert(i)
                }
            }, {})
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
            ncs.startTime = 15 * i - 15
            ncs.timer = 15
            cs.add(ncs)
        }
    }

    private fun initializeTimeLine() {
        for(i in 0..4*24-1) {
            val ntl = i*15
            tl.add(ntl)
        }
    }
}