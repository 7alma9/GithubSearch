package com.nxb.githubsearchdemo.ui

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.common.truth.Truth.assertThat
import com.nxb.githubsearchdemo.R
import com.nxb.githubsearchdemo.adapters.ReposAdapter
import com.nxb.githubsearchdemo.data.remote.RepoApi
import com.nxb.githubsearchdemo.data.responses.GithubResponse
import com.nxb.githubsearchdemo.getOrAwaitValuee
import com.nxb.githubsearchdemo.launchFragmentInHiltContainer
import com.nxb.githubsearchdemo.mockTest
import com.nxb.githubsearchdemo.other.Event
import com.nxb.githubsearchdemo.other.Resource
import com.nxb.githubsearchdemo.other.Status
import com.nxb.githubsearchdemo.repository.DefaultApiRepository
import com.nxb.githubsearchdemo.repository.FakeApiRepositoryTest
import com.nxb.githubsearchdemo.util.RecyclerViewMatcher
import com.nxb.githubsearchdemo.util.TestUtilss
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import javax.inject.Inject


@HiltAndroidTest
class MainFragmentTest {
    @Inject
    lateinit var customFragmentFactory: TestCustomFragmentFactory

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun setup() {
        hiltRule.inject()

    }


    @Test
    fun testLoaderVisibilityOnSearch() {

        launchFragmentInHiltContainer<MainFragment>(fragmentFactory = customFragmentFactory) {
        }
        onView(withId(R.id.progress)).check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.search_view)).perform(typeSearchViewText("salman"))
         onView(withId(R.id.progress)).check(ViewAssertions.matches(isDisplayed()))

    }


    @Test
    fun newTest() {
//        var testViewModel: MainViewModel? = null

        launchFragmentInHiltContainer<MainFragment>(fragmentFactory = customFragmentFactory) {

         }
        onView(withId(R.id.progress)).check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.search_view)).perform(typeSearchViewText("salman"))
        onView(withId(R.id.progress)).check(ViewAssertions.matches(isDisplayed()))

        onView(listMatcher().atPosition(0)).check(ViewAssertions.matches(hasDescendant(withText("Textview"))))
        onView(withId(R.id.progress)).check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))


    }
    @Test
    fun setQueryInViewModelWhenHitSearch() {
          launchFragmentInHiltContainer<MainFragment>(fragmentFactory = customFragmentFactory) {

         }
         onView(withId(R.id.search_view)).perform(typeSearchViewText("salman"))




    }
    @Test
    fun testDataLoad() {

        launchFragmentInHiltContainer<MainFragment>(fragmentFactory = customFragmentFactory) {

        }
        onView(withId(R.id.progress)).check(ViewAssertions.matches(CoreMatchers.not(isDisplayed())))
        onView(withId(R.id.search_view)).perform(typeSearchViewText("salman"))
        onView(listMatcher().atPosition(0)).check(ViewAssertions.matches(isDisplayed()))

    }


    fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.rv_repos)

    }

    fun typeSearchViewText(text: String): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return text
            }

            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as SearchView).setQuery(text, true)
            }
        }
    }


}