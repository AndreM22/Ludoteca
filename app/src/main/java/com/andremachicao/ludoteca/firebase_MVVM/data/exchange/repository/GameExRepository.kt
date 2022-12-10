package com.andremachicao.ludoteca.firebase_MVVM.data.exchange.repository

import com.andremachicao.ludoteca.firebase_MVVM.data.exchange.model.Exchange

interface GameExRepository {

    fun getExGames(): List<Exchange>
}