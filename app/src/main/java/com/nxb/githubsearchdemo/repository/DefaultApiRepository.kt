package com.nxb.githubsearchdemo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.nxb.githubsearchdemo.OpenForTesting
import com.nxb.githubsearchdemo.data.remote.RepoApi
import com.nxb.githubsearchdemo.data.responses.GithubResponse
import com.nxb.githubsearchdemo.data.responses.Item
import com.nxb.githubsearchdemo.other.Resource
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

/**
@author Salman Aziz
created on 6/22/21
 **/

 class DefaultApiRepository @Inject constructor(val api: RepoApi) : ApiRepository {

    override suspend fun getSearches(query: String,page :String): Resource<GithubResponse> {
        val response = api.getSearches(query,page = page)
        return try {
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Something Went Wrong body is null", null)
            } else {
                Resource.error("Something Went Wrong response failed", null)

            }
        } catch (e: Exception) {
            Resource.error(e.localizedMessage?:"Something went wrong", null)

        }
    }
}