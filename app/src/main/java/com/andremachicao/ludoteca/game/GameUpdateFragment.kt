package com.andremachicao.ludoteca.game

import android.Manifest
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andremachicao.ludoteca.R
import com.andremachicao.ludoteca.databinding.FragmentGameUpdateBinding
import com.andremachicao.ludoteca.game.model.Game
import com.andremachicao.ludoteca.setImageSrcUrl
import com.andremachicao.ludoteca.utils.hideKeyboard
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.properties.Delegates

class GameUpdateFragment:Fragment() {

    private lateinit var binding: FragmentGameUpdateBinding
    private val args: GameUpdateFragmentArgs by navArgs()
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private var imgCount = 1
    private var storage = Firebase.storage
    private var numericalState by Delegates.notNull<Double>()
    private lateinit var progressDialog: ProgressDialog
    private var list = mutableListOf<String>()
    private var deletedImages = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameUpdateBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = Firebase.auth
        //Progress dialog
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Porfavor espere")
        progressDialog.setMessage("Registrando...")
        progressDialog.setCanceledOnTouchOutside(false)

        //Cargar los datos del juego sin contar imagenes y el spinner
        binding.edtxNameGameUpdate.setText(args.gameInfo.name)
        binding.edtxLanguageInputUpdate.setText(args.gameInfo.language)
        binding.edtxDescriptionGameUpdate.setText(args.gameInfo.description)
        binding.edtxPlayersInputUpdate.setText(args.gameInfo.players.toString())
        binding.edtxTimeInputUpdate.text = args.gameInfo.time
        binding.edtxPriceInputUpdate.setText(args.gameInfo.price.toString())
        binding.edtxLocationInputUpdate.setText(args.gameInfo.location)

