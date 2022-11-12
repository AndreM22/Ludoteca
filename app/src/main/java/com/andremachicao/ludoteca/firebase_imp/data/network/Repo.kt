package com.andremachicao.ludoteca.firebase_imp.data.network

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andremachicao.ludoteca.game.model.Game
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Repo {
    private val db = Firebase.firestore
    fun getAllGames(): LiveData<MutableList<Game>>{
        val mutableData = MutableLiveData<MutableList<Game>>()
        db.collection("Games").get().addOnSuccessListener {result ->
            val listData = mutableListOf<Game>()
            for (document in result){
                Log.d(TAG, "El id es: ${document.data["id"]}")
                val game = Game(
                    id = document.data["id"] as String,
                    name = document.data["name"] as String,
                    state = document.data["state"] as Double,
                    language = document.data["language"] as String,
                    description = document.data["description"] as String,
                    players = (document.data["players"] as Long).toInt(),
                    time = document.data["time"] as String,
                    price = document.data["price"] as Double,
                    location = document.data["location"] as String,
                    image = document.data["image"] as String)
                listData.add(game)
            }
            mutableData.value = listData
        }
        return mutableData

    }

    fun deleteGame(id:String){
        db.collection("Games").document(id).delete()
    }
}