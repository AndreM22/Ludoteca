package com.andremachicao.ludoteca

import java.io.Serializable

data class Game(val name: String,
                val state: Double,
                val language: String,
                val description:String,
                val players: Int,
                val time: String,
                val price: Double,
                val location:String,
                val image:String): Serializable
