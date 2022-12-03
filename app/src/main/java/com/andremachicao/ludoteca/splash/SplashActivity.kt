package com.andremachicao.ludoteca.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.andremachicao.ludoteca.R
import com.andremachicao.ludoteca.loginRegister.LoginRegisterActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startTimer()
    }

    fun startTimer(){
        object: CountDownTimer(3000,1000){
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                val intent = Intent(applicationContext,LoginRegisterActivity::class.java).apply{}
                startActivity(intent)
                finish()
            }

        }.start()
    }
}