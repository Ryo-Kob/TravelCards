package com.rkb.travelcards.ui.planEditor1

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rkb.travelcards.Card
import com.rkb.travelcards.CardSuite
import com.rkb.travelcards.R

class PlanAdapter : ListAdapter<CardSuite, PlanAdapter.ViewHolder>(CardsComparator()) {
    lateinit var card : List<Card>

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var card : List<Card>
//        val textViewDescription: TextView

        companion object {
            fun create(parent: ViewGroup, viewType: Int) : ViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(when(viewType) {
                        CardSuite.VIEW_TYPE_EMPTY -> R.layout.plan_recycler_view_item_empty
                        CardSuite.VIEW_TYPE_CARD -> R.layout.plan_recycler_view_item
                        else ->  R.layout.plan_recycler_view_item
                    }, parent, false)
                return ViewHolder(view)
            }
        }

        @SuppressLint("ResourceAsColor")
        fun bind(cs: CardSuite) {
            // TODO: 特定のcardIdを持つCardの情報を取得し、textViewName等に反映させたい！
//            textViewName.text = card[cardId].title
//            textViewName.text = card[cs.cardId].title
            if (cs.isBlank) {
                val params : ViewGroup.LayoutParams = itemView.layoutParams
                params.height = cs.timer*6
                val textViewName = itemView.findViewById<TextView>(R.id.card_text_view_name)
//                textViewName.text = cs.text
////                val textViewTime = itemView.findViewById<TextView>(R.id.card_text_view_time)
//                textViewName.text = "${cs.startTime/60}:${cs.startTime%60}"
                textViewName.text = ""
            }else{
                val params : ViewGroup.LayoutParams = itemView.layoutParams
                params.height = cs.timer*6
                val textViewName = itemView.findViewById<TextView>(R.id.card_text_view_name)
                textViewName.text = cs.text
                val textViewTime = itemView.findViewById<TextView>(R.id.card_text_view_time)
                textViewTime.text = "%2d:%02d - %2d:%02d".format(cs.startTime/60, cs.startTime%60, cs.startTime/60+cs.timer/60, cs.startTime%60+cs.timer%60)
//                textViewTime.text = "${cs.timer} 分"

                // 時刻照合警告
                val cardView = itemView.findViewById<CardView>(R.id.card_view)
                if (cs.isStartTimeFixed) {
                    if (cs.startTimeOriginal != cs.startTime) {
//                        cardView.setCardBackgroundColor(R.color.pale_red)
                        cardView.setCardBackgroundColor(Color.rgb(255, 200, 200))
                    }else{
                        cardView.setCardBackgroundColor(Color.WHITE)
                    }
                }else{
                    cardView.setCardBackgroundColor(Color.WHITE)
                }
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(
                when(viewType) {
                    CardSuite.VIEW_TYPE_EMPTY -> R.layout.plan_recycler_view_item_empty
                    CardSuite.VIEW_TYPE_CARD -> R.layout.plan_recycler_view_item
                    else -> R.layout.plan_recycler_view_item
                }, viewGroup, false
            )

        val holder = ViewHolder(view)
        holder.card = card

//        view.setOnClickListener{       // リスナーの実装
//            itemClickListener?.onItemClick(holder)
//        }

        return holder
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

//        viewHolder.textView.text = "てきすと"//dataSet[position]
        val current = getItem(position)
        viewHolder.bind(current) // ここにも注意だ!
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
}