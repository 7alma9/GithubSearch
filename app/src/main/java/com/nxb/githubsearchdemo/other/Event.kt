package com.nxb.githubsearchdemo.other

/**
@author Salman Aziz
created on 6/22/21
 **/

open class Event<out T>(private val content:T) {

    var hasbeenHandeled=false
    private set

    fun getContentIfNotHandeled():T?{
        return if(hasbeenHandeled){
              null
        }else{
            hasbeenHandeled=true
            content
        }
    }

    fun peakContent()=content
}