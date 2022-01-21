package com.rkb.travelcards.ui.plan

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rkb.travelCards.ui.plan.PlanViewModel
import com.rkb.travelCards.ui.plan.PlanViewModelFactory
import com.rkb.travelcards.*
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlanFragment : Fragment() {

    var cards = mutableListOf<Card>()

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

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerView = view.findViewById<RecyclerView>(R.id.plan_list_recycler_view)
        val adapter = PlanAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)


        // card一覧(cards)を、データベースから拾ってくる。
        Single.fromCallable {
            planViewModel.getCardList()
        }.subscribeOn(Schedulers.io())
            .flatMapObservable { it.toObservable() }
            .subscribe({ cards.add(it) })

        planViewModel.allCardSuites.observe(viewLifecycleOwner, Observer { cardSuites ->
            // Update the cached copy of the words in the adapter.
            cardSuites?.let { adapter.submitList(it) }
        })

//        this.fab = view.findViewById<FloatingActionButton>(R.id.fragment_plan_fab)
//        this.fab.setOnClickListener {
//            val intent = Intent(activity, NewplanActivity::class.java)
//            startActivityForResult(intent, newplanActivityRequestCode)
//        }

        adapter.itemClickListener = object : PlanAdapter.OnItemClickListener {
            override fun onItemClick(holder: PlanAdapter.ViewHolder) {
                //
                // ここにクリックイベント時の処理を記述
                //
                // 例：Toastを表示
                val _position = holder.adapterPosition  // アイテムのポジションを取得
                val _mesg = holder.textViewName.text    // mesgはエレメントのView(TextView)
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
    }
}