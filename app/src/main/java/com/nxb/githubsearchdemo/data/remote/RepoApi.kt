package com.nxb.githubsearchdemo.data.remote

 import androidx.lifecycle.LiveData
 import com.nxb.githubsearchdemo.BuildConfig
 import com.nxb.githubsearchdemo.OpenForTesting
 import com.nxb.githubsearchdemo.data.responses.GithubResponse
 import retrofit2.Response
 import retrofit2.http.GET
 import retrofit2.http.Query

/**
@author Salman Aziz
created on 6/22/21
 **/
@OpenForTesting
interface RepoApi {
    @GET(BuildConfig.SEARCH_ROUTE)
      suspend fun getSearches(@Query("q") query: String,@Query("page") page: String,@Query("per_page") perPage: String="10"):Response<GithubResponse>
}