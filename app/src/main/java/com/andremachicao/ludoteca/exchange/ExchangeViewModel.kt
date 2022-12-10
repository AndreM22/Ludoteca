package com.andremachicao.ludoteca.exchange

import android.os.Looper
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
    

    fun getExGames(){
        _gamesEx.value = UiState.Loading
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            _gamesEx.value = repository.getExGames()
        },2000)

    }

}