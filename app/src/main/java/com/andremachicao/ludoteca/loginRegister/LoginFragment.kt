package com.andremachicao.ludoteca.loginRegister

import android.app.ActionBar
import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.andremachicao.ludoteca.R
import com.andremachicao.ludoteca.databinding.FragmentLoginBinding
import com.andremachicao.ludoteca.profile.Profile
import com.andremachicao.ludoteca.sharedPreferences.InitApp.Companion.prefs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
private lateinit var binding: FragmentLoginBinding
private lateinit var auth: FirebaseAuth
private lateinit var progressDialog: ProgressDialog
private val db = Firebase.firestore
private var email =""
private var pass =""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       //Progress dialog
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Porfavor espere")
        progressDialog.setMessage("Ingresando...")
        progressDialog.setCanceledOnTouchOutside(false)

        //Firebase auth
        auth= Firebase.auth
        binding.btLogInPage.setOnClickListener {
            validateData()
        }

        binding.btRegisterLogInPage.setOnClickListener {
            val goToRegister = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(goToRegister)
        }
    }
    private fun validateData(){
        email = binding.editTextLoginEmail.text.toString().trim()
        pass = binding.editTextLoginPassword.text.toString().trim()
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.editTextLoginEmail.error = "Formato invalido de correo"
        }
        else if(TextUtils.isEmpty(pass)){
            binding.editTextLoginPassword.error ="Porfavor ingrese contraseña"
        }else{
            firebaseLogin()
        }
    }
    private fun firebaseLogin(){
        progressDialog.show()
        auth.signInWithEmailAndPassword(email,pass).addOnSuccessListener {
            val user = auth.currentUser
            val email = user!!.email
            loadInfo()
            progressDialog.dismiss()
            Toast.makeText(context,"Ingreso con: $email",Toast.LENGTH_SHORT).show()
            val goToMain = LoginFragmentDirections.actionLoginFragmentToMainActivity()
            findNavController().navigate(goToMain)
        }.addOnFailureListener{ e ->
            progressDialog.dismiss()
            Toast.makeText(context,"No se pudo ingresar debido a ${e.message}",Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadInfo(){
        //Tener consistnecia de datos, tener base de datos del celular
        auth.currentUser?.email?.let {
            db.collection("users").document(it).collection("Profile").get().addOnSuccessListener { result ->
                val profileInfo = mutableListOf<Profile>()
                for(document in result){
                    val profile = Profile(
                        id = document.data["id"] as String,
                        names = document.data["names"] as String,
                        lastnames = document.data["lastnames"] as String,
                        email = document.data["email"] as String,
                        stars = document.data["stars"] as Double,
                        image = document.data["image"] as String

                    )
                    profileInfo.add(profile)
                }
                prefs.saveId(profileInfo[0].id)
                prefs.saveName(profileInfo[0].names)
                prefs.saveLastNames(profileInfo[0].lastnames)
                prefs.saveEmail(profileInfo[0].email)
                prefs.saveStar(profileInfo[0].stars)
                prefs.saveImage(profileInfo[0].image)
            }
        }
    }


}