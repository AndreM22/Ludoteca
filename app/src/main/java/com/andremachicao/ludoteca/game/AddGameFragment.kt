package com.andremachicao.ludoteca.game

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.andremachicao.ludoteca.databinding.FragmentAddGameBinding
import com.andremachicao.ludoteca.game.model.Game
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.koin.android.ext.android.bind
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.properties.Delegates

class AddGameFragment: Fragment(){
    private lateinit var binding: FragmentAddGameBinding
    private var img_count = 1
    private val db = Firebase.firestore
    private var storage = Firebase.storage
    private var numericalState by Delegates.notNull<Double>()

    companion object{
        private val IMAGE_CHOOSE = 1000;
        private val PERMISSION_CODE = 1001;
        private val CAMERA_CHOOSE = 1002;
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddGameBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Para configurar el estado
        var state = "Seleccione un estado"
        numericalState = 0.00
        //var numericalState:Double = 0.00
        val listStates = arrayOf("Seleccione un estado","Nuevo","Poco uso","Usado")
        var spinnerAdapter:ArrayAdapter<String> = ArrayAdapter(view.context,android.R.layout.simple_spinner_item,listStates)
        binding.spinnerState.adapter = spinnerAdapter
        binding.spinnerState.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                state = binding.spinnerState.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                state = "Seleccione un estado"
            }

        }
        /////////////////////////////////////////////////////
       // Parte para configurar el calendario
        binding.btDate.setOnClickListener{
            binding.datePickerContainer.visibility= View.VISIBLE
            binding.btAddGameAccept.visibility= View.GONE
            binding.btIncreasePlayers.visibility= View.GONE
            binding.btDecreasePlayers.visibility = View.GONE
            binding.edtxTimeInput.visibility = View.GONE
            binding.btDate.visibility = View.GONE
            binding.edtxPriceInput.visibility= View.GONE
            binding.edtxLocationInput.visibility = View.GONE
            binding.bolivianosInfo.visibility = View.GONE
        }
        binding.idDatePickerItem.setOnDateChangedListener{
            date,year,month,day ->
            binding.edtxTimeInput.setText(getDateFromDatePicker())

        }
        binding.confirmDate.setOnClickListener {
            binding.datePickerContainer.visibility= View.GONE
            binding.btAddGameAccept.visibility= View.VISIBLE
            binding.btIncreasePlayers.visibility= View.VISIBLE
            binding.btDecreasePlayers.visibility = View.VISIBLE
            binding.edtxTimeInput.visibility = View.VISIBLE
            binding.btDate.visibility = View.VISIBLE
            binding.edtxPriceInput.visibility= View.VISIBLE
            binding.edtxLocationInput.visibility = View.VISIBLE
            binding.bolivianosInfo.visibility = View.VISIBLE
        }

        ///////////////////////////////////////////////////////////
        binding.btIncreasePlayers.setOnClickListener{
            var players = 0
            if(binding.edtxPlayersInput.text.toString() == "") {
                players = 1
                binding.edtxPlayersInput.setText(players.toString())
            }else{
                players = binding.edtxPlayersInput.text.toString().toInt()
                players++
                binding.edtxPlayersInput.setText(players.toString())
            }

        }
        binding.btDecreasePlayers.setOnClickListener{
            var players = 0
            if (binding.edtxPlayersInput.text.toString() == ""){
                binding.edtxPlayersInput.setText(players.toString())
            }else if(binding.edtxPlayersInput.text.toString().toInt() != 0){
                players = binding.edtxPlayersInput.text.toString().toInt()
                players--
                binding.edtxPlayersInput.setText(players.toString())
            }
        }
        binding.btGallery.setOnClickListener {
            if(img_count <=3){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        requestPermissions(permissions,PERMISSION_CODE)
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
        binding.btTakePicture.setOnClickListener {
            if(img_count <=3){
                val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePhotoIntent, CAMERA_CHOOSE)
            }else{
                Toast.makeText(context,"Ya no se pueden añadir mas imágenes",Toast.LENGTH_SHORT).show()
            }

        }
        binding.btAddGameAccept.setOnClickListener {
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
            if (binding.edtxPlayersInput.text.toString() == "" || binding.edtxPlayersInput.text.toString().toInt() == 0){
                Toast.makeText(context,"Numero de jugadores no valido",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(img_count ==1){
                Toast.makeText(context,"Porfavor registre almenos una imagen",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = UUID.randomUUID().toString()
            uploadImages(id)
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val goToPrevious = AddGameFragmentDirections.actionAddGameFragmentToGamesFragment()
                findNavController().navigate(goToPrevious)
            }
        })
        }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*?"
        startActivityForResult(intent, IMAGE_CHOOSE)
    }

    fun getDateFromDatePicker(): String {
        val day = binding.idDatePickerItem.dayOfMonth.toString().padStart(2,'0')
        val month = (binding.idDatePickerItem.month+1).toString().padStart(2,'0')
        val year= binding.idDatePickerItem.year.toString().padStart(4,'0')
        return day+"/"+month+"/"+year
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseImageGallery()
                }else{
                    Toast.makeText(context,"Permission denied",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK){
            when(img_count){
                1 ->{
                    binding.img1Games.setImageURI(data?.data)
                    binding.img1Games.visibility = View.VISIBLE
                }
                2 ->{
                    binding.img2Games.setImageURI(data?.data)
                    binding.img2Games.visibility = View.VISIBLE
                }
                3 ->{
                    binding.img3Games.setImageURI(data?.data)
                    binding.img3Games.visibility = View.VISIBLE
                }
            }
            img_count++

        }else if(requestCode == CAMERA_CHOOSE && resultCode == Activity.RESULT_OK){
            val imgBitMap: Bitmap? = data!!.extras!!.get("data") as Bitmap?
            when(img_count){
                1 ->{
                    binding.img1Games.setImageBitmap(imgBitMap)
                    binding.img1Games.visibility = View.VISIBLE
                }
                2 ->{
                    binding.img2Games.setImageBitmap(imgBitMap)
                    binding.img2Games.visibility = View.VISIBLE
                }
                3 ->{
                    binding.img3Games.setImageBitmap(imgBitMap)
                    binding.img3Games.visibility = View.VISIBLE
                }
            }
            img_count++

        }

    }

    private fun uploadImages(id:String) {
        val storageRef = storage.reference
        val listOfImages = mutableListOf<String>()
        //val imageRoute = storageRef.child("games/$id/images/img_${System.currentTimeMillis()}.jpeg")
        val imageRoute = storageRef.child("games/$id/images/img_1.jpeg")
        binding.img1Games.isDrawingCacheEnabled =true
        binding.img1Games.buildDrawingCache()
        val bitmap = (binding.img1Games.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos)
        val data = baos.toByteArray()
        var uploadTask = imageRoute.putBytes(data)
        if(img_count == 2){
            uploadTask.addOnFailureListener{

            }.addOnSuccessListener { taskSnapshot ->
                imageRoute.downloadUrl.addOnSuccessListener {
                    Log.v("STORAGE","-------->>>"+it)
                    listOfImages.add(it.toString())
                    Log.d(TAG,"El tamanio de lista es: ${listOfImages.size}")
                    uploadGame(id,listOfImages)
                }
            }
        }

        if (img_count==3){
            val imageRoute2 = storageRef.child("games/$id/images/img_2.jpeg")
            binding.img2Games.isDrawingCacheEnabled =true
            binding.img2Games.buildDrawingCache()
            val bitmap2 = (binding.img2Games.drawable as BitmapDrawable).bitmap
            val baos2 = ByteArrayOutputStream()
            bitmap2.compress(Bitmap.CompressFormat.JPEG,50,baos2)
            val data2 = baos2.toByteArray()

            uploadTask.addOnFailureListener{

            }.addOnSuccessListener { taskSnapshot ->
                imageRoute.downloadUrl.addOnSuccessListener {
                    Log.v("STORAGE","-------->>>"+it)
                    listOfImages.add(it.toString())
                    Log.d(TAG,"El tamanio de lista es: ${listOfImages.size}")
                }
            }

            var uploadTask2 = imageRoute2.putBytes(data2)
            uploadTask2.addOnFailureListener{

            }.addOnSuccessListener { taskSnapshot ->
                imageRoute2.downloadUrl.addOnSuccessListener {
                    Log.v("STORAGE","-------->>>"+it)
                    listOfImages.add(it.toString())
                    Log.d(TAG,"El tamanio de lista es: ${listOfImages.size}")
                    uploadGame(id,listOfImages)
                }
            }
        }
        if (img_count==4){
            val imageRoute2 = storageRef.child("games/$id/images/img_2.jpeg")
            binding.img2Games.isDrawingCacheEnabled =true
            binding.img2Games.buildDrawingCache()
            val bitmap2 = (binding.img2Games.drawable as BitmapDrawable).bitmap
            val baos2 = ByteArrayOutputStream()
            bitmap2.compress(Bitmap.CompressFormat.JPEG,50,baos2)
            val data2 = baos2.toByteArray()

            val imageRoute3 = storageRef.child("games/$id/images/img_3.jpeg")
            binding.img3Games.isDrawingCacheEnabled =true
            binding.img3Games.buildDrawingCache()
            val bitmap3 = (binding.img3Games.drawable as BitmapDrawable).bitmap
            val baos3 = ByteArrayOutputStream()
            bitmap3.compress(Bitmap.CompressFormat.JPEG,50,baos3)
            val data3 = baos3.toByteArray()

            uploadTask.addOnFailureListener{

            }.addOnSuccessListener { taskSnapshot ->
                imageRoute.downloadUrl.addOnSuccessListener {
                    Log.v("STORAGE","-------->>>"+it)
                    listOfImages.add(it.toString())
                    Log.d(TAG,"El tamanio de lista es: ${listOfImages.size}")
                }
            }

            var uploadTask2 = imageRoute2.putBytes(data2)
            uploadTask2.addOnFailureListener{

            }.addOnSuccessListener { taskSnapshot ->
                imageRoute2.downloadUrl.addOnSuccessListener {
                    Log.v("STORAGE","-------->>>"+it)
                    listOfImages.add(it.toString())
                    Log.d(TAG,"El tamanio de lista es: ${listOfImages.size}")
                }
            }
            var uploadTask3 = imageRoute3.putBytes(data3)
            uploadTask3.addOnFailureListener{

            }.addOnSuccessListener { taskSnapshot ->
                imageRoute3.downloadUrl.addOnSuccessListener {
                    Log.v("STORAGE","-------->>>"+it)
                    listOfImages.add(it.toString())
                    Log.d(TAG,"El tamanio de lista es: ${listOfImages.size}")
                    uploadGame(id,listOfImages)
                }
            }
        }



    }
    private fun uploadGame(id:String,images:List<String>){
        try{
            val game = Game(
                id = id,
                name = binding.edtxNameGame.text.toString(),
                state = numericalState,
                language = binding.edtxLanguageInput.text.toString(),
                description = binding.edtxDescriptionGame.text.toString(),
                players = binding.edtxPlayersInput.text.toString().toInt(),
                time = binding.edtxTimeInput.text.toString(),
                price = binding.edtxPriceInput.text.toString().toDouble(),
                location = binding.edtxLocationInput.text.toString(),
                images = images )

            db.collection("Games").document(id).set(game)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "successful")
                    Toast.makeText(context,"Se guardo el nuevo juego",Toast.LENGTH_SHORT).show()
                    var goToMainGamePage = AddGameFragmentDirections.actionAddGameFragmentToGamesFragment()
                    findNavController().navigate(goToMainGamePage)

                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }catch (e: Exception){
            Toast.makeText(context,"Datos incompletos, porfavor llenar",Toast.LENGTH_SHORT).show()
        }

    }

}
