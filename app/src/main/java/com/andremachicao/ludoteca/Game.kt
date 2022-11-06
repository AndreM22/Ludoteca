package com.andremachicao.ludoteca

import java.io.Serializable

data class Game ( val Id_game:Int,
                  val Name: String,
                  val State: Double,
                  val Language: String,
                  val Description:String,
                  val Players:Int,
                  val Time: String,
                  val Price: Double,
                  val Location:String,
                  val Image:String):Serializable
