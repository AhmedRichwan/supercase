package com.rashwan.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_welcome.*

class Welcome : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_welcome)
        btnsignup.setOnClickListener {

            startActivity(Intent(this, SignUp::class.java))
        }
        mAuth = FirebaseAuth.getInstance()

        btnlogin.setOnClickListener {
            var email = edID.text.toString()
            var password = edpass.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {

                mAuth?.signInWithEmailAndPassword(email, password)

                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            Toast.makeText(applicationContext,
                                mAuth?.currentUser?.displayName.toString(),
                                Toast.LENGTH_LONG).show()
                        } else Toast.makeText(applicationContext,
                            it.exception.toString(),
                            Toast.LENGTH_LONG).show()


                    }
                mAuth?.currentUser?.email.toString()

            }
            if (email.isEmpty() && password.isEmpty()) {
                startActivity(Intent(this, SignUp::class.java))

            }
        }
    }


}

