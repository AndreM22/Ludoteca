package com.andremachicao.ludoteca

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.andremachicao.ludoteca.databinding.FragmentGamesBinding
import com.andremachicao.ludoteca.viewmodel.MainViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GamesFragment: Fragment() {
    private lateinit var binding: FragmentGamesBinding
    private val listOfGamesAdapter = GameListAdapter()
    private val viewModel by lazy { ViewModelProviders.of(this)[MainViewModel::class.java] }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGamesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvGameList.adapter = listOfGamesAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        binding.rvGameList.layoutManager = layoutManager
        val decorationItem = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvGameList.addItemDecoration(decorationItem)
        //LinearSnapHelper().attachToRecyclerView(binding.rvGameList)
        viewModel.fetchGamesData().observe(viewLifecycleOwner, {
            listOfGamesAdapter.addAll(it)
            Toast.makeText(context,"${listOfGamesAdapter.itemCount}",Toast.LENGTH_SHORT).show()
        })

        listOfGamesAdapter.setOnGameClickListener {
            Toast.makeText(context,"Se toca el juego ${it.name}",Toast.LENGTH_SHORT).show()
            val goToShowDetails = GamesFragmentDirections.actionGamesFragmentToGameDetailsFragment(it)
            findNavController().navigate(goToShowDetails)
        }



        binding.btAddGame.setOnClickListener {
            val goToAddGamePage = GamesFragmentDirections.actionGamesFragmentToAddGameFragment()
            findNavController().navigate(goToAddGamePage)
        }

    }

}