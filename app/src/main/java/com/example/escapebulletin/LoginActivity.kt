package com.example.escapebulletin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    lateinit var logInUserName:EditText
    lateinit var logInPassword:EditText
    lateinit var createAccountText:TextView
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        logInUserName = findViewById(R.id.logInUserName)
        logInPassword = findViewById(R.id.logInPassword)
        createAccountText = findViewById(R.id.createAccountText)
        progressBar = findViewById(R.id.logInprogressBar)
        firebaseAuth = FirebaseAuth.getInstance()

        if(firebaseAuth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    fun clickedOnLoginButton(view: android.view.View) {
        var eMail: String = logInUserName.text.toString().trim()
        var password:String = logInPassword.text.toString().trim()
        when {
            eMail.isEmpty() -> {
                logInUserName.error = "Email is Required."
            }
            password.isEmpty() -> {
                logInPassword.error = "Password is Required."
            }
            password.length < 6 -> {
                logInPassword.error = "Password must be >= 6 Characters"
            }
        }
        progressBar.visibility = View.VISIBLE
        firebaseAuth.createUserWithEmailAndPassword(eMail,password).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this,"Login Successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                progressBar.visibility = View.GONE
                finish()
            }
            else{
                Toast.makeText(this,"Error! "+it.exception.toString(), Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.INVISIBLE
            }
        }
    }
    fun clickedOnCreateAccount(view: android.view.View) {
        startActivity(Intent(this,RegisterActivity::class.java))
        finish()
    }
}