package com.makena.alc_002.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.makena.alc_002.R
import com.makena.alc_002.activity.adapters.CardRecyclerAdapter
import com.makena.alc_002.activity.managers.FirebaseManager
import com.makena.alc_002.activity.registration.LoginActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : AppCompatActivity() {
    private val TAG: String = "HomeActivity"
    val adapter: CardRecyclerAdapter? = CardRecyclerAdapter()

    private var _firebaseDb: FirebaseDatabase? = null
    private var _dbRefenrece: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        _firebaseDb = FirebaseManager._firebaseDb
        _dbRefenrece = FirebaseManager._firebaseReference!!.child("traveldeals")

        recycler!!.layoutManager = LinearLayoutManager(this)
        recycler!!.adapter = adapter

        fab.setOnClickListener { view ->
            val intent: Intent = Intent(this, InsertActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menu = menuInflater.inflate(R.menu.menu_home, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.action_logout -> {
                FirebaseManager.logout()
                finish()
                val intent: Intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        return true
    }

    override fun onStart() {
        super.onStart()
        if(FirebaseManager._authListener != null) FirebaseManager.attachListener()

    }

    override fun onStop() {
        super.onStop()
        if(FirebaseManager._authListener != null) FirebaseManager.detachListener()

    }

    override fun onPause() {
        super.onPause()
        if(FirebaseManager._authListener != null) FirebaseManager.detachListener()
    }

    override fun onResume() {
        super.onResume()
        if(FirebaseManager._authListener != null) FirebaseManager.attachListener()
    }

}
