package com.andremachicao.ludoteca.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andremachicao.ludoteca.databinding.FragmentExchangeGamesBinding
import com.andremachicao.ludoteca.game.GamesFragmentDirections

class ExchangeMainFragment: Fragment() {

    private lateinit var binding: FragmentExchangeGamesBinding
    private val listOfExchangesAdapter = ExchangeListAdapter()
    private val exchangeViewModel: ExchangeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExchangeGamesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvExchangeList.adapter = listOfExchangesAdapter
        val layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.rvExchangeList.layoutManager =layoutManager
        val decorationItem= DividerItemDecoration(context,layoutManager.orientation)
        binding.rvExchangeList.addItemDecoration(decorationItem)
        //viewModel.fetchGamesData().observe(viewLifecycleOwner, {
        //    listOfGamesAdapter.addAll(it)
        //})
        exchangeViewModel.getExGames()
        exchangeViewModel.gameEx.observe(viewLifecycleOwner){

        }

        listOfExchangesAdapter.setOnExchangeClickListener {
            Toast.makeText(context,"El nombre del juego es: ${it.gamename}",Toast.LENGTH_SHORT).show()
            //val goToShowDetails = GamesFragmentDirections.actionGamesFragmentToGameDetailsFragment(it)
            //findNavController().navigate(goToShowDetails)
        }
        binding.btAddExchange.setOnClickListener {
            val goToGamesPage = ExchangeMainFragmentDirections.actionExchangeMainFragmentToGamesFragment()
            findNavController().navigate(goToGamesPage)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }

}