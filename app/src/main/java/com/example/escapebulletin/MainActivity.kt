package com.example.escapebulletin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun logout(view: android.view.View) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun clickedOnStudent(view: android.view.View) {
        startActivity(Intent(this, StudentActivity::class.java))
    }
    fun clickedOnFaculty(view: android.view.View) {
        startActivity(Intent(this, FacultyActivity::class.java))
    }
}