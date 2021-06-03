package com.example.implementingfb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    //lateinit var fullName: EditText
    //lateinit var surName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btnLogin: Button
    var valid: Boolean = true
    lateinit var gotoRegister: TextView
    private lateinit var preferences:SharedPreferences

    lateinit var fAuth: FirebaseAuth
    lateinit var fStore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
        validarSesion()

        btnLogin.setOnClickListener()
        {
            checkField(email)
            checkField(password)

            if (valid)
            {

                if (password.text.toString().length < 8) {
                    password.error = "Debes completar este campo."
                }
                else if(email.text.isEmpty())
                {
                    email.error = "Ingresa correo electrónico."
                }
                else
                {

                    //Iniciamos sesion con Correo y Contraseña
                    fAuth.signInWithEmailAndPassword(
                        email.text.toString(),
                        password.text.toString()

                    ).addOnSuccessListener() {
                            authResult ->

                            checkUserAccessLevel(authResult.user!!.uid)

                        }.addOnFailureListener( OnFailureListener() {//En caso de error lanzar excepcion
                                Exception->

                                    Toast.makeText(this,
                                            Exception.message.toString(),
                                            Toast.LENGTH_SHORT).show();
                            })
                }
            }
        }

        gotoRegister.setOnClickListener()
        {
            val i = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(i)
        }
    }

     //Inicializamos los objetos contenidos y que utilizaremos en este activity
     fun init()
     {

        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        email = findViewById(R.id.etusuarioIniciar)
        password = findViewById(R.id.etContraIniciar)
        btnLogin = findViewById(R.id.btnIniciarSecion)
        gotoRegister = findViewById(R.id.tv_Crear)

         preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)

    }


    private fun checkUserAccessLevel(uid: String)
    {

        fStore.collection("Users").document(uid).get().addOnSuccessListener(){
            documentSnapshot ->
            Log.d("TAG","OnSuccess ${documentSnapshot.data}")
            //var user: FirebaseUser? = fAuth.currentUser

            //Identify user level
            //Lo enviamos a Activity correspondiente segun nivel de acceso (Admin)
            if (documentSnapshot.getString("isAdmin")!=null)
            {
                var editor: SharedPreferences.Editor = preferences.edit()
                editor.putString("usuario_id", uid)
                editor.putString("usuario_nombre", documentSnapshot.getString("Nombre Completo"))
                editor.commit()

                irAdmin()

            }

            //Lo enviamos a Activity correspondiente segun nivel de acceso (Usuarios)
            if (documentSnapshot.getString("isUser")!=null)
            {
                var editor: SharedPreferences.Editor = preferences.edit()
                editor.putString("usuario_idU", uid)
                editor.putString("usuario_nombreU", documentSnapshot.getString("Nombre Completo"))
                editor.commit()

                irUser()

            }
        }

    }

    //Valida si el User ha inicado sesion para mantenerlo en activity correspondiente
    private fun validarSesion()
    {
        ///Secion iniciada por Admin
        var usuario_nombre: String? = preferences.getString("usuario_nombre", null)
        var usuario_id: String? = preferences.getString("usuario_id", null)

        if (usuario_id != null && usuario_nombre != null)
        {
            irAdmin()
        }

        ///Secion iniciada por usuarios
        var usuario_nombreU: String? = preferences.getString("usuario_nombreU", null)
        var usuario_idU: String? = preferences.getString("usuario_idU", null)

        if (usuario_idU != null && usuario_nombreU != null)
        {
            irUser()
        }

    }

    //Nos lleva a la activity de Admin bajo condicion de AccessLevel
    private  fun irAdmin()
    {
        var i = Intent(this, AdminActivity::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
    }

    //Nos lleva a la activity de Usuario bajo condicion de AccessLevel
    private  fun irUser()
    {
        var i = Intent(this, MainActivity::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
    }

    //Verifica si los campos estan vacios
    fun checkField(textField: EditText): Boolean {
        if (textField.text.toString().isEmpty()) {
            textField.error = "Error"
            valid = false
        } else {
            valid = true
        }
        return valid
    }

    /*override
fun onStart(x: Intent) {
    super.onStart()

    if (FirebaseAuth.getInstance().currentUser != null)
    {
         var y = Intent(applicationContext, x::class.java)
        startActivity(y)
        finish()
    }

}*/

}