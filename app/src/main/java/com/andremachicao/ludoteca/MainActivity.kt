package com.andremachicao.ludoteca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.andremachicao.ludoteca.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(JuegosFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.juegos -> replaceFragment(JuegosFragment())
                R.id.chat -> replaceFragment(ChatFragment())
                R.id.contacto -> replaceFragment(ContactFragment())

                else -> {

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragManager = supportFragmentManager
        val fragmentTransaction = fragManager.beginTransaction()
        fragmentTransaction.replace(R.id.MainScreenFrameLayout,fragment)
        fragmentTransaction.commit()
    }
}