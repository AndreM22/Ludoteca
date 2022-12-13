package com.andremachicao.ludoteca.profile

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andremachicao.ludoteca.R
import com.andremachicao.ludoteca.databinding.FragmentEditProfileBinding
import com.andremachicao.ludoteca.databinding.FragmentEditProfileBindingImpl
import com.andremachicao.ludoteca.exchange.ExchangeViewModel
import com.andremachicao.ludoteca.firebase_MVVM.data.exchange.model.Exchange
import com.andremachicao.ludoteca.game.AddGameFragment
import com.andremachicao.ludoteca.game.GameUpdateFragmentDirections
import com.andremachicao.ludoteca.sharedPreferences.InitApp.Companion.prefs
import com.andremachicao.ludoteca.utils.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.lang.Exception

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val args: EditProfileFragmentArgs by navArgs()
    private lateinit var auth: FirebaseAuth
    private var storage = Firebase.storage
    private val db = Firebase.firestore
    private lateinit var progressDialog: ProgressDialog
    private var imageSelected = false
    private val exchangeViewModel: ExchangeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.profileUpdate = args.profileToUpdate
        auth = Firebase.auth
        loadUserPhoto()
        //Progress dialog
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Porfavor espere")
        progressDialog.setMessage("Registrando...")
        progressDialog.setCanceledOnTouchOutside(false)
        binding.EditProfileSaveButton.setOnClickListener{
            uploadPhoto()
            progressDialog.show()
        }
        binding.btUploadImageProfile.setOnClickListener {
            it.hideKeyboard()
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePhotoIntent, AddGameFragment.CAMERA_CHOOSE)
        }
        binding.btUploadGalleryProfile.setOnClickListener {
            it.hideKeyboard()
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, AddGameFragment.PERMISSION_CODE)
                }else{
                    chooseImageGallery()
                }
            }else{
                chooseImageGallery()
            }
        }
        exchangeViewModel.updateGameEx.observe(viewLifecycleOwner){state ->
            when(state){
                is UiState.Loading ->{

                }
                is UiState.Failure ->{
                    toast(state.error)
                }
                is UiState.Success ->{
                    toast("Se actualizo el juego")

                }
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val goToPrevious = EditProfileFragmentDirections.actionEditProfileFragmentToMyProfileFragment()
                findNavController().navigate(goToPrevious)
            }
        })
    }

    private fun uploadPhoto(){
        if(imageSelected){
            val storageRef = storage.reference
            val imageRoute = storageRef.child("${auth.currentUser?.email}/profile/img_1.jpeg")
            binding.EditProfileImage.isDrawingCacheEnabled =true
            binding.EditProfileImage.buildDrawingCache()
            val bitmap = (binding.EditProfileImage.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos)
            val data = baos.toByteArray()
            if(prefs.getImage() == ""){
                var uploadTask = imageRoute.putBytes(data)
                uploadTask.addOnFailureListener{
                }.addOnSuccessListener {
                    imageRoute.downloadUrl.addOnSuccessListener {
                        Log.v("STORAGE", "-------->>>$it")
                        updateProfile(it.toString())
                        }
                    }
            }else{
                imageRoute.delete().addOnSuccessListener {
                    var uploadTask = imageRoute.putBytes(data)
                    uploadTask.addOnFailureListener{
                    }.addOnSuccessListener {
                        imageRoute.downloadUrl.addOnSuccessListener {
                            Log.v("STORAGE", "-------->>>$it")
                            updateProfile(it.toString())
                        }
                    }

                }.addOnFailureListener{ e ->
                    Log.d(ContentValues.TAG,"El error es: $e")
                }
            }
        }else{
            updateProfile(prefs.getImage())
        }

    }
    private fun updateProfile(image:String){
        try{
            val profileMap = mapOf(
                "id" to args.profileToUpdate.id,
                "names" to binding.EditProfileFirstName.text.toString(),
                "lastnames" to binding.EditProfileLastName.text.toString(),
                "email" to args.profileToUpdate.email,
                "stars" to args.profileToUpdate.stars,
                "image" to image
            )
            auth.currentUser?.email?.let {
                db.collection("users").document(it).collection("Profile")
                    .document(args.profileToUpdate.id).update(profileMap).addOnSuccessListener {
                        updateSharedPrefs(image)
                        updateGamesExchanged()
                        progressDialog.dismiss()
                        Toast.makeText(context,"Se Actualizo el perfil", Toast.LENGTH_SHORT).show()
                        val goToMainProfile = EditProfileFragmentDirections.actionEditProfileFragmentToMyProfileFragment()
                        findNavController().navigate(goToMainProfile)
                    }}

        }catch (e:Exception){
            progressDialog.dismiss()
            Toast.makeText(context,"Datos incompletos, porfavor llenar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateSharedPrefs(image:String){
        prefs.wipe()
        prefs.saveId(args.profileToUpdate.id)
        prefs.saveName(binding.EditProfileFirstName.text.toString())
        prefs.saveLastNames(binding.EditProfileLastName.text.toString())
        prefs.saveEmail(args.profileToUpdate.email)
        prefs.saveStar(args.profileToUpdate.stars)
        prefs.saveImage(image)
    }
    private fun updateGamesExchanged(){
        db.collection("exchange")
            .whereEqualTo("profileid",prefs.getId())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    exchangeViewModel.updateGameExchange(
                        Exchange(
                            id = document.data["id"] as String,
                            exchangetype = document.data["exchangetype"] as String,
                            gameid = document.data["gameid"] as String,
                            gamename = document.data["gamename"] as String,
                            gamestate = document.data["gamestate"] as Double,
                            gamelanguage = document.data["gamelanguage"] as String,
                            gamedescription = document.data["gamedescription"] as String,
                            gameplayers = (document.data["gameplayers"] as Long).toInt(),
                            gametime = document.data["gametime"] as String,
                            gameprice = document.data["gameprice"] as Double,
                            gamelocation = document.data["gamelocation"] as String,
                            gameimages = document.data["gameimages"] as List<String>,
                            profileid = prefs.getId(),
                            profilenames = prefs.getName(),
                            profilelastnames = prefs.getLastNames(),
                            profileemail = prefs.getEmail(),
                            profileimage = prefs.getImage(),
                            stars = prefs.getStars().toDouble(),
                        )
                    )
                }

            }
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*?"
        startActivityForResult(intent, AddGameFragment.IMAGE_CHOOSE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddGameFragment.IMAGE_CHOOSE && resultCode == Activity.RESULT_OK){
            binding.EditProfileImage.setImageURI(data?.data)
            imageSelected = true
        }else if(requestCode == AddGameFragment.CAMERA_CHOOSE && resultCode == Activity.RESULT_OK) {
            val imgBitMap: Bitmap? = data!!.extras!!.get("data") as Bitmap?
            binding.EditProfileImage.setImageBitmap(imgBitMap)
            imageSelected = true

            }
        }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            AddGameFragment.PERMISSION_CODE ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseImageGallery()
                }else{
                    Toast.makeText(context,"Permission denied",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadUserPhoto(){
        if(prefs.getImage() != ""){
            Glide.with(binding.EditProfileImage)
                .load(prefs.getImage())
                .into(binding.EditProfileImage)
        }
    }
}