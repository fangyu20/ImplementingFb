package com.example.implementingfb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class AdminActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var textview: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        init()

        var usuario_nombre: String = preferences.getString("usuario_nombre", null).toString()
        val usuario_id: String = preferences.getString("usuario_id", null).toString()

        if (usuario_id != null && usuario_nombre != null)
        {
            textview.text=usuario_nombre
        }

        val btnLogoutAdmin = findViewById<Button>(R.id.btnCerrarAdmin)

        btnLogoutAdmin.setOnClickListener()
        {
            FirebaseAuth.getInstance().signOut()
            cerrarSesion()

        }
    }

    private fun init(){
        textview = findViewById(R.id.tvAdmin)
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