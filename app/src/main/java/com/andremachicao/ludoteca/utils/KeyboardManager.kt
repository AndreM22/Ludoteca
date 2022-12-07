package com.andremachicao.ludoteca.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


    fun View.hideKeyboard(){
        val inm = context.getSystemService(Context.INPUT_METHOD_SERVICE)as InputMethodManager
        inm.hideSoftInputFromWindow(windowToken,0)
    }
