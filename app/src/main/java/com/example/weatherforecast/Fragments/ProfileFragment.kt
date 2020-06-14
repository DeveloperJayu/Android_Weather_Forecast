package com.example.weatherforecast.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.weatherforecast.Activities.VerifyMobileActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.Util.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class ProfileFragment : Fragment() {

    lateinit var edit_text_name : EditText
    lateinit var text_email : TextView
    lateinit var text_not_verified : TextView
    lateinit var text_phone : TextView
    lateinit var button_save : Button
    lateinit var progressbar : ProgressBar

    lateinit var mAuth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        edit_text_name = view.findViewById(R.id.edit_text_name)
        text_email = view.findViewById(R.id.text_email)
        text_not_verified = view.findViewById(R.id.text_not_verified)
        text_phone = view.findViewById(R.id.text_phone)
        button_save = view.findViewById(R.id.button_save)
        progressbar = view.findViewById(R.id.progressbar)

        mAuth = FirebaseAuth.getInstance()

        mAuth.currentUser?.let{user ->
            edit_text_name.setText(user.displayName)
            text_email.text = user.email

            text_phone.text = if (user.phoneNumber.isNullOrEmpty()) "Add Number" else user.phoneNumber

            if (user.isEmailVerified){
                text_not_verified.visibility = View.GONE
            }
            else{
                text_not_verified.visibility = View.VISIBLE
            }
        }

        button_save.setOnClickListener {
            val name = edit_text_name.text.toString().trim()

            if (name.isEmpty()){
                edit_text_name.error = "Name Required"
                edit_text_name.requestFocus()
                return@setOnClickListener
            }
            val updates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            progressbar.visibility = View.VISIBLE

            mAuth.currentUser?.updateProfile(updates)
                ?.addOnCompleteListener{task ->
                    progressbar.visibility = View.INVISIBLE
                    if (task.isSuccessful) {
                        context?.toast("Profile Updated")
                    } else {
                        context?.toast(task.exception?.message!!)
                    }
                }
        }
        text_not_verified.setOnClickListener {

            mAuth.currentUser?.sendEmailVerification()
                ?.addOnCompleteListener {
                    if(it.isSuccessful){
                        context?.toast("Verification Email Sent")
                    }else{
                        context?.toast(it.exception?.message!!)
                    }
                }

        }

        text_phone.setOnClickListener{
            val intent = Intent(activity,VerifyMobileActivity::class.java)
            startActivity(intent)
        }



        // Inflate the layout for this fragment
        return view
    }
}