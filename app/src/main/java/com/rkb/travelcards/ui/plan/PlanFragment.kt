package com.rkb.travelcards.ui.plan

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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
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


//        val recyclerView = view.findViewById<RecyclerView>(R.id.plan_list_recycler_view)
        val adapter = PlanAdapter()
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(context)


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

//        this.fab = view.findViewById<FloatingActionButton>(R.id.fragment_plan_fab)
//        this.fab.setOnClickListener {
//            val intent = Intent(activity, NewplanActivity::class.java)
//            startActivityForResult(intent, newplanActivityRequestCode)
//        }

        Log.v("", activity.toString())
        Log.v("", activity!!.supportFragmentManager.toString())
        Log.v("", activity!!.supportFragmentManager.findFragmentById(R.id.my_nav_host).toString())

        // ドロワー
        val navHostFragment = activity!!.supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment
//        val navHostFragment = view.findViewById<View?>(R.id.my_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        view.findViewById<NavigationView>(R.id.my_nav_view).setupWithNavController(navController)

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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//    }
}