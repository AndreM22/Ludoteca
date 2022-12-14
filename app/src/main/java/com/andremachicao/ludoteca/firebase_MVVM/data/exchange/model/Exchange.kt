package com.andremachicao.ludoteca.firebase_MVVM.data.exchange.model

import java.io.Serializable

data class Exchange(
    val id:String ="",
    val exchangetype:String ="",
    val gameid: String ="",
    val gamename: String = "",
    val gamestate: Double = 0.00,
    val gamelanguage: String = "",
    val gamedescription:String = "",
    val gameplayers: Int = 0,
    val gametime: String= "",
    val gameprice: Double = 0.00,
    val gamelocation:String = "",
    val gameimages:List<String> = listOf(),
    val profileid: String = "",
    val profilenames: String= "",
    val profilelastnames: String = "",
    val profileemail: String= "",
    val profileimage: String = "",
    val stars: Double = 0.00,


): Serializable