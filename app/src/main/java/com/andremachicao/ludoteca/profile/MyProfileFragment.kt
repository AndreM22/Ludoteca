package com.andremachicao.ludoteca.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.andremachicao.ludoteca.databinding.FragmentMyProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MyProfileFragment : Fragment() {

    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = Firebase.auth
        loadInfo()
        binding.MyProfileLogOutButton.setOnClickListener{
            auth.signOut()
            val goToLoginPage = MyProfileFragmentDirections.actionMyProfileFragmentToLoginRegisterActivity()
            findNavController().navigate(goToLoginPage)
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
                        starts = document.data["starts"] as Double

                    )
                    profileInfo.add(profile)
                }
                if (profileInfo.isNotEmpty()){
                    binding.profileInfo = profileInfo[0]
                    val completeName = "${profileInfo[0].names} ${profileInfo[0].lastnames}"
                    binding.MyProfileName.text = completeName
                }
            }
        }
        }
    }
