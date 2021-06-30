package com.nxb.githubsearchdemo.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.example.github.util.LiveDataCallAdapterFactory
import com.google.common.truth.Truth.assertThat
import com.nxb.githubsearchdemo.TestMockResponseFileReader
import com.nxb.githubsearchdemo.data.responses.GithubResponse
import com.nxb.githubsearchdemo.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
 import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
 import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class RepoApiTest{

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: RepoApi

    private lateinit var mockWebServer: MockWebServer
    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())

            .build()
            .create(RepoApi::class.java)
     }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }
    @Test
    fun searchSuccess() {

        enqueueResponse(
            "success_json.json"
        )
        runBlocking {
            val response = service.getSearches("foo",page = "1")
          
            assertThat(response.isSuccessful).isTrue()
            assertThat(response.body() ).isNotNull()
            assertThat(response.body()?.totalCount).isEqualTo(2128713)
            assertThat(response.body()?.items?.size).isEqualTo(2)

         }


    }
    @Test
    fun searchFailure() {

        enqueueResponse(
            "success_json.json"
        )
        runBlocking {
            val response = service.getSearches("foo",page = "1")
            assertThat(response.isSuccessful).isFalse()
            assertThat(response ).isNotNull()

        }


    }
    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {

        val source=TestMockResponseFileReader(fileName)
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.content )
        )
    }
}