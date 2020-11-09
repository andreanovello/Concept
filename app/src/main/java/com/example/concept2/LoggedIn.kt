 package com.example.concept2

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoggedIn : AppCompatActivity() {
    //lateinit var menuButton: ActionBarDrawerToggle
    lateinit var conceptButton: Button

    private var mAuth: FirebaseAuth? = null
    private lateinit var user: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)

        mAuth = FirebaseAuth.getInstance()
        user = mAuth!!.currentUser!!

        initHomepage()
    }


    private fun initHomepage(){
        /*
        val menuView = findViewById<NavigationView>(R.id.menu_view)
        menuView.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.item1 -> Toast.makeText(applicationContext, "Clicked ITEM 1", Toast.LENGTH_SHORT).show()
                R.id.item2 -> Toast.makeText(applicationContext, "Clicked ITEM 2", Toast.LENGTH_SHORT).show()
                R.id.item3 -> Toast.makeText(applicationContext, "Clicked ITEM 3", Toast.LENGTH_SHORT).show()
            }
            true
        }

        val drawerLayout = findViewById<DrawerLayout>(R.id.main_view)

        menuButton = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(menuButton)
        menuButton.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        */
        activateConceptMenu()

    }
    /*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (menuButton.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    */

    private fun activateConceptMenu(){
        conceptButton = findViewById<Button>(R.id.menu_button)
        conceptButton.setOnClickListener {
            /*val popUpMenuView = LayoutInflater.from(this).inflate(R.layout.menu_pop_up, null)
            val popUpMenuBuilder = AlertDialog.Builder(this).setView(popUpMenuView).setTitle("Menu")
            popUpMenuBuilder.setView(popUpMenuView)
            val popUpMenuAlert = popUpMenuBuilder.show()

            val closeButton = findViewById<ImageView>(R.id.close_menu_button)
            closeButton.setOnClickListener {
                popUpMenuAlert.dismiss()
            }*/

            val menuDialog = Dialog(this)
            menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            menuDialog.setContentView(R.layout.menu_pop_up)
            menuDialog.setTitle("Menu")

            menuDialog.findViewById<ImageView>(R.id.close_menu_button).setOnClickListener {
                menuDialog.cancel()
            }

            menuDialog.show()
        }
    }



}