package com.nxb.githubsearchdemo.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.android.example.github.util.LiveDataCallAdapterFactory
import com.google.common.truth.Truth.assertThat
import com.nxb.githubsearchdemo.MainCoroutineRule
import com.nxb.githubsearchdemo.TestMockResponseFileReader
import com.nxb.githubsearchdemo.data.remote.RepoApi
import com.nxb.githubsearchdemo.data.responses.GithubResponse
import com.nxb.githubsearchdemo.mock
import com.nxb.githubsearchdemo.other.Resource
import com.nxb.githubsearchdemo.other.Status
import com.nxb.githubsearchdemo.util.TestUtils
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DefaultApiRepositoryTest {


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
    lateinit var defaultApiRepository: DefaultApiRepository

    lateinit var service: RepoApi
    private lateinit var mockWebServer: MockWebServer


    @Before
    fun init() {

        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())

                .build()
                .create(RepoApi::class.java)
        defaultApiRepository = DefaultApiRepository(service)


    }

    @Test
    fun testSearchWithLive() {
        enqueueResponse(
                "success_json.json"
        )
        runBlocking {

            val repo1 = TestUtils.createRepo("phonegap-start", "https://github.com/phonegap/phonegap-start", "PhoneGap Hello World app", "https://avatars.githubusercontent.com/u/60365?v=4")
            val repo2 = TestUtils.createRepo("HelloGitHub", "https://github.com/521xueweihan/HelloGitHub", ":octocat: 分享 GitHub 上有趣、入门级的开源项目", "https://avatars.githubusercontent.com/u/8255800?v=4")
            val githubResponse = GithubResponse(false, listOf(repo1, repo2), 2128713)
            val mockResponse = Response.success(githubResponse)
            val resp = defaultApiRepository.getSearches("hi", "1")
            assertThat(resp.status).isEqualTo(Status.SUCCESS)
            assertThat(resp).isEqualTo(Resource.success(mockResponse.body()))

        }

    }

    @Test
    fun testSearchWithMock() {

        service = mock()
        defaultApiRepository = DefaultApiRepository(service)

        runBlocking {
            val mockResponseBody = Mockito.mock(GithubResponse::class.java)
            val mockResponse = Response.success(mockResponseBody)
            Mockito.`when`(service.getSearches("hi", page = "1")).thenReturn(mockResponse)
            val resp = defaultApiRepository.getSearches("hi", page = "1")
            assertThat(resp).isEqualTo(Resource.success(mockResponse.body()))

        }

    }

    @Test
    fun testResponseBodyNull() {

        service = mock()
        defaultApiRepository = DefaultApiRepository(service)

        runBlocking {
            val mockResponseBody = Mockito.mock(GithubResponse::class.java)
            val mockResponse = Response.success(mockResponseBody)
            Mockito.`when`(service.getSearches("hi", page = "1")).thenReturn(mockResponse)
            val resp = defaultApiRepository.getSearches("hi", page = "1")
            assertThat(resp).isEqualTo(Resource.success(mockResponse.body()))

        }

    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {

        val source = TestMockResponseFileReader(fileName)
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
                mockResponse
                        .setBody(source.content)
        )
    }
}