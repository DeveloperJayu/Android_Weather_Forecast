package com.example.weatherforecast.Util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.weatherforecast.Activities.HomeActivity
import com.example.weatherforecast.Activities.LoginActivity

fun Context.toast(message : String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

fun Context.login(){
    val intent = Intent(this,HomeActivity::class.java)
    startActivity(intent)
}

fun Context.logout(){
    val intent = Intent(this,LoginActivity::class.java)
    startActivity(intent)
}