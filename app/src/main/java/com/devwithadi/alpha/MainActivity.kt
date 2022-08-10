package com.devwithadi.alpha

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    lateinit var name: EditText
    lateinit var deiscription: EditText
    lateinit var email: EditText
    lateinit var button: Button
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var firestore:FirebaseFirestore
    lateinit  var recyclerView:RecyclerView
    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser==null){

            firebaseAuth.signInAnonymously().addOnCompleteListener {
                Log.i("logs","Sigin Success")
            }


        }

    }
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val window: Window = this.getWindow()
        // Change Status Bar Color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black))


        // Intialized Firebase
        FirebaseApp.initializeApp(this)
        firebaseAuth= FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()
        name=findViewById(R.id.serached_name)
        deiscription=findViewById(R.id.des)
        email=findViewById(R.id.searched_email)
        button=findViewById(R.id.register)
        // Action for Register Button
        button.setOnClickListener {
            val hashMap= HashMap<String,String>()
            hashMap["name"]=name.text.toString()
            hashMap["email"]=email.text.toString()
            hashMap["keyword"]=deiscription.text.toString().lowercase()
            firestore.collection("Users").add(hashMap).addOnCompleteListener {
                if (it.isSuccessful && email.text.toString().indexOf("@")>=0 && name.text.toString().length>3 && deiscription.text.toString().length>5 )
                {  Log.i("logs","uploaded")
                    Toast.makeText(this, "User Uploaded!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Search::class.java))}
                else{
                    Toast.makeText(this, "Invalid Data, Try Again!", Toast.LENGTH_SHORT).show()
                }
            }


        }
        button.setOnLongClickListener(View.OnLongClickListener {
            startActivity(Intent(this, Search::class.java))
            Toast.makeText(this, "Skipped", Toast.LENGTH_SHORT).show()
            true


        })

    }




}




