package com.rkb.travelcards.ui.planEditor2

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rkb.travelcards.R
import com.rkb.travelcards.TravelCardsApplication

class DrawerPlanFragment2 : Fragment() , DrawerPlanItemClickListener2.OnRecyclerClickListener {
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
        val adapter = DrawerPlanAdapter2()
        recyclerView.adapter = adapter
        recyclerView.addOnItemTouchListener(DrawerPlanItemClickListener2(context!!, recyclerView, this))
        recyclerView.layoutManager = LinearLayoutManager(context)

        drawerViewModel.allCards.observe(viewLifecycleOwner, Observer { cards ->
            // Update the cached copy of the words in the adapter.
            cards?.let { adapter.submitList(it) }
        })

    }

//    override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenuInfo?) {
//        // 長押しされたメッセージ位置を取得し、
//        // RecyclerViewで表示させているListのindexとして扱い、
//        // ユーザーIDを取得してメニューの出し分けを実現
//        if (list.get(adapter.getPosition()).getUserId() === loginUserId) {
//            menu.add(Menu.NONE, 1, Menu.NONE, "コピー")
//            menu.add(Menu.NONE, 2, Menu.NONE, "編集")
//            menu.add(Menu.NONE, 3, Menu.NONE, "削除")
//        } else {
//            menu.add(Menu.NONE, 1, Menu.NONE, "コピー")
//        }
//    }

    override fun onItemClick(view: View, position: Int) {
        Log.v("", "cl: pos=${position}")

    }
    override fun onDoubleClick(view: View, position: Int) {
        Log.v("", "double-cl: pos=${position}")

    }
    override fun onItemLongClick(view: View, position: Int) {
        Log.v("", "long: pos=${position}")
        val data = bundleOf("cardId" to position)
        parentFragmentManager.setFragmentResult("pickingCardFromDrawer", data)
    }
}