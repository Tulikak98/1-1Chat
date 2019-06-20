package com.example.chatnewsfeed

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_personalinfo.*

class PersonalinfoActivity : AppCompatActivity() {
    companion object {
        val TAG = "PersonalInfo Activity"
    }

    /* lateinit var username: EditText
     lateinit var update: Button
     lateinit var mDatabase: DatabaseReference
     // lateinit var fr: FirebaseAuth
     lateinit var profile_pic_upload: ImageButton
     lateinit var ProgressBar: ProgressDialog
     lateinit var mImageStorage: StorageReference*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalinfo)
        supportActionBar?.title = "Personal Info"

        // username = findViewById(R.id.username) as EditText
        // update = findViewById(R.id.update) as Button
        // profile_pic_upload = findViewById(R.id.profile_pic_upload) as ImageButton
        // ProgressBar = ProgressDialog(this)

        // mAuth = FirebaseAuth.getInstance()
        //val user_id = LatestMessagesActivity.currentUser?.uid
        val fromId = FirebaseAuth.getInstance().uid

        //  mDatabase = FirebaseDatabase.getInstance().getReference("Faculty Details/").child(fromId!!)//path of db
        //  mImageStorage = FirebaseStorage.getInstance().getReference()

        update.setOnClickListener() {
            val name = username.text.toString().trim()
            Toast.makeText(this, "Updated Successfully", Toast.LENGTH_LONG).show()
            //  updateUser(name)
        }

        backbutton.setOnClickListener() {
            val intent = Intent(this@PersonalinfoActivity, HomePage::class.java)
            startActivity(intent)
        }

        profile_pic.setOnClickListener() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            profile_pic_upload.setImageBitmap(bitmap)
            profile_pic.alpha = 0f
            profile_pic.setBackgroundDrawable(bitmapDrawable)
        }
    }

    /*  private fun updateUser(name: String) {
          ProgressBar.setMessage("Updating ...")
          ProgressBar.show()

          val userMap = HashMap<String, Any>()
          userMap["name"] = name

          mDatabase.updateChildren(userMap).addOnCompleteListener { task ->
              if (task.isSuccessful) {
                  val intent = Intent(applicationContext, chattMain::class.java)
                  startActivity(intent)
                  finish()
                  ProgressBar.dismiss()
              }
          }
      }
  */

}