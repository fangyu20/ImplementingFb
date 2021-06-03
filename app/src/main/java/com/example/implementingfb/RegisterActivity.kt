package com.example.implementingfb

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern
import kotlin.math.log

class RegisterActivity : AppCompatActivity() {

    lateinit var fullName:EditText
    lateinit var surName:EditText
    lateinit var email:EditText
    lateinit var password:EditText
    lateinit var btnRegister: Button
    var valid: Boolean = true
    lateinit var gotoLogin: TextView
    var valorBoton: Boolean = false



    private lateinit var fAuth:FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var mRootReference: DatabaseReference


    //private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        mRootReference = Firebase.database.reference




       // binding= ActivityRegisterBinding.inflate(layoutInflater)
     //   setContentView(binding.root)




        fullName = findViewById(R.id.etNombreCrear)
        surName = findViewById(R.id.etApellidosCrear)
        email = findViewById(R.id.etCorreoCrear)
        password = findViewById(R.id.etContrasena)
        btnRegister = findViewById(R.id.btnCrearCrear)
        gotoLogin = findViewById(R.id.tv_Iniciarsecion)

        btnRegister.setOnClickListener()
        {



            valorBoton=true
            checkField(fullName)
            checkField(email)
            checkField(password)
            checkField(surName)

           // if(isEmailValid){}

//            validate()

            if (valid) {

                if (password.text.toString().length < 8) {
                    password.error = "La contraseña debe contener al menos 8 caracteres"
                }
                else if(email.text.isEmpty())
                {
                    email.error = "Ingresa correo electrónico"
                }
                else {
                    //Registration process

                    var fullName:String = fullName.text.toString()
                    var surName:String = surName.text.toString()
                    var email:String = email.text.toString()
                    var password:String = password.text.toString()
                    var userLevel:String = "1"
//                    var fullName:String = fullName.text.toString()
                    //Metodo de autenticacion y creacion de nuevo usuario en FB
                    crearUsuariosAuth(fullName, surName, email, password, userLevel, valorBoton)

/*fAuth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnSuccessListener()
                    {


                            Toast.makeText(this@RegisterActivity,"Cuenta Creada", Toast.LENGTH_SHORT).show()

                            val i = Intent(this, LoginActivity::class.java)
                            startActivity(i)
                            finish()



                    }.addOnFailureListener(){

                        Toast.makeText(this@RegisterActivity, e.exception!!.message.toString(),
                            Toast.LENGTH_SHORT).show()
                    }*/
                }
            }

        }

