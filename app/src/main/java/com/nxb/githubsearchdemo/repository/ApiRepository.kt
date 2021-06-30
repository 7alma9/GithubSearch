package com.nxb.githubsearchdemo.repository

 import androidx.lifecycle.LiveData
 import com.nxb.githubsearchdemo.data.responses.GithubResponse
 import com.nxb.githubsearchdemo.data.responses.Item
 import com.nxb.githubsearchdemo.other.Resource

interface ApiRepository {

    suspend fun getSearches(query:String,page:String):Resource<GithubResponse>
}