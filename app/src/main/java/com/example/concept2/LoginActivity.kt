package com.example.concept2

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    lateinit var loginButton: Button
    lateinit var signinButton: TextView
    lateinit var usernameBox: EditText
    lateinit var passwordBox: EditText
    lateinit var username: String
    lateinit var password: String

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()


        signinButton = findViewById<TextView>(R.id.sign_in_button)
        loginButton = findViewById<Button>(R.id.login_button)
        usernameBox = findViewById<EditText>(R.id.username)
        passwordBox = findViewById<EditText>(R.id.password)


        loginButton.setOnClickListener {
            username = usernameBox.text.toString()
            password = passwordBox.text.toString()


            if (username.isNotEmpty() && password.isNotEmpty()){
                loginUser()
            }else {
                Toast.makeText(this, "Empty email or password", Toast.LENGTH_LONG).show()
            }
        }


        signinButton.setOnClickListener{
            username = usernameBox.text.toString()
            password = passwordBox.text.toString()

            if ( username.isNotEmpty() && password.isNotEmpty() ){

                mAuth!!.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "You are now registered! Log in and start to play Concept", Toast.LENGTH_LONG).show()
                        }
                        else {
                            Toast.makeText(this, "An error occured during the registration", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        //updateUI(currentUser)
    }

    private fun loginUser() {
        mAuth!!.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, LoggedIn::class.java))
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show()
                }
            }
    }

}