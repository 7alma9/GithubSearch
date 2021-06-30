package com.nxb.githubsearchdemo.util

import com.nxb.githubsearchdemo.data.responses.Item
import com.nxb.githubsearchdemo.data.responses.Owner

/**
@author Salman Aziz
created on 6/24/21
 **/

object TestUtils {


    fun createRepo(  name:String, htmlUrl:String,description:String,avatarUrl:String )=Item(
        htmlUrl=htmlUrl,
        name=name,
        description=description,
        owner = Owner(avatarUrl)
    )
}