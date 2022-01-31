package com.rkb.travelcards.ui.planViewer

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.rkb.travelcards.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class PlanViewerFragment : Fragment() {
    lateinit var cards : List<Card>
    var cs : MutableList<CardSuite> = mutableListOf()
    var cs2 : MutableList<CardSuite2> = mutableListOf()
    var tl : MutableList<Int> = mutableListOf()

    val planViewModel: PlanViewerViewModel by viewModels {
        PlanViewerViewModelFactory((activity?.application as TravelCardsApplication).repository)
    }

    init {
        initializeTimeLine()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_plan_viewer, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerview初期化
        val recyclerView = view.findViewById<RecyclerView>(R.id.plan_viewer_recycler_view)
        val adapter = PlanViewerAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        val recyclerView2 = view.findViewById<RecyclerView>(R.id.plan_viewer_recycler_view2)
        val adapter2 = PlanViewerAdapter2()
        recyclerView2.adapter = adapter2
        recyclerView2.layoutManager = LinearLayoutManager(context)

        // timelineも同様に初期化
        val recyclerViewT = view.findViewById<RecyclerView>(R.id.plan_recycler_view_timeline)
        val adapterT = PlanViewerAdapterT()
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
                adapter2.card = cards
            }, {})

        Single.fromCallable {
            planViewModel.getCardSuiteList()
        }.subscribeOn(Schedulers.io())
            .subscribe({
//                if (true){ //it.isEmpty()) { // 仮で毎回初期化するときの条件文
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

        Single.fromCallable {
            planViewModel.getCardSuiteList2()
        }.subscribeOn(Schedulers.io())
            .subscribe({
//                if (true){ //it.isEmpty()) { // 仮で毎回初期化するときの条件文
                if (it.isEmpty()) {
                    initializeCardSuite2()
                }else {
                    for(i in it as MutableList<CardSuite2>) {
                        cs2.add(i)
                    }
                    cs2.sortBy { i -> i.startTime }
                    adapter2.notifyDataSetChanged()
                }
            }, {})

        // adapter接続
        adapter.submitList(cs)
        adapter2.submitList(cs2)
        adapterT.submitList(tl)

        // スクロールイベント
        var sliding = 0
        recyclerView.addOnScrollListener(object : OnScrollListener(){
            // https://android.suzu-sd.com/2021/05/recyclerview_item_scroll/#OnScrollListener
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when(newState) {
                    SCROLL_STATE_SETTLING, SCROLL_STATE_DRAGGING -> {
                        sliding = 1
                    }
                    SCROLL_STATE_IDLE -> {
                    }
                }
            }
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (sliding == 1) {
                    recyclerView2.scrollBy(0, dy)
                    recyclerViewT.scrollBy(0, dy)
                }
            }
        })
        recyclerView2.addOnScrollListener(object : OnScrollListener(){
            // https://android.suzu-sd.com/2021/05/recyclerview_item_scroll/#OnScrollListener
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when(newState) {
                    SCROLL_STATE_SETTLING, SCROLL_STATE_DRAGGING -> {
                        sliding = 2
                    }
                    SCROLL_STATE_IDLE -> {
                    }
                }
            }
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (sliding == 2) {
                    recyclerView.scrollBy(0, dy)
                    recyclerViewT.scrollBy(0, dy)
                }
            }
        })
        recyclerViewT.addOnItemTouchListener(object : OnItemTouchListener {
            // https://qiita.com/Horie1024/items/72742f76485d02bf1b90
            override fun onInterceptTouchEvent(recyclerView: RecyclerView, e: MotionEvent): Boolean {
                return true
            }
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    override fun onStop() {
        super.onStop()
        Log.v("", "onStop!")

        // 編集したrecyclerviewのデータを永続化
//        Single.fromCallable {
//            planViewModel
//        }.subscribeOn(Schedulers.io())
//            .subscribe({
//                it.deleteAllCardSuites()
//                for(i in cs) {
////                    Log.v("", "${i.id}, ${i.isBlank}, ${i.text}")
//                    it.insert(i)
//                }
//            }, {})
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

        // Cardに時刻指定のカードが有れば入れちゃう
        for(cardId in cards.indices) {
            if (!cards[cardId].isStartTimeSet) continue

            // 開始時刻計算
            var st = 0
            var stTime = LocalTime.parse(cards[cardId].strStartTime, DateTimeFormatter.ofPattern("HH:mm"))
//            Log.v("", "${stTime.hour}, ${stTime.minute}, ${stTime.toSecondOfDay()}")
            st += stTime.hour * 60
            st += stTime.minute

            // index計算
            var index = -1
            for(j in 0..cs.size-1) {
                Log.v("", "${cs[j].startTime}, ${st}")
                if (cs[j].startTime == st) {
                    index = j
                    break
                }
            }

            val ncs = CardSuite()
            ncs.cardId = cardId
            ncs.text = cards[cardId].title
            ncs.isBlank = false
            ncs.type = CardSuite.VIEW_TYPE_CARD
            ncs.startDate = 0 // 日付をどうにかして数値にしたいが……
            ncs.isStartDateFixed = false
            ncs.isStartTimeFixed = true
            ncs.timer = cards[cardId].timerHour * 60 + cards[cardId].timerMinute
            ncs.startTime = st
            ncs.startTimeOriginal = st
            Log.v("", "idx=${index}")
            cs.add(index, ncs)

            // 要らなくなった空白は捨てる
            for (i in 0..ncs.timer / 15 - 1) {
                cs.removeAt(index + 1)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeCardSuite2() {
        var ncs : CardSuite2
        for(i in 1..4*24) {
            ncs = CardSuite2()
            ncs.cardId = 0
            ncs.text = "$i"
            ncs.isBlank = true
            ncs.type = CardSuite2.VIEW_TYPE_EMPTY
            ncs.isStartDateFixed = false
            ncs.startDate = 0
            ncs.startTime = 15 * i - 15
            ncs.timer = 15
            cs2.add(ncs)
        }

        // Cardに時刻指定のカードが有れば入れちゃう
        for(cardId in cards.indices) {
            if (!cards[cardId].isStartTimeSet) continue

            // 開始時刻計算
            var st = 0
            var stTime = LocalTime.parse(cards[cardId].strStartTime, DateTimeFormatter.ofPattern("HH:mm"))
//            Log.v("", "${stTime.hour}, ${stTime.minute}, ${stTime.toSecondOfDay()}")
            st += stTime.hour * 60
            st += stTime.minute

            // index計算
            var index = -1
            for(j in 0..cs2.size-1) {
                Log.v("", "${cs2[j].startTime}, ${st}")
                if (cs2[j].startTime == st) {
                    index = j
                    break
                }
            }

            val ncs = CardSuite2()
            ncs.cardId = cardId
            ncs.text = cards[cardId].title
            ncs.isBlank = false
            ncs.type = CardSuite2.VIEW_TYPE_CARD
            ncs.startDate = 0 // 日付をどうにかして数値にしたいが……
            ncs.isStartDateFixed = false
            ncs.isStartTimeFixed = true
            ncs.timer = cards[cardId].timerHour * 60 + cards[cardId].timerMinute
            ncs.startTime = st
            ncs.startTimeOriginal = st
            Log.v("", "idx=${index}")
            cs2.add(index, ncs)

            // 要らなくなった空白は捨てる
            for (i in 0..ncs.timer / 15 - 1) {
                cs2.removeAt(index + 1)
            }
        }
    }
    private fun initializeTimeLine() {
        for(i in 0..4*24-1) {
            val ntl = i*15
            tl.add(ntl)
        }
    }
}