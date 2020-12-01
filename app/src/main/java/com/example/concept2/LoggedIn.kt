 package com.example.concept2

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.Manifest
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.TextView
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*


 class LoggedIn : AppCompatActivity() {
    lateinit var conceptButton: Button

    private var mAuth: FirebaseAuth? = null
    private lateinit var user: FirebaseUser

    private var galleryPermissionCode = 1001
    private var imagePickCode = 1000

    private var cloudFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)

        mAuth = FirebaseAuth.getInstance()
        user = mAuth!!.currentUser!!

        Toast.makeText(this, "Welcome " + user.displayName, Toast.LENGTH_SHORT).show()
        initHomepage()
    }


    private fun initHomepage(){
        activateConceptMenu()
        //RENDER PROFILE PICTURE MA PASSANDO COME ARGOMENTO UN StorageReference AL POSTO DI UN FILE URI
    }


    private fun activateConceptMenu(){
        conceptButton = findViewById<Button>(R.id.menu_button)
        conceptButton.setOnClickListener {

            val menuDialog = Dialog(this)
            menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            menuDialog.setContentView(R.layout.menu_pop_up)
            menuDialog.setTitle("Menu")

            //CLOSE MENU BUTTON
            menuDialog.findViewById<ImageView>(R.id.close_menu_button).setOnClickListener {
                menuDialog.cancel()
            }
            //USERNAME LABEL
            menuDialog.findViewById<TextView>(R.id.username_in_menu).text = user.displayName
            //PROFILE PICTURE
            val profilePicture = menuDialog.findViewById<ImageView>(R.id.profile_picture)
            Picasso.get().load(user.photoUrl).into(profilePicture)

            menuDialog.show()
            activateChangePictureListener(menuDialog)
        }
    }

    private fun activateChangePictureListener(dialog: Dialog){
        dialog.findViewById<ImageView>(R.id.profile_picture).setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, galleryPermissionCode);
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

    }

    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, imagePickCode)
    }



     override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults)

         when (requestCode){
             galleryPermissionCode -> {
                 if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                     pickImageFromGallery()
                 }
                 else {
                     Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                 }
             }
         }
     }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)

         if (resultCode == Activity.RESULT_OK && requestCode == imagePickCode){
             updateProfilePicture(data?.data!!)
         }
     }

     private fun updateProfilePicture(profilePictureUri: Uri){
         val profileUpdate = UserProfileChangeRequest.Builder().setPhotoUri(profilePictureUri).build()
         try{
             user.updateProfile(profileUpdate)
                 .addOnCompleteListener { task ->
                     if (task.isSuccessful) {
                         Toast.makeText(this@LoggedIn, "Succesfully updated!", Toast.LENGTH_SHORT).show()
                     }
                 }
         } catch (e:Exception){
             Toast.makeText(this@LoggedIn, e.message, Toast.LENGTH_SHORT).show()
         }
     }


}
