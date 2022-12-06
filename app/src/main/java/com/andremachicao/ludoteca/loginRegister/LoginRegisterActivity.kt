package com.andremachicao.ludoteca.loginRegister

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.andremachicao.ludoteca.MainActivity
import com.andremachicao.ludoteca.R
import com.andremachicao.ludoteca.databinding.ActivityLoginRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginRegisterActivity : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginRegisterBinding
    var isInLogIn : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)
        checkUser()
    }

    private fun checkUser(){
        val user = auth.currentUser
        if(user != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}