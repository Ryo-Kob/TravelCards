package com.rkb.travelcards.ui.planEditor2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rkb.travelcards.R

class PlanAdapterT2 : ListAdapter<Int, PlanAdapterT2.ViewHolder>(CardsComparator()) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            fun create(parent: ViewGroup, viewType: Int) : ViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.plan_recycler_view_timeline_item, parent, false)
                return ViewHolder(view)
            }
        }

        fun bind(card: Int) {
            // TODO: 特定のcardIdを持つCardの情報を取得し、textViewName等に反映させたい！
//            textViewName.text = card[cardId].title
//            textViewName.text = card[cs.cardId].title
            val params : ViewGroup.LayoutParams = itemView.layoutParams
            params.height = 15*6
            val textViewName = itemView.findViewById<TextView>(R.id.card_text_view_name)
            if (card%60 == 0) {
                textViewName.text = "${card/60}:00"
            }else{
                textViewName.text = ""
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.plan_recycler_view_timeline_item, viewGroup, false
            )
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val current = getItem(position)
        viewHolder.bind(current)
    }

    class CardsComparator : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }
}