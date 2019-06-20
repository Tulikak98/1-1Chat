package com.example.chatnewsfeed

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_feed_back.*

class FeedBackActivity : AppCompatActivity() {

    companion object {
        val TAG = "StudentRegisterActivity"
    }

    lateinit var feedBack: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)

        submit_Feedback.setOnClickListener {
            feedbackUpload()
            // val intent = Intent(this, Activity_authentication::class.java)
            // startActivity(intent)
        }
    }
    private fun feedbackUpload(){
        feedBack = editText_feedBack.text.toString()

        if (feedBack.isEmpty()){
            submit_Feedback.error = "empty!!!"
            return
        }else{

            val usid = FirebaseAuth.getInstance().uid ?: ""
            val ref = FirebaseDatabase.getInstance().getReference("/feedBack/$usid")
            // val user = ref.push().key
            var user = FeedBack(usid,feedBack)
            //ref.child(user_id).setValue(infos)
            ref.setValue(user)
                .addOnSuccessListener {
                    Log.d(Activity_authentication.TAG, "Finally we saved the feedback in Firebase Database")
                    Toast.makeText(this,"Thank You For Your Valuable FeedBack", Toast.LENGTH_LONG).show()

                    val intent = Intent(this, HomePage::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)

                }
                .addOnFailureListener {
                    Log.d(Activity_authentication.TAG, "Failed to send value to database: ${it.message}")
                }
        }

    }
}
class FeedBack(var usid: String, var feedBack: String){
    constructor():this("","")

}