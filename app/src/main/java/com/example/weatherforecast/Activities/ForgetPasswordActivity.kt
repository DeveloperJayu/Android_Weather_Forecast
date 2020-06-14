package com.example.weatherforecast.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.weatherforecast.R
import com.example.weatherforecast.Util.toast
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class ForgetPasswordActivity : AppCompatActivity() {

    lateinit var toolbar : Toolbar
    lateinit var edit_text_email : EditText
    lateinit var button_check : Button
    lateinit var text_view_login : TextView
    lateinit var progresbar : ProgressBar

    lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        toolbar = findViewById(R.id.toolbar)
        edit_text_email = findViewById(R.id.edit_text_email)
        button_check = findViewById(R.id.button_check)
        text_view_login = findViewById(R.id.text_view_login)
        progresbar = findViewById(R.id.progressBar)

        mAuth = FirebaseAuth.getInstance()

        button_check.setOnClickListener {
            val email = edit_text_email.text.toString()
            if (email.isEmpty()){
                edit_text_email.error = "Email Required"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edit_text_email.error = "Valid Email Required"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            forgetPassword(email)
        }

        text_view_login.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun forgetPassword(email: String) {
        progresbar.visibility = View.VISIBLE

        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this){task ->
                progresbar.visibility = View.GONE
                if (task.isSuccessful){
                    toast("Check Your Mail")
                }
                else{
                    task.exception?.message?.let {
                        toast(it)
                    }
                }
            }
    }
}