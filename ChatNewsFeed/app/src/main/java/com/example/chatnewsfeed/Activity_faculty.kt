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
import kotlinx.android.synthetic.main.activity_faculty.*
import java.lang.Exception
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class Activity_faculty : AppCompatActivity() {

    companion object {
        val TAG = "RegisterActivity"
    }

    lateinit var name: String
    lateinit var email: String
    lateinit var editFphone: EditText
    lateinit var clgid: String
    lateinit var no: String
    lateinit var div: String
    lateinit var profileImageUrl: String
    //   lateinit var profileImageUrl: String
    //  lateinit var selectphoto_button_register: Button
    // lateinit var photo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty)
        buttonFnext.setOnClickListener {
            DetailsUpload()

        }
        val stream: Spinner = findViewById(R.id.spinner1)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(this, R.array.Streams, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Specify the layout to use when the list of choices appears
            // Apply the adapter to the spinner
            stream.adapter = adapter
        }
        selectphoto_button_register.setOnClickListener {
            Log.d(TAG, "Try to show photo")
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

    private fun DetailsUpload() {
        try {
            name = editFname.text.toString()
            email = editFemail.text.toString()
            editFphone = findViewById<View>(R.id.editFphone) as EditText
            no = editFphone.text.toString()
            clgid = editFcollegeid.text.toString()
            val mySpinner = findViewById(R.id.spinner1) as Spinner
            val stream = mySpinner.getSelectedItem().toString()

            if (name.isEmpty()) {
                editFname.error = "Please enter your name"
                return
            } else if (!EmailValidFunc(email)) {
                editFemail.error = "Email Address is not valid..."
                return
            } else if (email.isEmpty()) {
                EmailValidFunc(email)
                editFemail.error = "Please enter your Email"
                return
            } else if (no.isEmpty() || no.length < 10) {
                editFphone.error = "Enter a valid mobile"
                editFphone.requestFocus()
                return
            } else if (clgid.isEmpty()) {
                editFcollegeid.error = "Please enter your College Id"
                return
            } else {
                uploadImageToStorage()
                Log.d(TAG, "Image url successfully passed from private fun:$profileImageUrl")
                val intent = Intent(this, Activity_authentication::class.java)
                intent.putExtra("Phone No.", no)
                intent.putExtra("Name", name)
                intent.putExtra("Email", email)
                intent.putExtra("Clgid", clgid)
                intent.putExtra("ProfileImageUrl", profileImageUrl)
                intent.putExtra("Stream", stream)
                startActivity(intent)
            }

            /*imp     val ref = FirebaseDatabase.getInstance().getReference("Faculty Details")
                 val user_id = ref.push().key
                 var infos = FInfo(user_id!!, name, email, no, clgid, stream)
                 ref.child(user_id).setValue(infos).addOnCompleteListener {
                     if (!it.isSuccessful) return@addOnCompleteListener
                     Log.d(TAG, "successfully created user with uid: ${it.result}")
                     println("value uploaded successfully!")
                     Toast.makeText(this, "Faculty Details Uploaded", Toast.LENGTH_LONG).show()
                     uploadImageToStorage()
                 }
                     .addOnFailureListener {
                         println("Something went wrong when uploading value")
                         Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                     }*/
        } catch (e: Exception) {
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
                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File Location: $it")
                    profileImageUrl = it.toString()
                    Log.d(TAG, "Successfully uploaded image:$profileImageUrl")
                    // saveUserToFirebaseDatabase(it.toString())
                }
            }

            .addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.message, Toast.LENGTH_LONG).show()

            }
    }

}
