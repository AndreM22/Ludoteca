package com.andremachicao.ludoteca.firebase_MVVM.data.exchange.repository

import com.andremachicao.ludoteca.firebase_MVVM.data.exchange.model.Exchange
import com.andremachicao.ludoteca.utils.UiState

interface GameExRepository {

    fun getExGames(result:(UiState<List<Exchange>>)->Unit)
    fun addExchange(exchange: Exchange,result:(UiState<String>) ->Unit)
}