        gotoLogin.setOnClickListener()
        {
            val i = Intent(applicationContext, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

    }

    //validate email format-domain
    //private fun validateEmail(): Boolean
    //{
    //    val emailIn = email.text.toString().trim()

        //var array = "@gmail.com"

        //val allowLocal = true

        //val validE = EmailValidator.getInstance(allowLocal).isValid(emailIn)

        //if(valid.getInstance(allowLocal).isValid(emailIn))
       /*return     if(validE)
        {
            Toast.makeText(this,"EMail YES", Toast.LENGTH_SHORT).show()
            etCorreoCrear.error = null

            true
        }else
        {

            Toast.makeText(this,"EMail NOT", Toast.LENGTH_SHORT).show()*/
           // etCorreoCrear.error = null
          //  true


//            false

      //  }





     //   var result=true
        /*return if(emailIn.isEmpty())
        {
            etCorreoCrear.error="Please enter valid email address"
            false
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailIn).matches()) {
            etCorreoCrear.error="Please enter valid email address"
            false
        }
        else
        {*/
            //val wordstofind: List<String> = array.split(' ').filter { x:String -> x!="@gmail.com" }
            //val textWords:List<String> = emailIn.split(' ').filter { x: String -> x!="@gmail.com" }

            //for(word:String in wordstofind)
           // {
                /*var found:Boolean = textWords.contains(word)

                if(!found)
                {
                    result= false
                    break
                }*/

            //}


            //result
        //}
    //}


    private fun validate()
    {
        val result = arrayOf(validatePassword())

        if(false in result)
        {
            return
        }

        Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()
    }


    private fun validatePassword():Boolean
    {
        val passwordIn = password.text.toString().trim()
        val passwordRegex = Pattern.compile(
                "∧"+
                        "(?=.*[0-9])"+
                        "(?=.*[a-z])"+
                        "(?=.*[@#$%∧&+=])"+
                        "(?=\\s+$)"+
                        ".{8,}"+
                        "$"
        )
        return if(passwordIn.isEmpty())
        {
            etCorreoCrear.error="Ingresa una contraseña"
            false
        }
        else if (passwordRegex.matcher(passwordIn).matches()) {
            etCorreoCrear.error="Pass is too weak"
            false
        }
        else
        {
            password.error = null
            true

        }
    }



    //Creamos y autenticamos usuarios segun los datos ingresados
    private fun crearUsuariosAuth(fullName:String, surName: String, email:String, password:String, userLevel: String, valorBoton:Boolean) {


        var user: FirebaseUser? = fAuth.currentUser

        fAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener()
        {
            task->
            if (task.isSuccessful)
            {
                Toast.makeText(this@RegisterActivity, "We have sent a email verefication to email address:${email}", Toast.LENGTH_SHORT).show()
            }

            if (user?.isEmailVerified!! && valorBoton)
            {
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener()
                { task ->
                    if (task.isSuccessful)//si se ha completado el proceso
                    {
                        fStore.collection("Users").document(user!!.uid).set(
                            //var userInfo: Map<String, Object> = HashMap()
                            hashMapOf(
                                "Nombre Completo" to fullName,
                                "Apellidos" to surName,
                                "Correo" to email,
                                "isUser" to userLevel
                            )
                        )
                        crearUsuariosDB(user, fullName, surName, email, userLevel)
                        val i = Intent(this@RegisterActivity, LoginActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
                        finish()
                    }
                    else
                    {
                        Toast.makeText(this@RegisterActivity, "BRRRRRRRRRRRRRRRR", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }




                //Toast.makeText(this@RegisterActivity, "We have sent a email verefication to email address:${email}", Toast.LENGTH_SHORT).show()


/*                if(user?.isEmailVerified!!)
                {

                    if (user.isEmailVerified)
                    {
                        fStore.collection("Users").document(user!!.uid).set(
                            //var userInfo: Map<String, Object> = HashMap()
                            hashMapOf(
                                "Nombre Completo" to fullName,
                                "Apellidos" to surName,
                                "Correo" to email,
                                "isUser" to userLevel
                            )
                        )
                        crearUsuariosDB(user, fullName, surName, email, userLevel)
                        val i = Intent(this@RegisterActivity, LoginActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
                        finish()
                    }
//                    Toast.makeText(this@RegisterActivity, "Cuenta Creada", Toast.LENGTH_SHORT).show()


                }
                else
                {
                    Toast.makeText(this@RegisterActivity, "Cuenta", Toast.LENGTH_SHORT).show()

                }*/



                /* Toast.makeText(this@RegisterActivity, "Cuenta Creada", Toast.LENGTH_SHORT).show()

                 fStore.collection("Users").document(user!!.uid).set(
                         //var userInfo: Map<String, Object> = HashMap()
                         hashMapOf(
                                 "Nombre Completo" to fullName,
                                 "Apellidos" to surName,
                                 "Correo" to email,
                                 "isUser" to userLevel
                         )
                 )*/

                //Llamamos el metodo para crear y guardar usuarios en realtime database
                //   crearUsuariosDB(user, fullName, surName, email, userLevel)

                //Pasamos a la siguiente activity
/*                val i = Intent(this@RegisterActivity, LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()*/

        }
















        /*val user: FirebaseUser? = fAuth.currentUser



        user?.sendEmailVerification()

        if(user?.isEmailVerified!!)
        {

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener()
            {task->
                if (task.isSuccessful)
                {
                    fStore.collection("Users").document(user!!.uid).set(
                        //var userInfo: Map<String, Object> = HashMap()
                        hashMapOf(
                            "Nombre Completo" to fullName,
                            "Apellidos" to surName,
                            "Correo" to email,
                            "isUser" to userLevel
                        )
                    )
                    crearUsuariosDB(user, fullName, surName, email, userLevel)
                    val i = Intent(this@RegisterActivity, LoginActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                    finish()

                }else{

                    Toast.makeText(this@RegisterActivity,
                        task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT).show()
                }

            }
        }*/

    //}





    //Cargamos a base de Realtime Database Usuarios
    private fun crearUsuariosDB(user: FirebaseUser?, fullName: String, surName: String, email: String, userLevel: String)
    {
        mRootReference.child("Users").child(user!!.uid).setValue(
                hashMapOf(
                        "Nombre Completo" to fullName,
                        "Apellidos" to surName,
                        "Correo" to email,
                        "isUser" to userLevel)
        )
    }

    fun checkField(textField: EditText): Boolean
     {
        if (textField.text.toString().isEmpty()) {
            textField.error = "Debes completar este campo."
            valid = false
        } else {
            valid = true
        }
        return valid
     }
}
