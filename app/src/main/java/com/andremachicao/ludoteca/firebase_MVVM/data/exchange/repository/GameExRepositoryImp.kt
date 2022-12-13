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

    override fun someExGames(typeExchange: String, result: (UiState<List<Exchange>>) -> Unit) {
        database.collection(FireStoreTables.EXCHANGE)
            .whereEqualTo("exchangetype",typeExchange)
            .get()
            .addOnSuccessListener {
                val listExchanges = arrayListOf<Exchange>()
                for (document in it){
                    val exchange = document.toObject(Exchange::class.java)
                    listExchanges.add(exchange)
                }
                result.invoke(
                    UiState.Success(listExchanges)
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

    override fun updateExchange(exchange: Exchange, result: (UiState<String>) -> Unit) {
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

    override fun deleteExchange(id: String, result: (UiState<String>) -> Unit) {
        database.collection(FireStoreTables.EXCHANGE)
            .document(id)
            .delete()
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(id)
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