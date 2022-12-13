package com.andremachicao.ludoteca.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andremachicao.ludoteca.databinding.FragmentExchangeGameDetailsBinding
import com.andremachicao.ludoteca.game.GameDetailsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

@AndroidEntryPoint
class ExchangeDetails:Fragment() {

    private lateinit var binding:FragmentExchangeGameDetailsBinding
    private val args: ExchangeDetailsArgs by navArgs()
    private val list = mutableListOf<CarouselItem>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExchangeGameDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.gameExInfo = args.detailsEx
        for (element in args.detailsEx.gameimages){
            list.add(CarouselItem(element))
        }
        binding.carouselExchangeDetails.addData(list)
        val completeName = "${args.detailsEx.profilenames} ${args.detailsEx.profilelastnames}"
        binding.txNameUserDetails.text = completeName
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val goToPrevious = ExchangeDetailsDirections.actionExchangeDetailsToExchangeMainFragment()
                findNavController().navigate(goToPrevious)
            }
        })
    }

}