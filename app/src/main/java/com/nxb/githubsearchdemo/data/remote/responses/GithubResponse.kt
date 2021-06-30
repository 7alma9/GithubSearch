package com.nxb.githubsearchdemo.data.responses

import com.google.gson.annotations.SerializedName
import com.nxb.githubsearchdemo.OpenForTesting


 data class GithubResponse(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("total_count")
    val totalCount: Int
)


 data class Item(

    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,
    @SerializedName("owner")
    val owner: Owner

)


 data class Owner(
    @SerializedName("avatar_url")
    val avatarUrl: String
)