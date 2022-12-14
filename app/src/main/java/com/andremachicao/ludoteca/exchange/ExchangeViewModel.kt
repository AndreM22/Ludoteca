package com.andremachicao.ludoteca.exchange

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andremachicao.ludoteca.firebase_MVVM.data.exchange.model.Exchange
import com.andremachicao.ludoteca.firebase_MVVM.data.exchange.repository.GameExRepository
import com.andremachicao.ludoteca.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    val repository: GameExRepository
): ViewModel() {
    private val _gamesEx = MutableLiveData<UiState<List<Exchange>>>()
    val gameEx:LiveData<UiState<List<Exchange>>>
        get() = _gamesEx

    private val _addGamesEx = MutableLiveData<UiState<String>>()
    val addGamesEx:LiveData<UiState<String>>
        get() = _addGamesEx

    private val _updateGameEx =MutableLiveData<UiState<String>>()
    val updateGameEx:LiveData<UiState<String>>
        get() =_updateGameEx

    private val _deleteGameEx =MutableLiveData<UiState<String>>()
    val deleteGameEx:LiveData<UiState<String>>
        get() =_deleteGameEx

    fun getExGames(){
        _gamesEx.value = UiState.Loading
        repository.getExGames { _gamesEx.value = it }

    }

    fun addExchange(exchange: Exchange){
        _addGamesEx.value = UiState.Loading
        repository.addExchange(exchange){
            _addGamesEx.value = it
        }
    }

    fun updateGameExchange(exchange: Exchange){
        _updateGameEx.value = UiState.Loading
        repository.updateExchange(exchange){
            _updateGameEx.value = it}
    }

    fun deleteExchangeGameFun(exchange: Exchange){
        _deleteGameEx.value = UiState.Loading
        repository.deleteExchange(exchange.id){
            _deleteGameEx.value = it
        }
    }

    fun filterExGames(typeExchange: String){
        _gamesEx.value = UiState.Loading
        repository.someExGames(typeExchange) { _gamesEx.value = it }
    }

}