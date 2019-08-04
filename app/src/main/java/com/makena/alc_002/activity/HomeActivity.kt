package com.makena.alc_002.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.makena.alc_002.R
import com.makena.alc_002.activity.adapters.CardRecyclerAdapter
import com.makena.alc_002.activity.managers.FirebaseManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : AppCompatActivity() {
    private val TAG: String = "HomeActivity"
    val adapter: CardRecyclerAdapter? = CardRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        FirebaseManager.openDbRef("traveldeals")

        recycler!!.layoutManager = LinearLayoutManager(this)
        recycler!!.adapter = adapter

        fab.setOnClickListener { view ->
            val intent: Intent = Intent(this, InsertActivity::class.java)
            startActivity(intent)
        }
    }
}
