package com.makena.alc_002.activity.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.gson.Gson
import com.makena.alc_002.R
import com.makena.alc_002.activity.InsertActivity
import com.makena.alc_002.activity.managers.FirebaseManager
import com.makena.alc_002.activity.models.Deal
import com.squareup.picasso.Picasso
import java.util.*

class CardRecyclerAdapter : RecyclerView.Adapter<CardHolder>() {
    val TAG: String = "CardRecyclerAdapter"
    var deals: ArrayList<Deal>? = FirebaseManager._deals

    init {
        FirebaseManager.openDbRef("traveldeals")
        val listener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                notifyDataSetChanged()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                notifyDataSetChanged()
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val deal = dataSnapshot.getValue(Deal::class.java)
                deal!!.id = dataSnapshot.key
                deals!!.add(deal)
                notifyItemInserted(deals!!.size - 1)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val deal = dataSnapshot.getValue(Deal::class.java)
                deal!!.id = dataSnapshot.key
                deals!!.remove(deal)
                notifyDataSetChanged()
            }
        }

        FirebaseManager._firebaseReference!!.addChildEventListener(listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_holder, parent, false)

        return CardHolder(itemView)
    }

    override fun getItemCount(): Int {
        return deals!!.size
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        var currentItem = deals!![position]
        holder.cardTitle.text = currentItem.dealTitle
        holder.cardDecription.text = currentItem.dealDescription
        holder.price.text = currentItem.dealPrice
        holder.upvote.text = currentItem.upvotes
        holder.downvote.text = currentItem.downvotes

        holder.updateCurrentItems(currentItem)
    }

}

class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cardTitle = itemView.findViewById<TextView>(R.id.cardTitle)
    val cardDecription = itemView.findViewById<TextView>(R.id.cardDescription)
    val imageLocation = itemView.findViewById<ImageView>(R.id.cardImageView)
    val price = itemView.findViewById<TextView>(R.id.textPrice)
    val upvote = itemView.findViewById<TextView>(R.id.textUpvote)
    val downvote = itemView.findViewById<TextView>(R.id.textDownvote)
    var deals: ArrayList<Deal>? = FirebaseManager._deals

    init {
        itemView.setOnClickListener {
            val pos = adapterPosition
            val deal = Gson().toJson(deals!![pos])
            Log.d("--","H: ${deal}")

            val intent: Intent = Intent(itemView.context, InsertActivity::class.java)
            intent.putExtra("deal", deal)
            itemView.context.startActivity(intent)
        }

        FirebaseManager.openDbRef("traveldeals")
        val listener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("CardRecyclerAdapter", "issue occured")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.d("onChildChanged", "i/child has been updated")
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val deal = dataSnapshot.getValue(Deal::class.java)
                deal!!.id = dataSnapshot.key
                deals!!.add(deal)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val deal = dataSnapshot.getValue(Deal::class.java)
                deal!!.id = dataSnapshot.key
                deals!!.remove(deal)
            }
        }

        FirebaseManager._firebaseReference!!.addChildEventListener(listener)
    }

    fun updateCurrentItems(item: Deal) {

        cardTitle.text = item.dealTitle
        cardDecription.text = item.dealDescription
        price.text = item.dealPrice
        if(item.imageUrl != null) showImage(item.imageUrl!!)
        upvote.text = item.upvotes
        downvote.text = item.downvotes
        if(item.imageUrl != null) {
            Picasso.with(itemView.context).load(item.imageUrl).into(imageLocation)
        }
    }

    fun showImage(url: String) {

        if(url.isEmpty() == false) {
            Picasso.with(itemView.context).load(url).resize(80, 80).centerCrop().into(imageLocation)
        }
    }

}
