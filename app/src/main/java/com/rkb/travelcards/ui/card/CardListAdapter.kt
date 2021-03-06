package com.rkb.travelcards.ui.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rkb.travelcards.Card
import com.rkb.travelcards.R


//class CardListAdapter : RecyclerView.Adapter<CardListAdapter.ViewHolder>() {
class CardListAdapter : ListAdapter<Card, CardListAdapter.CardViewHolder>(CardsComparator()) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewName: TextView = view.findViewById(R.id.card_text_view_name)
        private val textViewDescription: TextView = view.findViewById(R.id.card_text_view_time)
        private val cardView: View = view

        fun bind(card: Card) {
            textViewName.text = card.title
            textViewDescription.text = card.strDateTime

//            val params : ViewGroup.LayoutParams = cardView.layoutParams
//            params.height = 100 + card.timerHour * 100
//            cardView.layoutParams = params
        }

        companion object {
            fun create(parent: ViewGroup): CardViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_list_recycler_view_item, parent, false)
                return CardViewHolder(view)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(
            viewGroup
        )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: CardViewHolder, position: Int) {
        val current = getItem(position)
        viewHolder.bind(current) // ここにも注意だ!
    }

    // Return the size of your dataset (invoked by the layout manager)
//    override fun getItemCount() = currentList.size
//    override fun getItemCount() = 10

    class CardsComparator : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem.title == newItem.title // TODO: 全アイテムを比較するようにする. もしくは, この関数自体消しちゃう.
        }
    }

//    var itemClickListener: OnItemClickListener? = null
//    interface OnItemClickListener {
//        fun onItemClick(holder: CardListAdapter.ViewHolder)
//    }
}