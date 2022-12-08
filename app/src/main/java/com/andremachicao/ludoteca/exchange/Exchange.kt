package com.andremachicao.ludoteca.exchange

import java.io.Serializable

data class Exchange(
    val id:String,
    var gameid: String,
    val gamename: String,
    val gamestate: Double,
    val gamelanguage: String,
    val gamedescription:String,
    val gameplayers: Int,
    val gametime: String,
    val gameprice: Double,
    val gamelocation:String,
    val gameimages:List<String>,
    val profileid: String,
    val profilenames: String,
    val profilelastnames: String,
    val profileemail: String,
    val profileimage: String,
    val stars: Double,


): Serializable