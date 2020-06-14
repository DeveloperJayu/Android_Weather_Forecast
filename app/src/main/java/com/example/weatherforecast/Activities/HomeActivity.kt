package com.example.weatherforecast.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.example.weatherforecast.Fragments.ProfileFragment
import com.example.weatherforecast.Fragments.WeatherFragment
import com.example.weatherforecast.R
import com.example.weatherforecast.Util.logout
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {


    lateinit var toolbar : Toolbar
    lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        
        toolbar = findViewById(R.id.toolbar)

        mAuth = FirebaseAuth.getInstance()

        setUpToolbar()

        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout,WeatherFragment())
            .addToBackStack("weatherForecast")
            .commit()

        supportActionBar?.title = "Weather Forecast"

    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item?.itemId == R.id.weatherForecast){
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout,WeatherFragment())
                .addToBackStack("weatherForecast")
                .commit()

            supportActionBar?.title = "Weather Forecast"
        }

        if (item?.itemId == R.id.profile){
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, ProfileFragment())
                .addToBackStack("profileFragment")
                .commit()

            supportActionBar?.title = "Profile"
        }

        else if(item?.itemId == R.id.logout){
            AlertDialog.Builder(this).apply{
                setTitle("Are you Sure?")
                setPositiveButton("Yes"){ _, _ ->

                    FirebaseAuth.getInstance().signOut()
                    logout()

                }
                setNegativeButton("No"){ _,_ ->

                }
            }.create().show()
        }
        return super.onOptionsItemSelected(item)
    }
}