package com.makena.alc_002.activity.managers

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.makena.alc_002.activity.models.Deal

class FirebaseManager {
    companion object {
        var _firebaseDb: FirebaseDatabase? = null
        var _firebaseReference: DatabaseReference? = null
        var _firebaseManager: FirebaseManager? = null
        var _deals: ArrayList<Deal> = ArrayList<Deal>()

        fun openDbRef(dbRef: String) {
            if(_firebaseManager == null) {
                _firebaseManager = FirebaseManager()
                _firebaseDb = FirebaseDatabase.getInstance()

            }
            _deals = ArrayList<Deal>()
            _firebaseReference = _firebaseDb!!.reference.child(dbRef)

        }
    }

}
