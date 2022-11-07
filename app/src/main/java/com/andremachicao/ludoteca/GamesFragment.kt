package com.andremachicao.ludoteca

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.andremachicao.ludoteca.databinding.FragmentGamesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GamesFragment: Fragment() {
    private lateinit var binding: FragmentGamesBinding
    private val listOfGamesAdapter = GameListAdapter()
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGamesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvGameList.adapter =listOfGamesAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        binding.rvGameList.layoutManager=layoutManager
        LinearSnapHelper().attachToRecyclerView(binding.rvGameList)
        showData()
        binding.btAddGame.setOnClickListener {
            val goToAddGamePage = GamesFragmentDirections.actionGamesFragmentToAddGameFragment()
            findNavController().navigate(goToAddGamePage)
        }

    }
    private fun showData(){
        val  games: MutableList<Game> = mutableListOf()
        db.collection("Games").get()
            .addOnSuccessListener { result ->
                for (document in result){
                    Log.d(TAG,"${document.id} => ${document.data}")
                    print(document.data)
                    games.add(Game(
                        name = document.data["name"] as String,
                        state = document.data["state"] as Double,
                        language = document.data["language"] as String,
                        description = document.data["description"] as String,
                        players = (document.data["players"] as Long).toInt(),
                        time = document.data["time"] as String,
                        price = document.data["price"] as Double,
                        location = document.data["location"] as String,
                        image = document.data["image"] as String
                    ))
                    listOfGamesAdapter.addAll(games)
                }
            }
            .addOnFailureListener{exception ->
                Log.d(TAG,"Error getting documents: ",exception)
            }
    }
}