package com.andremachicao.ludoteca.game

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andremachicao.ludoteca.R
import com.andremachicao.ludoteca.databinding.FragmentGameDetailsBinding
import com.andremachicao.ludoteca.exchange.ExchangeViewModel
import com.andremachicao.ludoteca.firebase_MVVM.data.exchange.model.Exchange
import com.andremachicao.ludoteca.sharedPreferences.InitApp.Companion.prefs
import com.andremachicao.ludoteca.utils.*
import com.andremachicao.ludoteca.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import java.util.*

@AndroidEntryPoint
class GameDetailsFragment : Fragment() {

    private lateinit var binding : FragmentGameDetailsBinding
    private val args: GameDetailsFragmentArgs by navArgs()
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private lateinit var auth: FirebaseAuth
    private val list = mutableListOf<CarouselItem>()
    private val exchangeViewModel: ExchangeViewModel by viewModels()
    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        auth = Firebase.auth
        binding.gameInfo = args.gameInfo
        //Progress dialog
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Porfavor espere")
        progressDialog.setMessage("Registrando...")
        progressDialog.setCanceledOnTouchOutside(false)

        if(args.gameInfo.exchange){
            binding.spinnerTypeExchangeDetailsGame.hide()
            binding.btExchangeDetailsGamePage.hide()
            binding.txExchangedGame.show()
            binding.btUnxchangeDetailsGamePage.show()
        }
        //Opciones de intercambio
        var exchangeType = "Seleccione un tipo"
        val listExchangeTypes = arrayOf("Seleccione un tipo","Ambos","Solo intercambio","Solo venta")
        var spinnerAdapter:ArrayAdapter<String> = ArrayAdapter(view.context, R.layout.spinner_element_config,listExchangeTypes)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_elemet_list_config)
        binding.spinnerTypeExchangeDetailsGame.adapter = spinnerAdapter
        binding.spinnerTypeExchangeDetailsGame.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                exchangeType = binding.spinnerTypeExchangeDetailsGame.selectedItem.toString()
                p1?.hideKeyboard()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                exchangeType = "Seleccione un tipo"
                p0?.hideKeyboard()
            }

        }

        for (element in args.gameInfo.images){
            list.add(CarouselItem(element))
        }
        //list.add(CarouselItem(args.gameInfo.image))
        //list.add(CarouselItem("https://images.drive.com.au/driveau/image/private/c_fill,f_auto,g_auto,h_674,q_auto:eco,w_1200/ca-s3/2013/03/Suzuki-SX4-5-625x404.jpg"))
        //list.add(CarouselItem("https://www.km77.com/media/fotos/suzuki_sx4_2010_3480_1.jpg"))
        binding.carousel.addData(list)

        binding.btUpdateGame.setOnClickListener {
            val goToUpdatePage = GameDetailsFragmentDirections.actionGameDetailsFragmentToGameUpdateFragment(args.gameInfo)
            findNavController().navigate(goToUpdatePage)
        }

        binding.buttonDeleteGame.setOnClickListener {
            auth.currentUser?.email?.let { email->
                db.collection("users").document(email).collection("Games").document(args.gameInfo.id).delete().addOnSuccessListener {
                    val goToMainGameList = GameDetailsFragmentDirections.actionGameDetailsFragmentToGamesFragment()
                    val storageRef = storage.reference
                    val desertRef1 = storageRef.child("$email/games/${args.gameInfo.id}/images/img_1.jpeg")
                    val desertRef2 = storageRef.child("$email/games/${args.gameInfo.id}/images/img_2.jpeg")
                    val desertRef3 = storageRef.child("$email/games/${args.gameInfo.id}/images/img_3.jpeg")
                    desertRef1.delete().addOnSuccessListener {
                    }.addOnFailureListener{ e ->
                        Log.d(TAG,"El error es: $e")
                    }
                    desertRef2.delete().addOnSuccessListener {
                    }.addOnFailureListener{ e ->
                        Log.d(TAG,"El error es: $e")
                    }
                    desertRef3.delete().addOnSuccessListener {
                    }.addOnFailureListener{ e ->
                        Log.d(TAG,"El error es: $e")
                    }
                    deleteExchange()
                    Toast.makeText(context,"Se elimino el juego",Toast.LENGTH_SHORT).show()

                    findNavController().navigate(goToMainGameList)
                }
            }

        }
        binding.btUnxchangeDetailsGamePage.setOnClickListener {
            deleteExchange()
            val refGame = db.collection("users").document(prefs.getEmail()).collection("Games").document(args.gameInfo.id)
            refGame.update("exchange",false).addOnSuccessListener {
                binding.spinnerTypeExchangeDetailsGame.show()
                binding.btExchangeDetailsGamePage.show()
                binding.txExchangedGame.hide()
                binding.btUnxchangeDetailsGamePage.hide()
                toast("Se elimino el juego del intercambio")
                val goToMainpage = GameDetailsFragmentDirections.actionGameDetailsFragmentToGamesFragment()
                findNavController().navigate(goToMainpage)
            }

        }
        binding.btExchangeDetailsGamePage.setOnClickListener {
            if(exchangeType == "Seleccione un tipo"){
                toast("Porfavor seleccione un intercambio")
                return@setOnClickListener
            }else{
                val id = UUID.randomUUID().toString()
                exchangeViewModel.addExchange(
                    Exchange(
                        id = id,
                        exchangetype = exchangeType,
                        gameid = args.gameInfo.id,
                        gamename = args.gameInfo.name,
                        gamestate = args.gameInfo.state,
                        gamelanguage = args.gameInfo.language,
                        gamedescription = args.gameInfo.description,
                        gameplayers = args.gameInfo.players,
                        gametime = args.gameInfo.time,
                        gameprice = args.gameInfo.price,
                        gamelocation = args.gameInfo.location,
                        gameimages = args.gameInfo.images,
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
        exchangeViewModel.addGamesEx.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading ->{
                    progressDialog.show()
                }
                is UiState.Failure ->{
                    progressDialog.dismiss()
                    toast(state.error)
                }
                is UiState.Success ->{
                    val refGame = db.collection("users").document(prefs.getEmail()).collection("Games").document(args.gameInfo.id)
                    refGame.update("exchange",true).addOnSuccessListener {
                        progressDialog.dismiss()
                        toast("Se puso en intercambio el juego")
                        val goToMainpage = GameDetailsFragmentDirections.actionGameDetailsFragmentToGamesFragment()
                        findNavController().navigate(goToMainpage)
                    }.addOnFailureListener{
                        toast("No se pudo registrar el intercambio")
                    }


                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val goToPrevious = GameDetailsFragmentDirections.actionGameDetailsFragmentToGamesFragment()
                findNavController().navigate(goToPrevious)
            }
        })

    }
    private fun deleteExchange(){
        db.collection("exchange")
            .whereEqualTo("gameid",args.gameInfo.id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    exchangeViewModel.deleteExchangeGameFun(document.toObject(Exchange::class.java))
                }

            }
    }

}