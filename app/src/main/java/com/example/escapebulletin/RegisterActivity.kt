package com.example.escapebulletin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    lateinit var logInHere : TextView
    lateinit var signUpUserName: EditText
    lateinit var signUpPassword: EditText
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        logInHere = findViewById(R.id.loginHere)
        signUpPassword = findViewById(R.id.signUpPassword)
        signUpUserName = findViewById(R.id.signUpUserName)
        progressBar = findViewById(R.id.registerProgressBar)
        firebaseAuth = FirebaseAuth.getInstance()
        if(firebaseAuth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


    }

    fun clickedOnCreateAccount(view: android.view.View) {
        var eMail: String = signUpUserName.text.toString().trim()
        var password:String = signUpPassword.text.toString().trim()
        if(eMail.isEmpty()){
            signUpUserName.error = "Email is Required."
            return
        }
        else if(password.isEmpty()){
            signUpPassword.error = "Password is Required."
            return
        }
        else if(password.length < 6){
            signUpPassword.error = "Password must be >= 6 Characters"
            return
        }
        progressBar.visibility = View.VISIBLE
        firebaseAuth.createUserWithEmailAndPassword(eMail,password).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this,"User Created!",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                progressBar.visibility = View.GONE
                finish()
            }
            else{
                Toast.makeText(this,"Error! "+it.exception.toString(),Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        }
    }

    fun clickedOnAlreadyReg(view: android.view.View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}