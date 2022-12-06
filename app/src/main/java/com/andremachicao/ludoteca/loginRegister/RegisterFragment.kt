package com.andremachicao.ludoteca.loginRegister

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.andremachicao.ludoteca.R
import com.andremachicao.ludoteca.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment() {
    private lateinit var binding:FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog:ProgressDialog
    private var email = ""
    private var pass =""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Progress dialog
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Porfavor espere")
        progressDialog.setMessage("Registrando...")
        progressDialog.setCanceledOnTouchOutside(false)

        //Firebase auth
        auth= Firebase.auth
        binding.btLogInRegisterPage.setOnClickListener {
            val goToMain = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(goToMain)
        }
        binding.btRegisterRegisterPage.setOnClickListener {
            validateData()
            }

        }

    private fun validateData(){
        email =binding.editTextRegisterEmail.text.toString().trim()
        val lastName =binding.editTextRegisterLastName.text.toString()
        val name = binding.editTextRegisterName.text.toString()
        pass =binding.editTextREgisterPassword.text.toString().trim()
        val confirmPass =binding.edxtConfirmPassRegisterPage.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.editTextRegisterEmail.error = "Formato invalido de correo"
        }
        else if(TextUtils.isEmpty(pass)){
            binding.editTextREgisterPassword.error ="Porfavor ingrese contraseña"
        }else if (binding.edxtConfirmPassRegisterPage.text.toString().trim() != binding.editTextREgisterPassword.text.toString().trim()){
            binding.edxtConfirmPassRegisterPage.error = "La contraseña no coincide"
        }else{
            firebaseRegister()
        }
    }
    private fun firebaseRegister(){
        progressDialog.show()
        auth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener {
            progressDialog.dismiss()
            val user = auth.currentUser
            val email = user!!.email
            Toast.makeText(context,"Se registro con el email: $email",Toast.LENGTH_SHORT).show()
            val goToMainPage = RegisterFragmentDirections.actionRegisterFragmentToMainActivity()
            findNavController().navigate(goToMainPage)
        }.addOnFailureListener{ e ->
            progressDialog.dismiss()
            Toast.makeText(context,"No se pudo registrar por ${e.message}",Toast.LENGTH_SHORT).show()
        }
    }
}