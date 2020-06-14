package com.example.weatherforecast.Activities

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.weatherforecast.R
import com.example.weatherforecast.Util.login
import com.example.weatherforecast.Util.toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import java.util.concurrent.TimeUnit

class VerifyMobileActivity : AppCompatActivity() {

    lateinit var layoutPhone : LinearLayout
    lateinit var ccp : CountryCodePicker
    lateinit var edit_text_phone : EditText
    lateinit var button_send_verification : Button
    lateinit var progressbar_pic : ProgressBar

    lateinit var layoutVerification : LinearLayout
    lateinit var progressbar : ProgressBar
    lateinit var edit_text_code : EditText
    lateinit var button_verify : Button

    lateinit var txt_skip : TextView

    var verificationId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_mobile)

        layoutPhone = findViewById(R.id.layoutPhone)
        ccp = findViewById(R.id.ccp)
        edit_text_phone = findViewById(R.id.edit_text_phone)
        button_send_verification = findViewById(R.id.button_send_verification)
        progressbar_pic = findViewById(R.id.progressbar_pic)

        layoutVerification = findViewById(R.id.layoutVerification)
        progressbar = findViewById(R.id.progressbar)
        edit_text_code = findViewById(R.id.edit_text_phone)
        button_verify = findViewById(R.id.button_verify)

        txt_skip = findViewById(R.id.txt_skip)

        button_send_verification.setOnClickListener {

            val phone = edit_text_phone.text.toString().trim()

            if (phone.isEmpty() || phone.length != 10) {
                edit_text_phone.error = "Enter a valid phone"
                edit_text_phone.requestFocus()
                return@setOnClickListener
            }

            val phoneNumber = '+' + ccp.selectedCountryCode + phone

            PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(
                    phoneNumber,
                    60,
                    TimeUnit.SECONDS,
                    this@VerifyMobileActivity,
                    phoneAuthCallbacks
                )


            layoutPhone.visibility = View.GONE
            layoutVerification.visibility = View.VISIBLE
        }

        button_verify.setOnClickListener {
            val code = edit_text_code.text.toString().trim()

            if(code.isEmpty()){
                edit_text_code.error = "Code required"
                edit_text_code.requestFocus()
                return@setOnClickListener
            }

            verificationId?.let{
                val credential = PhoneAuthProvider.getCredential(it, code)
                addPhoneNumber(credential)
            }
        }

        txt_skip.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Confirmation")
            dialog.setMessage("Are you sure you want to skip this step?")
            dialog.setPositiveButton("Yes"){ _,_ ->
                login()
            }
            dialog.setNegativeButton("No"){ _,_ ->

            }
            dialog.create()
            dialog.show()
        }

    }

    val phoneAuthCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            phoneAuthCredential?.let{
                addPhoneNumber(it)
            }
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            exception?.message?.let {
                toast(it)
            }
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId,token)
            this@VerifyMobileActivity.verificationId = verificationId
        }

    }

    fun addPhoneNumber(phoneAuthCredential: PhoneAuthCredential){
        FirebaseAuth.getInstance()
            .currentUser?.updatePhoneNumber(phoneAuthCredential)
            ?.addOnCompleteListener{task ->
                if (task.isSuccessful){
                    toast("Phone Verification Successful")
                    login()
                }
                else{
                    task.exception?.message?.let {
                        toast(it)
                    }
                }
            }
    }

}