package com.makena.alc_002.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.makena.alc_002.R
import com.makena.alc_002.activity.models.Deal
import kotlinx.android.synthetic.main.insert_content.*

class InsertActivity: AppCompatActivity() {

    private var _firebaseDb: FirebaseDatabase? = null
    private var _dbRefenrece: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.insert_content)

        _firebaseDb = FirebaseDatabase.getInstance()
        _dbRefenrece = _firebaseDb!!.reference.child("traveldeals")

        setActionBar(toolbar)

        btnSave.setOnClickListener{
            saveDeal()
            Snackbar.make(it, "Deal saved", Snackbar.LENGTH_SHORT)
            clean()
        }
    }

    private fun clean() {

        txtTitle.text.clear()
        txtDescription.text.clear()
        txtPrice.text.clear()

    }

    private fun saveDeal() {
        val title: String = txtTitle.text.toString()
        val description: String = txtDescription.text.toString()
        val price: String = txtPrice.text.toString()

        val deal = Deal(title,description,price)

        _dbRefenrece!!.push().setValue(deal)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_save, menu)
        MenuItem.SHOW_AS_ACTION_ALWAYS
        return true
    }
}
