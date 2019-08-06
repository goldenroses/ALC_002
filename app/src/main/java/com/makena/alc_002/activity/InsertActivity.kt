package com.makena.alc_002.activity

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.makena.alc_002.activity.managers.FirebaseManager
import com.makena.alc_002.activity.models.Deal
import kotlinx.android.synthetic.main.insert_content.*
import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.makena.alc_002.R
import com.squareup.picasso.Picasso


class InsertActivity: AppCompatActivity() {

    private var _firebaseDb: FirebaseDatabase? = null
    private var _dbRefenrece: DatabaseReference? = null
    private var deal: Deal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.insert_content)

        _firebaseDb = FirebaseManager._firebaseDb
        _dbRefenrece = FirebaseManager._firebaseReference!!.child("traveldeals")

        val dealDataInJson = intent.getStringExtra("deal")
        deal = Gson().fromJson<Deal>(dealDataInJson, Deal::class.java)

        if(deal == null) {
            deal = Deal()
        }

        txtTitle.setText(deal!!.dealTitle)
        txtDescription.setText(deal!!.dealDescription)
        txtPrice.setText(deal!!.dealPrice)
        if (deal!!.imageUrl != null){ showImage(deal!!.imageUrl!!)}

        setSupportActionBar(toolbar)
        toolbar.title = "Add Travel Deal"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Add Travel Deal"

        btnSave.setOnClickListener{
            saveDeal()
            Toast.makeText(this, "Deal saved", Toast.LENGTH_SHORT).show()
            clean()
        }

        btnUpload.setOnClickListener {
            val intent:Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/jpeg")
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, "Insert picture"), 78)

        }
    }

    private fun clean() {
        txtTitle.text.clear()
        txtDescription.text.clear()
        txtPrice.text.clear()
    }

    fun showImage(url: String) {

        if(url.isEmpty() == false) {
            val width = Resources.getSystem().displayMetrics.widthPixels
            Picasso.with(this).load(url).resize(width, width*2/3).centerCrop().into(imageView)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICTURE_RESULT_CODE && resultCode == Activity.RESULT_OK) {

            val uri: Uri = data!!.data
            val storageReference: StorageReference = FirebaseManager._firebaseStorageReference!!.child(uri.lastPathSegment)
            storageReference.putFile(uri).addOnSuccessListener(object: OnSuccessListener<UploadTask.TaskSnapshot> {
                override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {
                    val downloadUri = taskSnapshot!!.storage.downloadUrl
                    if(downloadUri.isSuccessful) {
                        val downloadPath = downloadUri.result.toString()
                        Log.d("UPLOAD IMAGE", "PICTURE_RESULT_CODE : ${PICTURE_RESULT_CODE} : resultCode : ${downloadPath}")
                        deal!!.imageUrl = downloadPath
                        showImage(downloadPath)

                    }else {
                        Log.d("UPLOAD IMAGE", "failure!")
                        Toast.makeText(imageView.context,"Image upload failed, try again", Toast.LENGTH_LONG).show()

                    }
                }
            })
        }

    }

    private fun saveDeal() {
        deal!!.dealTitle = txtTitle.text.toString()
        deal!!.dealDescription = txtDescription.text.toString()
        deal!!.dealPrice = txtPrice.text.toString()

        if(deal!!.id == null) {
            FirebaseManager._firebaseReference!!.push().setValue(deal)
            backToList()
            finish()

        } else {
            FirebaseManager._firebaseReference!!.child(deal!!.id!!).setValue(deal)
            backToList()
            finish()

        }
    }

    fun deleteDeal() {
        if(deal == null) {
            Toast.makeText(this, "Deal not found", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseManager._firebaseReference!!.child(deal!!.id!!).removeValue()
        backToList()
        finish()
    }

    fun backToList() {
        val intent: Intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menu = menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.action_delete -> {
                deleteDeal()
                backToList()
                finish()

            }
        }
        return  true
    }

    companion object {
        const val PICTURE_RESULT_CODE = 78

    }
}
