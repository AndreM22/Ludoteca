package com.andremachicao.ludoteca.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andremachicao.ludoteca.Game
import com.andremachicao.ludoteca.firebase_imp.data.network.Repo

class MainViewModel: ViewModel() {

    val games = MutableLiveData<List<Game>>(listOf())
    val repo = Repo()
    fun fetchGamesData():LiveData<MutableList<Game>>{
        val mutableData = MutableLiveData<MutableList<Game>>()
        repo.getAllGames().observeForever {
            mutableData.value = it
        }

        return mutableData


    }

}