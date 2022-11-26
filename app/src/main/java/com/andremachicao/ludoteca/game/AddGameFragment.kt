package com.andremachicao.ludoteca.game

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ScrollView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.andremachicao.ludoteca.databinding.FragmentAddGameBinding
import com.andremachicao.ludoteca.game.model.Game
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.android.bind
import java.util.*

class AddGameFragment: Fragment(){
    private lateinit var binding: FragmentAddGameBinding
    private val db = Firebase.firestore
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
        var numericalState:Double = 0.00
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
            binding.idDatePickerItem.visibility= View.VISIBLE
            binding.btAddGameAccept.visibility= View.GONE
        }


        binding.idDatePickerItem.setOnDateChangedListener{
            date,year,month,day ->
            binding.edtxTimeInput.setText(getDateFromDatePicker())
            binding.idDatePickerItem.visibility= View.GONE
            binding.btAddGameAccept.visibility= View.VISIBLE
        }
        ///////////////////////////////////////////////////////////
        binding.btIncreasePlayers.setOnClickListener{
            var players = 0
            if(binding.edtxPlayersInput.text.toString() == "") {
                players = 1
                binding.edtxPlayersInput.setText(players.toString())
                Toast.makeText(
                    context,
                    "Bueno",
                    Toast.LENGTH_SHORT
                ).show()
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

            val id = UUID.randomUUID().toString()
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
                    image = binding.edtxGameImage.text.toString() )

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

    fun getDateFromDatePicker(): String {
        val day = binding.idDatePickerItem.dayOfMonth.toString().padStart(2,'0')
        val month = (binding.idDatePickerItem.month+1).toString().padStart(2,'0')
        val year= binding.idDatePickerItem.year.toString().padStart(4,'0')
        return day+"/"+month+"/"+year
    }




}
