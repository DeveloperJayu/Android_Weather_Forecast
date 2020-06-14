package com.example.weatherforecast.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.weatherforecast.R
import com.example.weatherforecast.Util.login
import com.example.weatherforecast.Util.toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    lateinit var loginLayout : RelativeLayout
    lateinit var edit_text_email : EditText
    lateinit var edit_text_password : EditText
    lateinit var button_sign_in : Button
    lateinit var text_view_login : TextView
    lateinit var progressBar: ProgressBar
    lateinit var toolbar : Toolbar
    lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        loginLayout = findViewById(R.id.loginLayout)
        edit_text_email = findViewById(R.id.edit_text_email)
        edit_text_password = findViewById(R.id.edit_text_password)
        button_sign_in = findViewById(R.id.button_sign_in)
        progressBar = findViewById(R.id.progressBar)
        text_view_login = findViewById(R.id.text_view_login)
        toolbar = findViewById(R.id.toolbar)

        mAuth = FirebaseAuth.getInstance()

        setUpToolbar()

        button_sign_in.setOnClickListener {
            val email = edit_text_email.text.toString()
            val password = edit_text_password.text.toString()

            if (email.isEmpty()&&password.isEmpty()){
                edit_text_email.error = "Email Required"
                edit_text_email.requestFocus()
                edit_text_password.error = "Password Required"
                edit_text_password.requestFocus()
                return@setOnClickListener
            }
            else if (email.isEmpty()){
                edit_text_email.error = "Email Required"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edit_text_email.error = "Valid Email Required"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }
            else if (password.isEmpty()){
                edit_text_password.error = "Password Required"
                edit_text_password.requestFocus()
                return@setOnClickListener
            }

            registerUser(email, password)
        }

        text_view_login.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(email: String, password: String) {
        progressBar.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful){
                    val intent = Intent(this@RegisterActivity,VerifyMobileActivity::class.java)
                    startActivity(intent)
                }
                else{
                    task.exception?.message?.let{
                        toast(it)
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()
        mAuth.currentUser?.let{
            login()
        }
    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register"
    }
}