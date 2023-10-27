package com.example.temi_beta.api



import android.util.Log
import com.beust.klaxon.Klaxon
import fuel.HttpResponse
import fuel.httpGet

data class MenusInterface (
    val menu: List<List<Any>>
)




suspend fun getMenus(): List<List<Any>>? {
    val response:HttpResponse = "https://fastapideta-1-v4049125.deta.app/".httpGet()
    return if(response.statusCode == 200){
        Log.d("Api","getmenu is complete")
        Klaxon().parse<MenusInterface>(response.body)?.menu
    }else{
        Log.e("Api","getmenu is error")
        null
    }
}
