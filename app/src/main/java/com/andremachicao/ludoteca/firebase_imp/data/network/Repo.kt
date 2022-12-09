package com.andremachicao.ludoteca.firebase_imp.data.network

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andremachicao.ludoteca.game.model.Game
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Repo {
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    fun getAllGames(): LiveData<MutableList<Game>>{
        auth = Firebase.auth
        val mutableData = MutableLiveData<MutableList<Game>>()
        auth.currentUser?.email?.let { email ->
            db.collection("users").document(email).collection("Games").get().addOnSuccessListener { result ->
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
                        images = document.data["images"] as List<String>,
                        exchange = document.data["exchange"] as Boolean)
                    listData.add(game)
                }
                mutableData.value = listData
            }
        }
        return mutableData

    }

    fun deleteGame(id:String){
        db.collection("Games").document(id).delete()
    }
}