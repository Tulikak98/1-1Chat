package com.example.chatnewsfeed

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.book_list.view.*

class BooksActivity : AppCompatActivity() {

    companion object {
        val TAG = "BookActivity"
    }

    lateinit var mSearchText : EditText
    lateinit var mRecyclerView : RecyclerView

    lateinit var mDatabase : DatabaseReference

    lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<Book, UsersViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)

        mSearchText =findViewById(R.id.searchText)
        mRecyclerView = findViewById(R.id.list_view)


        mDatabase = FirebaseDatabase.getInstance().getReference("books")


        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))


        mSearchText.addTextChangedListener(object  : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                val searchText = mSearchText.getText().toString().trim()

                loadFirebaseData(searchText)
            }
        } )

    }

    private fun loadFirebaseData(searchText : String) {

        if(searchText.isEmpty()){

            FirebaseRecyclerAdapter.cleanup()
            mRecyclerView.adapter = FirebaseRecyclerAdapter

        }else {


            val firebaseSearchQuery = mDatabase.orderByChild("TITLE").startAt(searchText).endAt(searchText + "\uf8ff")

            FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Book, UsersViewHolder>(

                Book::class.java,
                R.layout.book_list,
                UsersViewHolder::class.java,
                firebaseSearchQuery


            ) {
                override fun populateViewHolder(viewHolder: UsersViewHolder, model: Book?, position: Int) {


                    viewHolder.mview.userName.setText(model?.TITLE)
                    //   var title_book = viewHolder.mview.userName.setText(model?.TITLE)
                    Log.d(TAG, " book fetch")//put TITLE after $
                    viewHolder.mview.available.setText(model?.AVAILABLITY)
                    //   viewHolder.mview.available.setText(model?.AVAILABLITY!!)
                    //     var title_book_avl = viewHolder.mview.available.setText(model?.AVAILABLITY)
                    Log.d(TAG, " book fetch2")
                    // Picasso.with(applicationContext).load(model?.image).into(viewHolder.mview.UserImageView)

                }

            }

            mRecyclerView.adapter = FirebaseRecyclerAdapter

        }
    }


    // // View Holder Class

    class UsersViewHolder(var mview : View) : RecyclerView.ViewHolder(mview) {

    }


}
