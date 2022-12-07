package com.andremachicao.ludoteca.profile

import java.io.Serializable

data class Profile( val names: String,
                    val lastnames: String,
                    val email: String,
                    val starts: Double): Serializable
