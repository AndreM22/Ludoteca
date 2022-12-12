package com.andremachicao.ludoteca.firebase_MVVM.data.exchange.repository

import com.andremachicao.ludoteca.firebase_MVVM.data.exchange.model.Exchange
import com.andremachicao.ludoteca.utils.FireStoreTables
import com.andremachicao.ludoteca.utils.UiState
import com.google.firebase.firestore.FirebaseFirestore

class GameExRepositoryImp(
    val database:FirebaseFirestore
): GameExRepository {
    override fun getExGames(result: (UiState<List<Exchange>>) -> Unit) {
        database.collection(FireStoreTables.EXCHANGE)
            .get()
            .addOnSuccessListener {
                val listExchanges = arrayListOf<Exchange>()
                for (document in it){
                    val exchange = document.toObject(Exchange::class.java)
                    /*
                    val exchange = Exchange(
                        id = document.data["id"] as String,
                        exchangetype = document.data["exchangetype"] as String,
                        gameid = document.data["gameid"] as String,
                        gamename = document.data["gamename"] as String,
                        gamestate = document.data["gamestate"] as Double,
                        gamelanguage = document.data["gamelanguage"] as String,
                        gamedescription = document.data["gamedescription"] as String,
                        gameplayers = (document.data["gameplayers"] as Long).toInt(),
                        gametime = document.data["gametime"] as String,
                        gameprice = document.data["gameprice"] as Double,
                        gamelocation = document.data["gamelocation"] as String,
                        gameimages = document.data["gameimages"] as List<String>,
                        profileid = document.data["profileid"] as String,
                        profilenames = document.data["profilenames"] as String,
                        profilelastnames = document.data["profilelastnames"] as String,
                        profileemail = document.data["profileemail"] as String,
                        profileimage = document.data["profileimage"] as String,
                        stars = document.data["stars"] as Double
                    )

                     */
                    listExchanges.add(exchange)
                }
                result.invoke(
                    UiState.Success(listExchanges)
                )
            }
            .addOnFailureListener{
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun addExchange(exchange: Exchange, result: (UiState<String>) -> Unit) {
        database.collection(FireStoreTables.EXCHANGE)
            .document(exchange.id)
            .set(exchange)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(exchange.id)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }
}