package com.makena.alc_002.activity.managers

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.makena.alc_002.activity.models.Deal

class FirebaseManager {

    companion object {
        private val TAG: String = "FirebaseManager";

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

            }
            _deals = ArrayList<Deal>()
            _firebaseReference = _firebaseDb!!.reference.child(dbRef)

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
