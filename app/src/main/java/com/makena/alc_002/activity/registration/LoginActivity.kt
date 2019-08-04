package com.makena.alc_002.activity.registration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.makena.alc_002.R
import com.makena.alc_002.activity.HomeActivity
import com.makena.alc_002.activity.InsertActivity
import com.makena.alc_002.activity.managers.FirebaseManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val TAG: String = "LoginActivity";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login);

        setupFirebaseAuth()

        FirebaseManager._authListener = FirebaseAuth.AuthStateListener {
            val user = FirebaseManager._user
            if (user != null) {
                // email verification - disabled user.isEmailVerified()
                if (true) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_SHORT)
                        .show();

                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Email is not Verified\nCheck your Inbox", Toast.LENGTH_SHORT)
                        .show()
                    FirebaseAuth.getInstance().signOut()
                }
            }
        }

        buttonSignIn.setOnClickListener {

            //check if the fields are filled out
            if (!isEmpty(editTextEmail!!.text.toString()) && !isEmpty(editTextPassword!!.text.toString())) {
                Log.d(TAG, "onClick: attempting to authenticate.")

                //check if the fields are filled out
                showDialog()

                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    editTextEmail.text.toString(),
                    editTextPassword.text.toString()
                )
                    .addOnCompleteListener {
                        hideDialog()
                        if(it.isSuccessful) {
                            val intent: Intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        }
                        if(it.isCanceled) {
                            Toast.makeText(this, "Login/Username Invalid", Toast.LENGTH_SHORT).show()

                        }
                    }.addOnFailureListener {
                        fun onFailure(e: Exception) {
                            hideDialog()
                            Toast.makeText(this, "Login/Username Invalid", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
            }
        }
        buttonSignInWithGoogle.setOnClickListener {

            // Configure Google Sign In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            //check if the fields are filled out
            if (!isEmpty(editTextEmail!!.text.toString()) && !isEmpty(editTextPassword!!.text.toString())) {
                Log.d(TAG, "onClick: attempting to authenticate.")

                //check if the fields are filled out
                showDialog()

                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    editTextEmail.text.toString(),
                    editTextPassword.text.toString()
                )
                    .addOnCompleteListener {
                        hideDialog()
                        if(it.isSuccessful) {
                            val intent: Intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        }
                        if(it.isCanceled) {
                            Toast.makeText(this, "Login/Username Invalid", Toast.LENGTH_SHORT).show()

                        }
                    }.addOnFailureListener {
                        fun onFailure(e: Exception) {
                            hideDialog()
                            Toast.makeText(this, "Login/Username Invalid", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
            }
        }

            link_register_with_email.setOnClickListener {
                val intent: Intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            link_forgot_password.setOnClickListener {
                val dialog: PasswordResetDialog = PasswordResetDialog()
                dialog.show(supportFragmentManager, "PasswordResetDialog")
            }

            link_resend_verification_email.setOnClickListener {
                val dialog: ResendVerificationDialog = ResendVerificationDialog()
                dialog.show(supportFragmentManager, "ResendVerificationDialog")
            }

        }

    private fun isEmpty(string: String): Boolean {
        return string.equals("");
    }


    private fun showDialog() {
        progressBar!!.setVisibility(View.VISIBLE);

    }

    private fun hideDialog() {
        if (progressBar!!.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private fun hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private fun setupFirebaseAuth() {
        val loggin = FirebaseManager.authenticateUser()

        if(loggin == true) {
            Toast.makeText(this, "Authenticated with: " + FirebaseManager._user!!.getEmail(), Toast.LENGTH_SHORT)
                .show();

            val intent = Intent(this, InsertActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT)
                .show()
            hideDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(FirebaseManager._authListener!!)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(FirebaseManager._authListener!!)

    }
}
