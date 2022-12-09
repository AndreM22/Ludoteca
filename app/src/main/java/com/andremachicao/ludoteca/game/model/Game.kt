package com.andremachicao.ludoteca.game.model

import java.io.Serializable

data class Game(
    var id: String,
    val name: String,
    val state: Double,
    val language: String,
    val description:String,
    val players: Int,
    val time: String,
    val price: Double,
    val location:String,
    val images:List<String>,
    val exchange: Boolean ): Serializable
