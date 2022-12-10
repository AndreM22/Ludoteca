package com.andremachicao.ludoteca.exchange

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andremachicao.ludoteca.firebase_MVVM.data.exchange.model.Exchange
import com.andremachicao.ludoteca.firebase_MVVM.data.exchange.repository.GameExRepository

class ExchangeViewModel(
    val repository: GameExRepository
): ViewModel() {
    private val _gamesEx = MutableLiveData<List<Exchange>>()
    val gameEx:LiveData<List<Exchange>>
        get() = _gamesEx
    

    fun getExGames(){
        _gamesEx.value = repository.getExGames()
    }

}