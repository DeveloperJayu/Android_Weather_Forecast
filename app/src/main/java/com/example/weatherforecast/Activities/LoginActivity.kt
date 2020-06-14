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

class LoginActivity : AppCompatActivity() {

    lateinit var loginLayout : RelativeLayout
    lateinit var edit_text_email : EditText
    lateinit var edit_text_password : EditText
    lateinit var text_view_forget_password : TextView
    lateinit var button_sign_in : Button
    lateinit var text_view_register : TextView
    lateinit var progressBar: ProgressBar
    lateinit var mAuth : FirebaseAuth
    lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginLayout = findViewById(R.id.loginLayout)
        edit_text_email = findViewById(R.id.edit_text_email)
        edit_text_password = findViewById(R.id.edit_text_password)
        text_view_forget_password = findViewById(R.id.text_view_forget_password)
        button_sign_in = findViewById(R.id.button_sign_in)
        text_view_forget_password = findViewById(R.id.text_view_forget_password)
        progressBar = findViewById(R.id.progressBar)
        text_view_register = findViewById(R.id.text_view_register)
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

            loginUser(email, password)
        }

        text_view_register.setOnClickListener {
            val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
        }

        text_view_forget_password.setOnClickListener {
            val intent = Intent(this@LoginActivity,ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Login"
    }

    private fun loginUser(email: String, password: String) {
        progressBar.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful){
                    login()
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
}