package com.andremachicao.ludoteca.firebase_MVVM.data.exchange.repository

import com.andremachicao.ludoteca.firebase_MVVM.data.exchange.model.Exchange
import com.andremachicao.ludoteca.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore

class GameExRepositoryImp(
    val database:FirebaseFirestore
): GameExRepository {
    override fun getExGames(): UiState<List<Exchange>> {
        val data = arrayListOf(
            Exchange(
                id = "sdfsdf",
                exchangetype = "sdf",
                gameid = "sdf",
                gamename = "sdfsdf",
                gamestate = 45.22,
                gamelanguage = "dfgew",
                gamedescription = "sdsdf",
                gameplayers = 4,
                gametime ="12/05/22",
                gameprice = 45.25,
                gamelocation = "Si",
                gameimages = listOf(),
                profileid = "sdfsdf",
                profilenames = "sdfsdf",
                profilelastnames = "rq tqerg",
                profileemail = "sdfsdfsd",
                profileimage = "sdfsdf",
                stars = 5.00

            )
        )
        if(data.isNullOrEmpty()){
            return UiState.Failure("Data is Empty")
        }else{
            return UiState.Success(data)
        }
    }
}