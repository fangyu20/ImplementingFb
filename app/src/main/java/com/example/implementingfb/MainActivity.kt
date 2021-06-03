package com.example.implementingfb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var textview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        var usuario_nombreU: String = preferences.getString("usuario_nombreU", null).toString()
        val usuario_idU: String = preferences.getString("usuario_idU", null).toString()

        if (usuario_idU != null && usuario_nombreU != null)
        {
            textview.text=usuario_nombreU
        }

        val btnLogoutUser = findViewById<Button>(R.id.btnCerrarUser)

        btnLogoutUser.setOnClickListener()
        {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

        btnLogoutUser.setOnClickListener()
        {

            FirebaseAuth.getInstance().signOut()
            cerrarSesion()
            /*
            FirebaseAuth.getInstance().signOut()
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()*/
        }
    }

    private fun init(){
        textview = findViewById(R.id.tvUser)
        preferences=getSharedPreferences("Preferences", Context.MODE_PRIVATE)
    }

    private fun cerrarSesion()
    {
        preferences.edit().clear().apply()
        irLogin()
    }

    private fun irLogin()
    {
        var i = Intent(this, LoginActivity::class.java)

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
    }
}
