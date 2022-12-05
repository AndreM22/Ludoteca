package com.andremachicao.ludoteca.game

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andremachicao.ludoteca.databinding.FragmentGameUpdateBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GameUpdateFragment:Fragment() {

    private lateinit var binding: FragmentGameUpdateBinding
    private val args: GameUpdateFragmentArgs by navArgs()
    private val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameUpdateBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.edtxNameGameUpdate.setText(args.gameInfo.name)
        binding.edtxGameImageUpdate.setText("Perro")
        binding.edtxStateGameUpdate.setText(args.gameInfo.state.toString())
        binding.edtxLanguageInputUpdate.setText(args.gameInfo.language)
        binding.edtxDescriptionGameUpdate.setText(args.gameInfo.description)
        binding.edtxPlayersInputUpdate.setText(args.gameInfo.players.toString())
        binding.edtxTimeInputUpdate.setText(args.gameInfo.time)
        binding.edtxPriceInputUpdate.setText(args.gameInfo.price.toString())
        binding.edtxLocationInputUpdate.setText(args.gameInfo.location)

        binding.btAcceptUpdate.setOnClickListener {
            try{
                val gameMap = mapOf(
                    "id" to args.gameInfo.id,
                    "name" to binding.edtxNameGameUpdate.text.toString(),
                    "state" to binding.edtxStateGameUpdate.text.toString().toDouble(),
                    "language" to binding.edtxLanguageInputUpdate.text.toString(),
                    "description" to binding.edtxDescriptionGameUpdate.text.toString(),
                    "players" to binding.edtxPlayersInputUpdate.text.toString().toInt(),
                    "time" to binding.edtxTimeInputUpdate.text.toString(),
                    "price" to binding.edtxPriceInputUpdate.text.toString().toDouble(),
                    "location" to binding.edtxLocationInputUpdate.text.toString(),
                    "images" to listOf<String>("Perro,Gato")
                )

                db.collection("Games").document(args.gameInfo.id).update(gameMap)
                    .addOnSuccessListener { documentReference ->
                        Log.d(ContentValues.TAG, "successful")
                        Toast.makeText(context,"Se Actualizo el juego", Toast.LENGTH_SHORT).show()
                        var goToMainGamePage = GameUpdateFragmentDirections.actionGameUpdateFragmentToGamesFragment()
                        findNavController().navigate(goToMainGamePage)

                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding document", e)
                    }
            }catch (e: Exception){
                Toast.makeText(context,"Datos incompletos, porfavor llenar", Toast.LENGTH_SHORT).show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val goToPrevious = GameUpdateFragmentDirections.actionGameUpdateFragmentToGameDetailsFragment(args.gameInfo)
                findNavController().navigate(goToPrevious)
            }
        })
    }


}