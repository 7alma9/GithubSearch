package com.nxb.githubsearchdemo.ui

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
import com.nxb.githubsearchdemo.other.Event
import com.nxb.githubsearchdemo.other.Resource
import com.nxb.githubsearchdemo.other.Status
import com.nxb.githubsearchdemo.repository.DefaultApiRepository
import com.nxb.githubsearchdemo.util.TestUtils
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()


    var service = mock<RepoApi>()
    var defaultApiRepository = mock<DefaultApiRepository>()
    private lateinit var repoViewModel: MainViewModel

    @Before
    fun init() {
        repoViewModel = MainViewModel(defaultApiRepository)
    }

    @Test
    fun noObserverNoSearch() {
        assertThat(repoViewModel.githubResponse).isNotNull()


        repoViewModel.setQuery("hi")

        runBlocking {
            verify(defaultApiRepository, never()).getSearches(Mockito.anyString(), Mockito.anyString())
        }
    }

    @Test
    fun setObserverCanSearch() {

        repoViewModel.setQuery("hi")
        repoViewModel.githubResponse.observeForever(mock())

        runBlocking {
            verify(defaultApiRepository).getSearches(Mockito.anyString(), Mockito.anyString())
        }
    }

    @Test
    fun changeQueryLoadFromPage1() {

        val observer = mock<Observer<Event<Resource<GithubResponse>>>>()
        repoViewModel.githubResponse.observeForever(observer)
        verifyNoMoreInteractions(observer)
        verifyNoMoreInteractions(defaultApiRepository)
        repoViewModel.setQuery("hi")

        runBlocking {
            verify(defaultApiRepository).getSearches("hi", "1")
        }
        repoViewModel.setQuery("hi new")

        runBlocking {
            verify(defaultApiRepository).getSearches("hi", "1")
        }
    }


    @Test
    fun blankQuery() {
        val observer = Observer<Event<Resource<GithubResponse>>>() {
            assertThat(it.peakContent()).isEqualTo(Resource.error("Query Cannot be empty", null))
        }
        repoViewModel.setQuery("")
        repoViewModel.githubResponse.observeForever(observer)


        runBlocking {
            verify(defaultApiRepository, never()).getSearches("hi", "1")
         }

    }

    @Test
    fun paginationTest() {

        val observer = mock<Observer<Event<Resource<GithubResponse>>>>()
        repoViewModel.githubResponse.observeForever(observer)

        verifyNoMoreInteractions(observer)
        verifyNoMoreInteractions(defaultApiRepository)
        repoViewModel.setQuery("hi")

        runBlocking {

            verify(defaultApiRepository).getSearches("hi", "1")
        }
        repoViewModel.searchNextPage()

        runBlocking {
            verify(defaultApiRepository).getSearches("hi", "2")
        }


    }


    @Test
    fun pageStopTest(){
        val repo1 = TestUtils.createRepo("phonegap-start", "https://github.com/phonegap/phonegap-start", "PhoneGap Hello World app", "https://avatars.githubusercontent.com/u/60365?v=4")
        val repo2 = TestUtils.createRepo("HelloGitHub", "https://github.com/521xueweihan/HelloGitHub", ":octocat: 分享 GitHub 上有趣、入门级的开源项目", "https://avatars.githubusercontent.com/u/8255800?v=4")
        val githubResponse = GithubResponse(false, listOf(repo1, repo2), 2128713)
        val nextPage = Resource(Status.SUCCESS,githubResponse,null)


        val observer = mock<Observer<Event<Resource<GithubResponse>>>>()
        repoViewModel.githubResponse.observeForever(observer)
        repoViewModel.setQuery("hi")

        runBlocking {

            `when`(defaultApiRepository.getSearches("hi","1")).thenReturn(nextPage)
               verify(defaultApiRepository).getSearches("hi","1")
            assertThat(defaultApiRepository.getSearches("hi","1").data).isEqualTo(githubResponse)

        }
    }


}



