package com.example.escapebulletin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class FacultyActivity : AppCompatActivity() {

    private lateinit var facultyId:EditText
    private lateinit var facultyName:EditText
    private lateinit var facultyEmail:EditText
    private lateinit var facultyMobile:EditText
    private lateinit var classDay:EditText
    private lateinit var classYear:EditText
    private lateinit var classBranch:EditText
    private lateinit var classSection:EditText
    private lateinit var classTimeStart: EditText
    private lateinit var classTimeEnd: EditText
    private lateinit var uploadButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty)

        facultyId = findViewById(R.id.facultyId)
        facultyName = findViewById(R.id.facultyName)
        facultyEmail = findViewById(R.id.facultyEmail)
        facultyMobile = findViewById(R.id.facultyMobile)
        classDay = findViewById(R.id.facultyDay)
        classYear = findViewById(R.id.facultyYear)
        classBranch = findViewById(R.id.facultyBranch)
        classSection = findViewById(R.id.facultySection)
        classTimeStart = findViewById(R.id.facultyTimeStart)
        classTimeEnd = findViewById(R.id.facultyTimeEnd)

        uploadButton = findViewById(R.id.uploadButton)

        val opFaculty = OpFaculty()

        uploadButton.setOnClickListener{
            var facultyKey:String = (classDay.text.toString())+"*"+(classYear.text.toString())+"*"+(classBranch
                .text.toString())+"*"+(classSection.text.toString())+"*"+(classTimeStart.text.toString())+"*"+(
                    classTimeEnd.text.toString())

            val fac = Faculty(facultyKey,facultyName.text.toString(),facultyEmail.text.toString(),facultyMobile.text.toString())
            opFaculty.add(fac).addOnCompleteListener{
                Toast.makeText(this,"Record is Inserted!",Toast.LENGTH_SHORT).show()
            }.addOnCanceledListener{
                Toast.makeText(this,"Failed!",Toast.LENGTH_SHORT).show()
            }

        }
    }
}