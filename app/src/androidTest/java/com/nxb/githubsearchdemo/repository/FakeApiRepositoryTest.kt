package com.nxb.githubsearchdemo.repository

import com.nxb.githubsearchdemo.data.responses.GithubResponse
import com.nxb.githubsearchdemo.other.Event
import com.nxb.githubsearchdemo.other.Resource
import com.nxb.githubsearchdemo.other.Status
import com.nxb.githubsearchdemo.util.TestUtilss
import org.junit.Assert.*

class FakeApiRepositoryTest:ApiRepository{
    override suspend fun getSearches(query: String, page: String): Resource<GithubResponse> {
        val repo1 = com.nxb.githubsearchdemo.TestUtilss.createRepo("phonegap-start", "https://github.com/phonegap/phonegap-start", "PhoneGap Hello World app", "https://avatars.githubusercontent.com/u/60365?v=4")
        val repo2 = com.nxb.githubsearchdemo.TestUtilss.createRepo("HelloGitHub", "https://github.com/521xueweihan/HelloGitHub", ":octocat: 分享 GitHub 上有趣、入门级的开源项目", "https://avatars.githubusercontent.com/u/8255800?v=4")
        val githubResponse = GithubResponse(false, listOf(repo1, repo2,repo2,repo2,repo2,repo2, repo2,repo2,repo2,repo2,repo2, repo2,repo2,repo2,repo2,repo2, repo2,repo2,repo2,repo2,repo2, repo2,repo2,repo2,repo2,repo2), 2128713)

        return Resource(Status.SUCCESS, githubResponse, null)
    }

}