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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
private lateinit var binding: FragmentLoginBinding
private lateinit var auth: FirebaseAuth
private lateinit var progressDialog: ProgressDialog
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
            progressDialog.dismiss()
            val user = auth.currentUser
            val email = user!!.email
            Toast.makeText(context,"Ingreso con: $email",Toast.LENGTH_SHORT).show()
            val goToMain = LoginFragmentDirections.actionLoginFragmentToMainActivity()
            findNavController().navigate(goToMain)
        }.addOnFailureListener{ e ->
            progressDialog.dismiss()
            Toast.makeText(context,"No se pudo ingresar debido a ${e.message}",Toast.LENGTH_SHORT).show()
        }
    }


}