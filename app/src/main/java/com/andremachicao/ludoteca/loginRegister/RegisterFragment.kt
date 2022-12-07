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
import com.andremachicao.ludoteca.databinding.FragmentRegisterBinding
import com.andremachicao.ludoteca.profile.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class RegisterFragment : Fragment() {
    private lateinit var binding:FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
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
        val lastName =binding.editTextRegisterLastName.text.toString().trim()
        val name = binding.editTextRegisterName.text.toString().trim()
        pass =binding.editTextREgisterPassword.text.toString().trim()
        val confirmPass =binding.edxtConfirmPassRegisterPage.text.toString().trim()
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.editTextRegisterEmail.error = "Formato invalido de correo"
        }else if(TextUtils.isEmpty(name)){
            binding.editTextRegisterName.error = "Ingrese un nombre"
        }else if(TextUtils.isEmpty(lastName)){
            binding.editTextRegisterLastName.error ="Ingrese un apellido"
        }
        else if(TextUtils.isEmpty(pass)){
            binding.editTextREgisterPassword.error ="Porfavor ingrese contraseña"
        }else if(pass.length <6){
            binding.editTextREgisterPassword.error ="La contraseña debe tener 6 carateres min"
        }
        else if (confirmPass != pass){
            binding.edxtConfirmPassRegisterPage.error = "La contraseña no coincide"
        }else{
            firebaseRegister(name,lastName)
        }
    }
    private fun firebaseRegister(name:String,lastName:String){
        progressDialog.show()
        auth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener {
            val id = UUID.randomUUID().toString()
            try{
                val profile = Profile(
                    id= id,
                    names = name,
                    lastnames = lastName,
                    email = email,
                    starts = 1.00
                )
                db.collection("users").document(email)
                    .collection("Profile").document(id).set(profile)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(context,"Se registro con el email: $email",Toast.LENGTH_SHORT).show()
                        val goToMainPage = RegisterFragmentDirections.actionRegisterFragmentToMainActivity()
                        findNavController().navigate(goToMainPage)
                }
            }catch (e: Exception){
                progressDialog.dismiss()
                Toast.makeText(context,"Datos incompletos, porfavor llenar",Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener{ e ->
            progressDialog.dismiss()
            Toast.makeText(context,"No se pudo registrar por ${e.message}",Toast.LENGTH_SHORT).show()
        }
    }
}