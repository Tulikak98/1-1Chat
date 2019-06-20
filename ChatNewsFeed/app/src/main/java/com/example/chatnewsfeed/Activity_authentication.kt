package com.example.chatnewsfeed

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class Activity_authentication : AppCompatActivity() {
    companion object {
        val TAG = "Activity_authentication"
    }
    lateinit var name: String
    lateinit var email: String
    lateinit var clgid: String
    lateinit var stream: String
    lateinit var profileImageUrl: String
    //  lateinit var div: String
    //   lateinit var year: String


    lateinit var editPass: EditText
    lateinit var buttonDone: Button
    lateinit var no: String
    private var mAuth: FirebaseAuth? = null
    private var mVerificationId: String? = null

    private val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

            //Getting the code sent by SMS
            val code = phoneAuthCredential.smsCode
            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editPass.setText(code)
                //verifying the code
                verifyVerificationCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@Activity_authentication, e.message, Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(s: String?, forceResendingToken: PhoneAuthProvider.ForceResendingToken?) {
            super.onCodeSent(s, forceResendingToken)

            //storing the verification id that is sent to the user
            mVerificationId = s
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        val intent = intent
        editPass = findViewById<View>(R.id.editPass) as EditText

        mAuth = FirebaseAuth.getInstance()

        no = intent.getStringExtra("Phone No.")
        Log.d(TAG, "phone no added:$no")
        profileImageUrl = intent.getStringExtra("ProfileImageUrl")
        Log.d(TAG, "pictureIMG added:$profileImageUrl")
        name = intent.getStringExtra("Name")
        Log.d(TAG, "name added:$name")
        clgid = intent.getStringExtra("Clgid")
        Log.d(TAG, "ClgiD added:$clgid")
        stream = intent.getStringExtra("Stream")
        Log.d(TAG, "Stream added:$stream")
        //   div = intent.getStringExtra("Division")
        //    Log.d(TAG, "Stream added:$div")
        //  year = intent.getStringExtra("Year")
        //   Log.d(TAG, "Stream added:$year")
        email = intent.getStringExtra("Email")
        Log.d(TAG, "Email Id added:$email")

        sendVerificationCode(no)

        buttonDone = findViewById<View>(R.id.buttonDone) as Button

        buttonDone.setOnClickListener(View.OnClickListener {
            val code = editPass.text.toString().trim { it <= ' ' }
            if (code.isEmpty() || code.length < 6) {
                editPass.error = "Enter valid code"
                editPass.requestFocus()
                return@OnClickListener
            }

            //verifying the code entered manually
            verifyVerificationCode(code)

        })
    }

    private fun sendVerificationCode(no: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91$no",
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallbacks
        )
    }

    private fun verifyVerificationCode(code: String?) {
        //creating the credential
        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code!!)

        //signing the user
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this@Activity_authentication) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Auth Done")
                    saveUserToFirebaseDatabase()
                    //verification successful we will start the profile activity
                    /* val intent = Intent(this@Activity_authentication, chattMain::class.java)
                     intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                     startActivity(intent)*/

                } else {

                    //verification unsuccessful.. display an error message

                    var message = "Something is wrong, we will fix it soon..."

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        message = "Invalid code entered..."
                    }


                }
            }
    }
    private fun saveUserToFirebaseDatabase() {

        //  val uid = FirebaseAuth.getInstance().uid ?: ""
        //val user_id = uid
        val usid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$usid")
        // val user = ref.push().key
        var user = FInfo(usid, name, no, profileImageUrl, email, clgid, stream)
        //ref.child(user_id).setValue(infos)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "Finally we saved the user to Firebase Database")

                val intent = Intent(this@Activity_authentication, HomePage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database: ${it.message}")
            }
    }
    /* class User(val uid: String, val name: String, val profileImageUrl: String) {

     }*/
}

