package com.example.chatnewsfeed

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_student.*
import java.lang.Exception
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class Activity_student : AppCompatActivity(){
    companion object {
        val TAG = "StudentRegisterActivity"
    }

    lateinit var name: String
    lateinit var email: String
    lateinit var editSphone: EditText
    lateinit var clgid: String
    lateinit var no: String
    lateinit var profileImageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)
        buttonSnext.setOnClickListener {
            DetailsUpload()
            // val intent = Intent(this, Activity_authentication::class.java)
            // startActivity(intent)
        }
        val spinner: Spinner = findViewById(R.id.spinner1)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(this, R.array.Streams, android.R.layout.simple_spinner_item).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Specify the layout to use when the list of choices appears
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        val division: Spinner = findViewById(R.id.spinner2)
        ArrayAdapter.createFromResource(this,
            R.array.Div, android.R.layout.simple_spinner_item).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            division.adapter = adapter
        }
        val mentorleader: Spinner = findViewById(R.id.spinner3)
        ArrayAdapter.createFromResource(this, R.array.Mentor_leader, android.R.layout.simple_spinner_item).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mentorleader.adapter = adapter
        }
        selectphoto_button_register.setOnClickListener {
            Log.d(Activity_student.TAG, "Try to show photo")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
            uploadImageToStorage()
        }
    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            select_imageview.setImageBitmap(bitmap)
            selectphoto_button_register.alpha = 0f
            selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun DetailsUpload(){
        try {
            name = editSname.text.toString()
            email = editSemail.text.toString()
            editSphone = findViewById<View>(R.id.editSphone) as EditText
            no = editSphone.text.toString()
            clgid = editScollegeid.text.toString()
            var mySpinner1 = findViewById(R.id.spinner1) as Spinner
            var stream = mySpinner1.getSelectedItem().toString()
            var mySpinner2 = findViewById(R.id.spinner2) as Spinner
            var div = mySpinner2.getSelectedItem().toString()
            var mySpinner3 = findViewById(R.id.spinner3) as Spinner
            var year = mySpinner3.getSelectedItem().toString()

            if (name.isEmpty()) {
                editSname.error = "Please enter your name"
                return
            }else if (!EmailValidFunc(email)) {
                editSemail.error = "Email Address is not valid..."
                return
            }else if (email.isEmpty()) {
                EmailValidFunc(email)
                editSemail.error = "Please enter your Email"
                return
            }else if (no.isEmpty() || no.length < 10){
                editSphone.error = "Enter a valid mobile"
                editSphone.requestFocus()
                return
            }else if (clgid.isEmpty()) {
                editScollegeid.error = "Please enter your College Id"
                return
            }else{
                uploadImageToStorage()
                Log.d(Activity_student.TAG, "Image url successfully passed from private fun:$profileImageUrl")
                val intent = Intent(this@Activity_student, Activity_authentication::class.java)
                intent.putExtra("Phone No.", no)
                intent.putExtra("Name", name)
                intent.putExtra("Email", email)
                intent.putExtra("Clgid", clgid)
                intent.putExtra("ProfileImageUrl", profileImageUrl)
                intent.putExtra("Stream", stream)
                intent.putExtra("Div", div)
                intent.putExtra("Year", year)
                startActivity(intent)
            }

/*            val ref = FirebaseDatabase.getInstance().getReference("Student Details")
            val user_id = ref.push().key
            var infos = SInfo(user_id!!, name, email, no, clgid, stream, div, year)
            ref.child(user_id).setValue(infos).addOnCompleteListener {
                if (it.isSuccessful) {
                    println("value uploaded successfully!")
                    Toast.makeText(this, "Student Details Uploaded", Toast.LENGTH_LONG).show()
                } else {
                    println("Something went wrong when uploading value")
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }*/

        }catch (e: Exception){
            println(e)
        }
    }
    private fun EmailValidFunc(email: String): Boolean {
        val EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9])*(\\.[A-Za-z]{2,})$"
        val pattern: Pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun uploadImageToStorage() {

        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(Activity_faculty.TAG, "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(Activity_student.TAG, "File Location: $it")
                    profileImageUrl = it.toString()
                    Log.d(Activity_faculty.TAG, "Successfully uploaded image:$profileImageUrl")
                    // saveUserToFirebaseDatabase(it.toString())
                }
            }

            .addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.message, Toast.LENGTH_LONG).show()

            }
    }
}