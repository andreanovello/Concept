package com.example.concept2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    lateinit var loginButton: Button
    lateinit var signinButton: TextView
    lateinit var usernameBox: EditText
    lateinit var passwordBox: EditText
    lateinit var username: String
    lateinit var password: String

    private var mAuth: FirebaseAuth? = null
    private var cloudFirestore = Firebase.firestore

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
                Toast.makeText(this, "Empty username or password", Toast.LENGTH_LONG).show()
            }
        }


        signinButton.setOnClickListener{
            username = usernameBox.text.toString()
            password = passwordBox.text.toString()

            createNewAccount(username,password)
        }

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        //updateUI(currentUser)
    }

    private fun loginUser() {
        mAuth!!.signInWithEmailAndPassword(username + "@gmail.com", password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, LoggedIn::class.java))
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun createNewAccount(username: String, password: String) {
        //val friendsArray=
        val user = hashMapOf(
            "Username" to username,
            //"Friends" to friendsArray,
            "ProfilePicture" to "",
            "Online" to false
        )

        if ( username.isNotEmpty() && password.isNotEmpty() ) {
            mAuth!!.createUserWithEmailAndPassword(username + "@gmail.com", password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(username).build()
                        mAuth!!.currentUser?.updateProfile(profileUpdates)
                        //CREATING DATAS IN CLOUD STORAGE
                        cloudFirestore.collection("accounts").add(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "You are now registered! Log in and start to play Concept", Toast.LENGTH_LONG).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Something went wrong during the registration. Try again",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    } else {
                        Toast.makeText(this, "An error occurred during the authentication", Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show()
        }
    }
}