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
class CardListAdapter : ListAdapter<Card, CardListAdapter.ViewHolder>(CardsComparator()) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.card_text_view_name)
        }

        fun bind(text: String?) {
            textView.text = text
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.card_list_recycler_view_item, viewGroup, false)

        val holder = CardListAdapter.ViewHolder(view)

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
        viewHolder.bind(current.title) // ここにも注意だ!
    }

    // Return the size of your dataset (invoked by the layout manager)
//    override fun getItemCount() = dataSet.size
//    override fun getItemCount() = 10

    class CardsComparator : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem.title == newItem.title // ここに要注意!
        }
    }

    var itemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(holder: CardListAdapter.ViewHolder)
    }
}