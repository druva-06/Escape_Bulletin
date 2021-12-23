package com.example.escapebulletin

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import com.google.firebase.storage.FirebaseStorage

class StudentActivity : AppCompatActivity() {

    private lateinit var filepath:Uri
    private lateinit var rollNo:EditText
    private lateinit var firstName:EditText
    private lateinit var lastName:EditText
    private lateinit var phoneNumber:EditText
    private lateinit var year:EditText
    private lateinit var branch:EditText
    private lateinit var section:EditText
    private lateinit var profile:ImageView
    private lateinit var uploadButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        rollNo = findViewById(R.id.rollNo)
        firstName = findViewById(R.id.firstName)
        lastName = findViewById(R.id.lastName)
        phoneNumber = findViewById((R.id.phoneNumber))
        year = findViewById(R.id.year)
        branch = findViewById(R.id.branch)
        section = findViewById(R.id.section)
        uploadButton = findViewById(R.id.uploadButton)
        profile = findViewById(R.id.profile)

        

        val opStudent = OpStudent()

        profile.setOnClickListener{
            startFileChooser()
        }

        uploadButton.setOnClickListener{
            val std = Student(rollNo.text.toString(),firstName.text.toString(),lastName.text.toString(),phoneNumber.text.toString(),
                branch.text.toString(),year.text.toString(),section.text.toString())

            opStudent.add(std).addOnCompleteListener{
                Toast.makeText(this,"Record is Inserted!",Toast.LENGTH_SHORT).show()
            }.addOnCanceledListener{
                Toast.makeText(this,"Failed!",Toast.LENGTH_SHORT).show()
            }
            uploadFile()
        }
    }
    private fun startFileChooser(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Choose Profile"),111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 111 && resultCode == Activity.RESULT_OK && data!=null){
            filepath = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filepath)
            profile.setImageBitmap(bitmap)
        }
    }

    private fun uploadFile(){
        if(filepath!=null){
            val pd = ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()
            val imageRef = FirebaseStorage.getInstance().reference.child("Student/${rollNo.text}")
            imageRef.putFile(filepath)
                .addOnSuccessListener {
                    pd.dismiss()
                    Toast.makeText(this,"profile Uploaded!",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    pd.dismiss()
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener {
                    val progress:Double = (100.0 * it.bytesTransferred)/(it.totalByteCount)
                    pd.setMessage("Uploaded ${progress.toInt()}%")
                }
        }
    }
}