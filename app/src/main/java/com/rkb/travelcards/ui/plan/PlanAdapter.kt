package com.rkb.travelcards.ui.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rkb.travelcards.Card
import com.rkb.travelcards.CardSuite
import com.rkb.travelcards.R

class PlanAdapter() : ListAdapter<CardSuite, PlanAdapter.ViewHolder>(CardsComparator()) {
    var card = mutableListOf<Card>()

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cards = mutableListOf<Card>()
        val textViewName: TextView
//        val textViewDescription: TextView

        init {
            // Define click listener for the ViewHolder's View.
            textViewName = view.findViewById(R.id.card_text_view_name)
//            textViewDescription = view.findViewById(R.id.card_text_view_time)
        }

        fun bind(cardId: Int) {
            // TODO: 特定のcardIdを持つCardの情報を取得し、textViewName等に反映させたい！
//            textViewName.text = vm.getCard(cardId).toString()
            textViewName.text = cards[cardId]
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.plan_recycler_view_item, viewGroup, false)

        val holder = PlanAdapter.ViewHolder(view)

        view.setOnClickListener{       // リスナーの実装
            itemClickListener?.onItemClick(holder)
        }

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

//        viewHolder.textView.text = "てきすと"//dataSet[position]
        val current = getItem(position)
        viewHolder.bind(current.cardId) // ここにも注意だ!
    }

    // Return the size of your dataset (invoked by the layout manager)
//    override fun getItemCount() = dataSet.size
//    override fun getItemCount() = 10

    class CardsComparator : DiffUtil.ItemCallback<CardSuite>() {
        override fun areItemsTheSame(oldItem: CardSuite, newItem: CardSuite): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CardSuite, newItem: CardSuite): Boolean {
            return oldItem.cardId == newItem.cardId // TODO: 全アイテムを比較するようにする. もしくは, この関数自体消しちゃう.
        }
    }

    var itemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(holder: PlanAdapter.ViewHolder)
    }
}