        ///Para Recoger el valor del spinner
        val listStates = arrayOf("Seleccione un estado","Usado","Poco uso","Nuevo")
        var spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(view.context,
            R.layout.spinner_element_config,listStates)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_elemet_list_config)
        binding.spinnerStateGameUpdate.adapter = spinnerAdapter
        val spinnerStartValue = args.gameInfo.state.toInt()

        // Para colocar las imagenes
        list = args.gameInfo.images as MutableList<String>
        imgCount += list.size
        binding.img1GamesUpdate.visibility = View.VISIBLE
        binding.btDeleteImg1.visibility = View.VISIBLE
        Glide.with(binding.img1GamesUpdate)
            .load(list[0])
            .into(binding.img1GamesUpdate)
        if(list.size >1){
            binding.img2GamesUpdate.visibility = View.VISIBLE
            binding.btDeleteImg2.visibility = View.VISIBLE
            Glide.with(binding.img2GamesUpdate)
                .load(list[0])
                .into(binding.img2GamesUpdate)
            if(list.size ==3){
                binding.img3GamesUpdate.visibility = View.VISIBLE
                binding.btDeleteImg3.visibility = View.VISIBLE
                Glide.with(binding.img3GamesUpdate)
                    .load(list[0])
                    .into(binding.img3GamesUpdate)
            }
        }
        //Delete images control
        binding.btDeleteImg1.setOnClickListener {
            imgCount -= 1
            binding.img1GamesUpdate.visibility= View.GONE
            binding.btDeleteImg1.visibility = View.GONE
            deletedImages.add(1)
            /*
            val storageRef = storage.reference
            val desertRef1 = storageRef.child("${auth.currentUser?.email}/games/${args.gameInfo.id}/images/img_1.jpeg")
            desertRef1.delete().addOnSuccessListener {
            }.addOnFailureListener{ e ->
                Log.d(ContentValues.TAG,"El error es: $e")
            }
            list.remove(args.gameInfo.images[0])
             */

        }
        binding.btDeleteImg2.setOnClickListener {
            imgCount -=1
            binding.img2GamesUpdate.visibility = View.GONE
            binding.btDeleteImg2.visibility = View.GONE
            deletedImages.add(2)
            /*
            val storageRef = storage.reference
            val desertRef2 = storageRef.child("${auth.currentUser?.email}/games/${args.gameInfo.id}/images/img_2.jpeg")
            desertRef2.delete().addOnSuccessListener {
            }.addOnFailureListener{ e ->
                Log.d(ContentValues.TAG,"El error es: $e")
            }
            list.remove(args.gameInfo.images[1])

             */
        }
        binding.btDeleteImg3.setOnClickListener {
            imgCount -= 1
            binding.img3GamesUpdate.visibility = View.GONE
            binding.btDeleteImg3.visibility = View.GONE
            deletedImages.add(3)
            /*
            val storageRef = storage.reference
            val desertRef3 = storageRef.child("${auth.currentUser?.email}/games/${args.gameInfo.id}/images/img_3.jpeg")
            desertRef3.delete().addOnSuccessListener {
            }.addOnFailureListener{ e ->
                Log.d(ContentValues.TAG,"El error es: $e")
            }
            list.remove(args.gameInfo.images[2])
            */
        }


        //Spinner Control
        var state = "Seleccione un estado"
        numericalState = 0.00
        binding.spinnerStateGameUpdate.setSelection(spinnerStartValue)
        binding.spinnerStateGameUpdate.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                state = binding.spinnerStateGameUpdate.selectedItem.toString()
                p1?.hideKeyboard()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                state = "Seleccione un estado"
                p0?.hideKeyboard()
            }

        }
        // Parte para configurar el calendario
        binding.btDateUpdate.setOnClickListener{
            it.hideKeyboard()
            binding.datePickerContainerUpdate.visibility= View.VISIBLE
            binding.btAddGameAcceptUpdate.visibility= View.GONE
            binding.btIncreasePlayersUpdate.visibility= View.GONE
            binding.btDecreasePlayersUpdate.visibility = View.GONE
            binding.edtxTimeInputUpdate.visibility = View.GONE
            binding.btDateUpdate.visibility = View.GONE
            binding.edtxPriceInputUpdate.visibility= View.GONE
            binding.edtxLocationInputUpdate.visibility = View.GONE
            binding.bolivianosInfoUpdate.visibility = View.GONE
            binding.imageContainerUpdate.visibility = View.GONE
            binding.spinnerStateGameUpdate.visibility = View.GONE
        }
        binding.idDatePickerItemUpdate.setOnDateChangedListener{
                date,year,month,day ->
            binding.edtxTimeInputUpdate.setText(getDateFromDatePicker())

        }
        binding.confirmDateUpdate.setOnClickListener {
            binding.datePickerContainerUpdate.visibility= View.GONE
            binding.btAddGameAcceptUpdate.visibility= View.VISIBLE
            binding.btIncreasePlayersUpdate.visibility= View.VISIBLE
            binding.btDecreasePlayersUpdate.visibility = View.VISIBLE
            binding.edtxTimeInputUpdate.visibility = View.VISIBLE
            binding.btDateUpdate.visibility = View.VISIBLE
            binding.edtxPriceInputUpdate.visibility= View.VISIBLE
            binding.edtxLocationInputUpdate.visibility = View.VISIBLE
            binding.bolivianosInfoUpdate.visibility = View.VISIBLE
            binding.imageContainerUpdate.visibility = View.VISIBLE
            binding.spinnerStateGameUpdate.visibility = View.VISIBLE
        }

        binding.btIncreasePlayersUpdate.setOnClickListener{
            it.hideKeyboard()
            var players = 0
            if(binding.edtxPlayersInputUpdate.text.toString() == "") {
                players = 1
                binding.edtxPlayersInputUpdate.setText(players.toString())
            }else{
                players = binding.edtxPlayersInputUpdate.text.toString().toInt()
                players++
                binding.edtxPlayersInputUpdate.setText(players.toString())
            }

        }
        binding.btDecreasePlayersUpdate.setOnClickListener{
            it.hideKeyboard()
            var players = 0
            if (binding.edtxPlayersInputUpdate.text.toString() == ""){
                binding.edtxPlayersInputUpdate.setText(players.toString())
            }else if(binding.edtxPlayersInputUpdate.text.toString().toInt() != 0){
                players = binding.edtxPlayersInputUpdate.text.toString().toInt()
                players--
                binding.edtxPlayersInputUpdate.setText(players.toString())
            }
        }

        binding.btGalleryUpdate.setOnClickListener {
            it.hideKeyboard()
            if(imgCount <=3){
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
            }else{
                Toast.makeText(context,"Ya no se pueden añadir mas imágenes",Toast.LENGTH_SHORT).show()
            }

        }
        binding.btTakePictureUpdate.setOnClickListener {
            it.hideKeyboard()
            if(imgCount <=3){
                val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePhotoIntent, AddGameFragment.CAMERA_CHOOSE)
            }else{
                Toast.makeText(context,"Ya no se pueden añadir mas imágenes",Toast.LENGTH_SHORT).show()
            }

        }
        binding.btAddGameAcceptUpdate.setOnClickListener {
            it.hideKeyboard()
            if (state != "Seleccione un estado"){
                when(state){
                    "Nuevo" -> numericalState = 3.00
                    "Poco uso" -> numericalState =2.00
                    "Usado" -> numericalState =1.00
                }
            }
            else{
                Toast.makeText(context,"Porfavor ingrese un estado",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (binding.edtxPlayersInputUpdate.text.toString() == "" || binding.edtxPlayersInputUpdate.text.toString().toInt() == 0){
                Toast.makeText(context,"Numero de jugadores no valido",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(imgCount ==1){
                Toast.makeText(context,"Porfavor registre almenos una imagen",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(binding.edtxTimeInputUpdate.text.toString() == "Fecha de fabricacion"){
                Toast.makeText(context,"Introduzca una fecha",Toast.LENGTH_SHORT).show()
                binding.edtxTimeInputUpdate.error = "Introduzca una fecha"
                return@setOnClickListener
            }

            val id = UUID.randomUUID().toString()
            progressDialog.show()
            uploadImages(id)
        }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val goToPrevious = GameUpdateFragmentDirections.actionGameUpdateFragmentToGameDetailsFragment(args.gameInfo)
                findNavController().navigate(goToPrevious)
            }
        })
    }

    private fun getDateFromDatePicker(): String {
        val day = binding.idDatePickerItemUpdate.dayOfMonth.toString().padStart(2,'0')
        val month = (binding.idDatePickerItemUpdate.month+1).toString().padStart(2,'0')
        val year= binding.idDatePickerItemUpdate.year.toString().padStart(4,'0')
        return "$day/$month/$year"
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*?"
        startActivityForResult(intent, AddGameFragment.IMAGE_CHOOSE)
    }

    private fun uploadImages(id:String) {
        val storageRef = storage.reference
        val listOfImages = mutableListOf<String>()
        //val imageRoute = storageRef.child("games/$id/images/img_${System.currentTimeMillis()}.jpeg")
        val imageRoute = storageRef.child("${auth.currentUser?.email}/games/$id/images/img_1.jpeg")
        binding.img1GamesUpdate.isDrawingCacheEnabled =true
        binding.img1GamesUpdate.buildDrawingCache()
        val bitmap = (binding.img1GamesUpdate.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos)
        val data = baos.toByteArray()
        var uploadTask = imageRoute.putBytes(data)
        if(imgCount == 2){
            uploadTask.addOnFailureListener{

            }.addOnSuccessListener {
                imageRoute.downloadUrl.addOnSuccessListener {
                    Log.v("STORAGE", "-------->>>$it")
                    listOfImages.add(it.toString())
                    Log.d(ContentValues.TAG,"El tamanio de lista es: ${listOfImages.size}")
                    uploadGame(listOfImages)
                }
            }
        }

        if (imgCount==3){
            val imageRoute2 = storageRef.child("${auth.currentUser?.email}/games/$id/images/img_2.jpeg")
            binding.img2GamesUpdate.isDrawingCacheEnabled =true
            binding.img2GamesUpdate.buildDrawingCache()
            val bitmap2 = (binding.img2GamesUpdate.drawable as BitmapDrawable).bitmap
            val baos2 = ByteArrayOutputStream()
            bitmap2.compress(Bitmap.CompressFormat.JPEG,50,baos2)
            val data2 = baos2.toByteArray()

            uploadTask.addOnFailureListener{

            }.addOnSuccessListener {
                imageRoute.downloadUrl.addOnSuccessListener {
                    Log.v("STORAGE", "-------->>>$it")
                    listOfImages.add(it.toString())
                    Log.d(ContentValues.TAG,"El tamanio de lista es: ${listOfImages.size}")
                }
            }

            var uploadTask2 = imageRoute2.putBytes(data2)
            uploadTask2.addOnFailureListener{

            }.addOnSuccessListener {
                imageRoute2.downloadUrl.addOnSuccessListener {
                    Log.v("STORAGE", "-------->>>$it")
                    listOfImages.add(it.toString())
                    Log.d(ContentValues.TAG,"El tamanio de lista es: ${listOfImages.size}")
                    uploadGame(listOfImages)
                }
            }
        }
        if (imgCount==4){
            val imageRoute2 = storageRef.child("${auth.currentUser?.email}/games/$id/images/img_2.jpeg")
            binding.img2GamesUpdate.isDrawingCacheEnabled =true
            binding.img2GamesUpdate.buildDrawingCache()
            val bitmap2 = (binding.img2GamesUpdate.drawable as BitmapDrawable).bitmap
            val baos2 = ByteArrayOutputStream()
            bitmap2.compress(Bitmap.CompressFormat.JPEG,50,baos2)
            val data2 = baos2.toByteArray()

            val imageRoute3 = storageRef.child("${auth.currentUser?.email}/games/$id/images/img_3.jpeg")
            binding.img3GamesUpdate.isDrawingCacheEnabled =true
            binding.img3GamesUpdate.buildDrawingCache()
            val bitmap3 = (binding.img3GamesUpdate.drawable as BitmapDrawable).bitmap
            val baos3 = ByteArrayOutputStream()
            bitmap3.compress(Bitmap.CompressFormat.JPEG,50,baos3)
            val data3 = baos3.toByteArray()

            uploadTask.addOnFailureListener{

            }.addOnSuccessListener {
                imageRoute.downloadUrl.addOnSuccessListener {
                    Log.v("STORAGE", "-------->>>$it")
                    listOfImages.add(it.toString())
                    Log.d(ContentValues.TAG,"El tamanio de lista es: ${listOfImages.size}")
                }
            }

            val uploadTask2 = imageRoute2.putBytes(data2)
            uploadTask2.addOnFailureListener{

            }.addOnSuccessListener {
                imageRoute2.downloadUrl.addOnSuccessListener {
                    Log.v("STORAGE", "-------->>>$it")
                    listOfImages.add(it.toString())
                    Log.d(ContentValues.TAG,"El tamanio de lista es: ${listOfImages.size}")
                }
            }
            val uploadTask3 = imageRoute3.putBytes(data3)
            uploadTask3.addOnFailureListener{

            }.addOnSuccessListener {
                imageRoute3.downloadUrl.addOnSuccessListener {
                    Log.v("STORAGE", "-------->>>$it")
                    listOfImages.add(it.toString())
                    Log.d(ContentValues.TAG,"El tamanio de lista es: ${listOfImages.size}")
                    uploadGame(listOfImages)
                }
            }
        }



    }

    private fun uploadGame(images:List<String>){
        try{
            val gameMap = mapOf(
                "id" to args.gameInfo.id,
                "name" to binding.edtxNameGameUpdate.text.toString(),
                "state" to numericalState,
                "language" to binding.edtxLanguageInputUpdate.text.toString(),
                "description" to binding.edtxDescriptionGameUpdate.text.toString(),
                "players" to binding.edtxPlayersInputUpdate.text.toString().toInt(),
                "time" to binding.edtxTimeInputUpdate.text.toString(),
                "price" to binding.edtxPriceInputUpdate.text.toString().toDouble(),
                "location" to binding.edtxLocationInputUpdate.text.toString(),
                "images" to images,
                "exchange" to args.gameInfo.exchange
            )

            auth.currentUser?.email?.let {
                db.collection("users").document(it).collection("Games").document(args.gameInfo.id).update(gameMap)
                    .addOnSuccessListener { documentReference ->
                        progressDialog.dismiss()
                        Log.d(ContentValues.TAG, "successful")
                        Toast.makeText(context,"Se Actualizo el juego", Toast.LENGTH_SHORT).show()
                        var goToMainGamePage = GameUpdateFragmentDirections.actionGameUpdateFragmentToGamesFragment()
                        findNavController().navigate(goToMainGamePage)

                    }
                    .addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Log.w(ContentValues.TAG, "Error adding document", e)
                    }
            }
        }catch (e: Exception){
            progressDialog.dismiss()
            Toast.makeText(context,"Datos incompletos, porfavor llenar", Toast.LENGTH_SHORT).show()
        }

    }

}