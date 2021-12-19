package com.rkb.travelcards

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


//class CardListAdapter : ListAdapter<Card, CardListAdapter.CardViewHolder>(
//        CardsComparator()
//) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
//        return CardViewHolder.create(
//                parent
//        )
//    }
//
//    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
//        val current = getItem(position)
//        holder.bind(current.card)
//    }
//
//    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val cardItemView: TextView = itemView.findViewById(R.id.textView)
//
//        fun bind(text: String?) {
//            cardItemView.text = text
//        }
//
//        companion object {
//            fun create(parent: ViewGroup): CardViewHolder {
//                val view: View = LayoutInflater.from(parent.context)
//                        .inflate(R.layout.recyclerview_item, parent, false)
//                return CardViewHolder(view)
//            }
//        }
//    }
//
//    class CardsComparator : DiffUtil.ItemCallback<Card>() {
//        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
//            return oldItem === newItem
//        }
//
//        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
//            return oldItem.card == newItem.card
//        }
//    }
//}