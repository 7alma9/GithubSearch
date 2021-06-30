package com.nxb.githubsearchdemo

import java.io.InputStreamReader

/**
@author Salman Aziz
created on 6/23/21
 **/

class TestMockResponseFileReader(path: String) {

    val content: String

    init {
        val reader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(path))
        content = reader.readText()
        reader.close()
    }
}