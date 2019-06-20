package com.example.chatnewsfeed

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    companion object {
        val USER_KEY = "User key"
        val TAG = "NewMessageActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)// if not working insert dis lines in layout  ->  android:layout_width="0dp" android:layout_height="0dp" app:layoutManager="android.support.v7.widget.LinearLayoutManager"
        supportActionBar?.title = "Select User"
        recyclerview_newmessage.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        fetchUsers()
    }
    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(FInfo::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as UserItem

                    val intent = Intent(view.context, ChatLogActivity::class.java)
//          intent.putExtra(USER_KEY,  userItem.user.username)
                    intent.putExtra(USER_KEY, userItem.user)

                    startActivity(intent)

                    finish()

                }

                recyclerview_newmessage.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
class UserItem(val user: FInfo): Item<ViewHolder>(){ ////Replace User with your data class name

    override fun bind(viewHolder: ViewHolder, position: Int) {
        //will be call in our list of easc user object later on...
        viewHolder.itemView.username_textview_new_message.text = user.name
        Log.d("NewMessages","viewholder binding...")
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_new_message) // Replace profile_pic with profileImageUrl or with image variable of data class
    }

    override fun getLayout(): Int {
        Log.d("NewMessages","user row new msg layout")
        return R.layout.user_row_new_message
    }
}









/*import android.content.Intent
import android.nfc.Tag
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = "Select User"
        recyclerview_newmessage.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        sampleData()
        Log.d("NewMessage", "sample data is called")
       // fetchUsers()
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }

    private fun sampleData(){
        val adapter = GroupAdapter<ViewHolder>()

        adapter.add(Sample("hello"))
        adapter.add(Sample("hello"))
        adapter.add(Sample("hello"))
        adapter.add(Sample("hello"))
        Log.d("NewMessage", "adapter is added")
        recyclerview_newmessage.adapter = adapter
        Log.d("NewMessage", "recyclerview is called")

    }
    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(FInfo::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                        Log.d("NewMessage","addition!!!${UserItem(user)}")

                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as UserItem

                    val intent = Intent(view.context, ChatLogActivity::class.java)
//          intent.putExtra(USER_KEY,  userItem.user.username)
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)

                    finish()

                }

                recyclerview_newmessage.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
class Sample(val A: String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_textview_new_message.text = A
        Log.d("NewMessage", "String is added to username")
    }

    override fun getLayout(): Int {
        Log.d("NewMessage", "user added with name with layout")
        return R.layout.user_row_new_message
    }
}
class UserItem(val user: FInfo): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_textview_new_message.text = user.name

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_new_message)
    }

    override fun getLayout(): Int {

        return R.layout.user_row_new_message
    }
}*/
/*
class UserItem(val user: FInfo): Item<ViewHolder>(){ ////Replace User with your data class name

    override fun bind(viewHolder: ViewHolder, position: Int) {
        //will be call in our list of easc user object later on...
        viewHolder.itemView.username_textview_new_message.text = user.name
        Log.d("NewMessage", "user added with name$user")

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_new_message) // Replace profile_pic with profileImageUrl or with image variable of data class
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}
*/
