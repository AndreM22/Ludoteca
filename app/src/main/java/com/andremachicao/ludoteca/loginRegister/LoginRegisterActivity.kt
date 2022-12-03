package com.andremachicao.ludoteca.loginRegister

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.andremachicao.ludoteca.MainActivity
import com.andremachicao.ludoteca.R


class LoginRegisterActivity : AppCompatActivity(){

    private lateinit var login: Button
    private lateinit var register: Button
    var isInLogIn : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
        replaceFragment(loginFragment())

        login = findViewById(R.id.buttonLogin)
        register = findViewById(R.id.buttonRegister)

        login.isSelected=true
        register.isSelected=false

        login.setOnClickListener{
            if (!isInLogIn) {
                login.isSelected = true
                register.isSelected = false
                replaceFragment(loginFragment())
            }else{
                pasarAInicio()
            }
            isInLogIn = true
        }

        register.setOnClickListener{
            if (isInLogIn) {
                login.isSelected = false
                register.isSelected = true
                replaceFragment(registerFragment())
            }else{
                pasarAInicio()
            }
            isInLogIn = false
        }

    }

    private fun replaceFragment(fragment: Fragment){
        val fragManager = supportFragmentManager
        val fragmentTransaction = fragManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentLoginRegister,fragment)
        fragmentTransaction.commit()
    }

    private fun pasarAInicio(){
        startActivity(Intent(this, MainActivity::class.java))
    }


}