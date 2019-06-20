package com.example.chatnewsfeed

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.app_bar_home_page.*

class HomePage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val intent = Intent(this, NewMessageActivity::class.java)
            startActivity(intent)
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        verifyUserIsLoggedIn()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        menuInflater.inflate(R.menu.home_page, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.chat){
            intent = Intent(this@HomePage, LatestMessageActivity::class.java)
            startActivity(intent)
            return true
        }
        if (id == R.id.New_Group) {
            return true
        }
        if (id == R.id.starred) {
            return true
        }
        if (id == R.id.personal_info) {
            intent = Intent(this@HomePage, PersonalinfoActivity::class.java)
            startActivity(intent)
            return true
        }
        if (id == R.id.notification) {
          //  intent = Intent(this@chattMain, PlusOneFragment::class.java)
            // startActivity(intent)
            return true
        }
        if (id == R.id.sign_out) {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@HomePage, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.chat -> {
                val intent = Intent(this, LatestMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.books -> {
                val intent = Intent(this, BooksActivity::class.java)
                startActivity(intent)
            }

            R.id.personal_info -> {
                val intent = Intent(this, PersonalinfoActivity::class.java)
                startActivity(intent)
            }
            R.id.sign_out -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.feedback -> {
                val intent = Intent(this, FeedBackActivity::class.java)
                startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
