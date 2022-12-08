package com.andremachicao.ludoteca.sharedPreferences

import android.content.Context

class Prefs(val context:Context) {
    val SHARED_ID = "id"
    val SHARED_INIT = "MyUser"
    val SHARED_NAME = "username"
    val SHARED_LASTNAME ="lastname"
    val SHARED_EMAIL ="email"
    val SHARED_STARS ="star"
    val SHARED_IMAGE = "image"
    val storage = context.getSharedPreferences(SHARED_INIT,0)

    fun saveId(id:String){
        storage.edit().putString(SHARED_ID,id).apply()
    }
    fun saveName(name:String){
        storage.edit().putString(SHARED_NAME,name).apply()
    }

    fun saveLastNames(lastnames:String){
        storage.edit().putString(SHARED_LASTNAME,lastnames).apply()
    }

    fun saveEmail(email:String){
        storage.edit().putString(SHARED_EMAIL,email).apply()
    }

    fun saveStar(stars: Double){
        storage.edit().putString(SHARED_STARS,stars.toString()).apply()
    }
    fun saveImage(image: String){
        storage.edit().putString(SHARED_IMAGE,image).apply()
    }

    fun getId():String{
        return storage.getString(SHARED_ID,"")!!
    }
    fun getName():String{
        return storage.getString(SHARED_NAME,"")!!
    }

    fun getLastNames():String{
        return storage.getString(SHARED_LASTNAME,"")!!
    }

    fun getEmail():String{
        return storage.getString(SHARED_EMAIL,"")!!
    }

    fun getStars():String{
        return storage.getString(SHARED_STARS,"")!!
    }

    fun getImage():String{
        return storage.getString(SHARED_IMAGE,"")!!
    }
    fun wipe(){
        storage.edit().clear().apply()
    }

}