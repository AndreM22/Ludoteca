package com.andremachicao.ludoteca.exchange

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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
import com.andremachicao.ludoteca.utils.UiState
import com.andremachicao.ludoteca.utils.hide
import com.andremachicao.ludoteca.utils.show
import com.andremachicao.ludoteca.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangeMainFragment: Fragment() {

    private lateinit var binding: FragmentExchangeGamesBinding
    private val listOfExchangesAdapter = ExchangeListAdapter()
    private val exchangeViewModel: ExchangeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExchangeGamesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvExchangeList.adapter = listOfExchangesAdapter
        val layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.rvExchangeList.layoutManager =layoutManager
        val decorationItem= DividerItemDecoration(context,layoutManager.orientation)
        binding.rvExchangeList.addItemDecoration(decorationItem)
        exchangeViewModel.getExGames()
        exchangeViewModel.gameEx.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading ->{
                    binding.progressBarExchangeMain.show()
                }
                is UiState.Failure ->{
                    binding.progressBarExchangeMain.hide()
                    toast(state.error)
                }
                is UiState.Success ->{
                    binding.progressBarExchangeMain.hide()
                    listOfExchangesAdapter.addAll(state.data.toMutableList())

                }
            }
        }

        listOfExchangesAdapter.setOnExchangeClickListener {

            val goToShowDetails = ExchangeMainFragmentDirections.actionExchangeMainFragmentToExchangeDetails(it)
            findNavController().navigate(goToShowDetails)
        }
        binding.btAddExchange.setOnClickListener {
            val goToGamesPage = ExchangeMainFragmentDirections.actionExchangeMainFragmentToGamesFragment()
            findNavController().navigate(goToGamesPage)
        }
        binding.cbOnlyExchange.setOnClickListener {
            if(binding.cbOnlyExchange.isChecked){
                binding.cbOnlySale.isChecked = false
                toast("Solo Intercambio")
                exchangeViewModel.filterExGames("Solo intercambio")
            }else{
                exchangeViewModel.getExGames()
            }

        }
        binding.cbOnlySale.setOnClickListener {
            if(binding.cbOnlySale.isChecked){
                binding.cbOnlyExchange.isChecked = false
                toast("Solo Venta")
                exchangeViewModel.filterExGames("Solo venta")
            }else{
                exchangeViewModel.getExGames()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }

}