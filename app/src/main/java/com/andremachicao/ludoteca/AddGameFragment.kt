package com.andremachicao.ludoteca

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.andremachicao.ludoteca.databinding.FragmentAddGameBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btAddGameAccept.setOnClickListener {
            var id = UUID.randomUUID().toString()
            try{
                var game = Game(
                    Name = binding.edtxNameGame.text.toString(),
                    State = binding.edtxStateGame.text.toString().toDouble(),
                    Language = binding.edtxLanguageInput.text.toString(),
                    Description = binding.edtxDescriptionGame.text.toString(),
                    Players = binding.edtxPlayersInput.text.toString().toInt(),
                    Time = binding.edtxTimeInput.text.toString(),
                    Price = binding.edtxPriceInput.text.toString().toDouble(),
                    Location = binding.edtxLocationInput.text.toString(),
                    Image = binding.edtxGameImage.text.toString() )

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

}
