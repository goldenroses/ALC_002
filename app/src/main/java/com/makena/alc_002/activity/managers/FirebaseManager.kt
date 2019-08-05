package com.makena.alc_002.activity.managers

import android.content.Intent
import android.util.Log
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.makena.alc_002.activity.InsertActivity
import com.makena.alc_002.activity.models.Deal
import kotlinx.android.synthetic.main.activity_home.*

class FirebaseManager {

    companion object {
        private val TAG: String = "FirebaseManager";
        var _isAdmin: Boolean = false

        var _firebaseDb: FirebaseDatabase? = null
        var _firebaseAuth: FirebaseAuth? = null
        var _firebaseReference: DatabaseReference? = null
        var _firebaseManager: FirebaseManager? = null
        var _user: FirebaseUser? = null
        var _authListener: FirebaseAuth.AuthStateListener? = null

        var _deals: ArrayList<Deal> = ArrayList<Deal>()

        fun openDbRef(dbRef: String) {
            if(_firebaseManager == null) {
                _firebaseManager = FirebaseManager()
                _firebaseDb = FirebaseDatabase.getInstance()
                _firebaseAuth = FirebaseAuth.getInstance()
                _user = FirebaseAuth.getInstance().currentUser
                if (_user!=null) {
                    isAdmin(_user!!.uid)
                }

            }
            _deals = ArrayList<Deal>()
            _firebaseReference = _firebaseDb!!.reference.child(dbRef)

        }

        fun isAdmin(uid: String) {
            Log.d(TAG, "Checking if isAdmin : ${_isAdmin}")
            val adminDbRef: DatabaseReference = _firebaseDb!!.reference.child("admins").child(uid)
            val listener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.d(TAG, "onCancelled")

                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    _isAdmin = true

                    Log.d(TAG, "This user is an admin")
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            }

            adminDbRef.addChildEventListener(listener)
            Log.d(TAG, "Checking if isAdmin after listener again: ${_isAdmin}")

        }

        // Authentication
        fun authenticateUser(): Boolean {
            Log.d(TAG, "setupFirebaseAuth: started.")
            var isLoggedIn: Boolean = false
            _user = FirebaseAuth.getInstance().currentUser

                if (_user != null) {
                    // email verification - disabled user.isEmailVerified()
                    if (true) {

                        Log.d(TAG, "onAuthStateChanged:signed_in:::::" + _user!!.getUid());

                        isLoggedIn = true

                    } else {
                        logout()
                    }
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out")
                }

            return isLoggedIn
        }

        fun attachListener() {
            _firebaseAuth!!.addAuthStateListener(_authListener!!)
        }

        fun detachListener() {
            _firebaseAuth!!.removeAuthStateListener(_authListener!!)
        }

        fun logout(): Boolean {
            FirebaseAuth.getInstance().signOut()

            return true
        }
    }

}
