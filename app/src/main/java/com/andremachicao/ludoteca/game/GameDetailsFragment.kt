package com.andremachicao.ludoteca.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andremachicao.ludoteca.databinding.FragmentGameDetailsBinding
import com.andremachicao.ludoteca.viewmodel.MainViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GameDetailsFragment : Fragment() {

    private lateinit var binding : FragmentGameDetailsBinding
    private val args: GameDetailsFragmentArgs by navArgs()
    private val db = Firebase.firestore
    private val viewModel by lazy { ViewModelProviders.of(this)[MainViewModel::class.java] }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.gameInfo = args.gameInfo

        binding.btUpdateGame.setOnClickListener {
            Toast.makeText(context,"Entro al on click del update",Toast.LENGTH_SHORT).show()
            val goToUpdatePage = GameDetailsFragmentDirections.actionGameDetailsFragmentToGameUpdateFragment(args.gameInfo)
            findNavController().navigate(goToUpdatePage)
        }

        binding.buttonDeleteGame.setOnClickListener {
            db.collection("Games").document(args.gameInfo.id).delete().addOnSuccessListener {
                val goToMainGameList = GameDetailsFragmentDirections.actionGameDetailsFragmentToGamesFragment()
                Toast.makeText(context,"Se elimino el juego con el id ${args.gameInfo.id}",Toast.LENGTH_SHORT).show()
                findNavController().navigate(goToMainGameList)
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

}