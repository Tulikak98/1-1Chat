package com.example.chatnewsfeed

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_message.*

class LatestMessageActivity : AppCompatActivity() {

    companion object {
        var currentUser: FInfo? = null  //change FInfo to User also in constructornothing will happenok
        val TAG = "LatestMessages"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_message)

        recyclerview_latest_messages.adapter = adapter
        recyclerview_latest_messages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        // set item click listener on your adapter
        adapter.setOnItemClickListener { item, view ->
            Log.d(TAG, "123")
            val intent = Intent(this, ChatLogActivity::class.java)

            // we are missing the chat partner user

            val row = item as LatestMessageRow
            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }

//    setupDummyRows()
        listenForLatestMessages()

        fetchCurrentUser()

        //  verifyUserIsLoggedIn()
    }

    val latestMessagesMap = HashMap<String, ChatMessage>()

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    val adapter = GroupAdapter<ViewHolder>()

//  private fun setupDummyRows() {
//
//
//    adapter.add(LatestMessageRow())
//    adapter.add(LatestMessageRow())
//    adapter.add(LatestMessageRow())
//  }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(FInfo::class.java)
                Log.d("LatestMessages", "Current user ${currentUser?.profileImageUrl}")
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}