package com.makena.alc_002.activity.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makena.alc_002.R
import com.makena.alc_002.activity.InsertActivity
import com.makena.alc_002.activity.models.Deal
import com.squareup.picasso.Picasso
import java.util.*


class CardRecyclerAdapter : RecyclerView.Adapter<CardHolder>() {
    val deals: ArrayList<Deal> = ArrayList<Deal>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_holder, parent, false)

        return CardHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        var currentItem = deals[position]
        holder.cardTitle.text = currentItem.dealTitle
        holder.cardDecription.text = currentItem.dealDescription
        holder.upvote.text = currentItem.upvotes
        holder.downvote.text = currentItem.downvotes

        holder.updateCurrentItems(currentItem)
    }


}

class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val cardTitle = itemView.findViewById<TextView>(R.id.cardTitle)
    val cardDecription = itemView.findViewById<TextView>(R.id.cardDescription)
    val imageLocation = itemView.findViewById<ImageView>(R.id.cardImageView)
    val upvote = itemView.findViewById<TextView>(R.id.upvoteImageView)
    val downvote = itemView.findViewById<TextView>(R.id.downvoteImageView)

    init {
        itemView.setOnClickListener {
            val intent: Intent = Intent(itemView.context, InsertActivity::class.java)
        }
    }

    fun updateCurrentItems(item: Deal) {

        if(item.imageUrl != null) {
            Picasso.with(itemView.context).load(item.imageUrl)
        }
    }

}
