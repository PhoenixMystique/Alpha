package com.devwithadi.alpha

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class Search : AppCompatActivity() {
    lateinit  var recyclerView: RecyclerView
    lateinit var searchlist:List<searchModel>
    lateinit var CustomAdapter:CustomAdapter
    var firestore= FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serach)
        var actionss:FloatingActionButton = findViewById(R.id.actions)
        actionss.setOnClickListener {

            startActivity(Intent(this,MainActivity::class.java))
        }
        // Change Status Bar Color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black))

        searchlist = ArrayList()
        CustomAdapter= CustomAdapter(searchlist)
        recyclerView =findViewById(R.id.view)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter=CustomAdapter

        val serachfield:EditText = findViewById(R.id.bar)
        serachfield.doOnTextChanged { text, start, before, count ->
               // Method To Search
               serachFirebase( serachfield.text.toString().lowercase())

        }
    }

    private fun serachFirebase(lowercase: String) {
          firestore.collection("Users").orderBy("keyword").startAt(lowercase).endAt("$lowercase\uf8ff").get().addOnCompleteListener {
             if (
                 it.isSuccessful
             )
             {
                 if (lowercase.length==0){  searchlist = ArrayList()
                     CustomAdapter.mList =searchlist
                         CustomAdapter.notifyDataSetChanged() }
                 else{
                 Log.i("alphalog","Sucess")
                 searchlist =it.result!!.toObjects(searchModel::class.java)
                 CustomAdapter.mList =searchlist
                 CustomAdapter.notifyDataSetChanged()}

             }
 else{
     Log.i("alphalog","Data Download Failed")
 }
          }
    }
}