package com.andremachicao.ludoteca.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.andremachicao.ludoteca.databinding.FragmentMyProfileBinding
import com.andremachicao.ludoteca.sharedPreferences.InitApp.Companion.prefs
import com.bumptech.glide.Glide
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
        loadProfilePage()
        loadUserPhoto()
        binding.MyProfileLogOutButton.setOnClickListener{
            auth.signOut()
            prefs.wipe()
            val goToLoginPage = MyProfileFragmentDirections.actionMyProfileFragmentToLoginRegisterActivity()
            findNavController().navigate(goToLoginPage)
        }
        binding.MyProfileEditProfileButton.setOnClickListener{
            val goToUpdatePage = MyProfileFragmentDirections.actionMyProfileFragmentToEditProfileFragment(loadInfo())
            findNavController().navigate(goToUpdatePage)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }

    private fun loadInfo(): Profile {
        return Profile(
            id = prefs.getId(),
            names = prefs.getName(),
            lastnames = prefs.getLastNames(),
            email = prefs.getEmail(),
            stars = prefs.getStars().toDouble(),
            image = prefs.getImage()
        )
    }

    private fun loadProfilePage(){
        binding.profileInfo = loadInfo()
        val completeName = "${loadInfo().names} ${loadInfo().lastnames}"
        binding.MyProfileName.text = completeName
    }
    private fun loadUserPhoto(){
        if(prefs.getImage() != ""){
            Glide.with(binding.MyProfileImage)
                .load(prefs.getImage())
                .into(binding.MyProfileImage)
        }
    }

}


