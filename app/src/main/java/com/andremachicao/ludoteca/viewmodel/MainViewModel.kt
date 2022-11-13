package com.andremachicao.ludoteca.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andremachicao.ludoteca.game.model.Game
import com.andremachicao.ludoteca.firebase_imp.data.network.Repo

class MainViewModel: ViewModel() {

    val repo = Repo()
    fun fetchGamesData():LiveData<MutableList<Game>>{
        val mutableData = MutableLiveData<MutableList<Game>>()
        repo.getAllGames().observeForever { gamesList ->
            mutableData.value = gamesList
        }

        return mutableData

    }

    fun deleteGame(id:String){
        repo.deleteGame(id)
    }

}