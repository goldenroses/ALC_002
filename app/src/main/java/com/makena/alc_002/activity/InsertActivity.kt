package com.makena.alc_002.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.makena.alc_002.R
import kotlinx.android.synthetic.main.insert_content.*

class InsertActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.insert_content)

        setActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_save, menu)
        MenuItem.SHOW_AS_ACTION_ALWAYS
        return true
    }
}