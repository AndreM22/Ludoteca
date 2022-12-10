package com.andremachicao.ludoteca.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andremachicao.ludoteca.databinding.FragmentExchangeGameDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangeDetails:Fragment() {

    private lateinit var binding:FragmentExchangeGameDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExchangeGameDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}