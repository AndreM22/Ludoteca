package com.andremachicao.ludoteca

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.andremachicao.ludoteca.databinding.FragmentGamesBinding

class GamesFragment: Fragment() {
    private lateinit var binding: FragmentGamesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGamesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btAddGame.setOnClickListener {
            val goToAddGamePage = GamesFragmentDirections.actionGamesFragmentToAddGameFragment()
            findNavController().navigate(goToAddGamePage)
        }
    }
}